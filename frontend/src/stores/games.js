import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { REQ_TYPE, useRequestStore } from "@/stores/request";
import { GAME_PHASE, GAME_UPDATE, RESOURCE } from "@/utils/const";
import { useStateStore } from "@/stores/state";

export const useGameStore = defineStore("game", () => {
  const requestStore = useRequestStore();
  const stateStore = useStateStore();
  const urls = {
    details: (code) => `game/${code}/details`,
    select: (code) => `game/${code}/select`,
    resource: (code) => `game/${code}/resource`,
    cards: (code) => `game/${code}/cards`,
    build: (code) => `game/${code}/build`,
  };

  const game = ref();

  const getGame = computed(() => game.value);
  const getCurrentPlayer = computed(
    () =>
      game.value?.players.find(
        (player) => player.id === game.value.currentPlayer
      ) ?? {}
  );

  async function fetchGame(code) {
    const response = await requestStore.request(
      urls.details(code),
      REQ_TYPE.GET
    );
    game.value = response.data;
  }

  async function selectCharacter(code, character) {
    const response = await requestStore.request(
      urls.select(code) + "?character=" + character,
      REQ_TYPE.GET
    );
    game.value.currentCharacter = character;
    game.value.skippedCharacters = response.data;
  }

  async function gatherResources(code, resource) {
    const response = await requestStore.request(
      urls.resource(code) + "?type=" + resource,
      REQ_TYPE.GET
    );
    game.value.drawnCards = response.data;
  }

  async function drawCards(code, cards) {
    const response = await requestStore.request(
      urls.cards(code),
      REQ_TYPE.POST,
      cards
    );
    game.value.drawnCards = [];
    game.value.hand.push(...response.data);
  }

  async function buildDistrict(code, request) {
    await requestStore.request(urls.build(code), REQ_TYPE.POST, request);
  }

  const gameUpdateHandler = function handleGameUpdate(msg) {
    const update = JSON.parse(msg.body);
    if (update.code === game.value.code) {
      switch (update.type) {
        case GAME_UPDATE.NEXT_PLAYER: {
          game.value.currentPlayer = update.change;
          break;
        }
        case GAME_UPDATE.PLAYER_TURN: {
          game.value.currentPlayer = game.value.players.find(
            (player) => player.userId === stateStore.getUser.id
          ).id;
          game.value.unavailableCharacters = update.change;
          break;
        }
        case GAME_UPDATE.CHARACTER_REVEAL: {
          game.value.phase = GAME_PHASE.RESOURCE;
          game.value.currentPlayer = update.change.id;
          game.value.player = game.value.players.map((player) =>
            player.id === update.change.id ? update.change : player
          );
          break;
        }
        case GAME_UPDATE.RESOURCE_COLLECTION: {
          let player = game.value.players.find(
            (p) => p.id === game.value.currentPlayer.id
          );
          player[
            update.change.resource === RESOURCE.CARDS ? "handSize" : "gold"
          ] += update.change.amount;
          game.value.currentPlayer = player;
          break;
        }
        default: {
          console.log(`UNKNOWN GAME UPDATE: ${update.type}`);
        }
      }
    }
  };

  return {
    getGame: getGame,
    getCurrentPlayer: getCurrentPlayer,
    fetchGame,
    selectCharacter,
    gatherResources,
    drawCards,
    buildDistrict,
    gameUpdateHandler,
  };
});
