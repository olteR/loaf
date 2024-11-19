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
    endTurn: (code) => `game/${code}/end-turn`,
    ability: `game/ability`,
  };

  const game = ref();

  const getGame = computed(() => game.value);
  const getCurrentPlayer = computed(
    () =>
      game.value?.players.find(
        (player) => player.id === game.value.currentPlayer
      ) ?? {}
  );
  const getCharacter = computed(
    () =>
      game.value?.characters.find(
        (character) => character.number === game.value.character
      ) ?? null
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
    game.value.character = character;
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

  async function buildDistrict(code, index) {
    await requestStore.request(
      `${urls.build(code)}?index=${index}`,
      REQ_TYPE.GET
    );
  }

  async function endTurn(code) {
    await requestStore.request(urls.endTurn(code), REQ_TYPE.GET);
  }

  async function useAbility(request) {
    await requestStore.request(urls.ability, REQ_TYPE.POST, request);
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
          game.value.players = game.value.players.map((player) =>
            player.id === update.change.id ? update.change : player
          );
          game.value.currentPlayer = update.change.id;
          game.value.phase = GAME_PHASE.RESOURCE;
          break;
        }
        case GAME_UPDATE.RESOURCE_COLLECTION: {
          game.value.players = game.value.players.map((player) => {
            if (player.id === game.value.currentPlayer) {
              player[
                update.change.resource === RESOURCE.CARDS ? "handSize" : "gold"
              ] += parseInt(update.change.amount);
            }
            return player;
          });
          game.value.phase = GAME_PHASE.TURN;
          break;
        }
        case GAME_UPDATE.BUILD: {
          game.value.players = game.value.players.map((player) => {
            if (player.id === game.value.currentPlayer) {
              player.districts.push(update.change);
              player.handSize -= 1;
              player.gold -= update.change.cost;
            }
            return player;
          });
          break;
        }
        case GAME_UPDATE.NEW_TURN:
        case GAME_UPDATE.USE_ABILITY: {
          game.value = update.change;
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
    getCharacter: getCharacter,
    fetchGame,
    selectCharacter,
    gatherResources,
    drawCards,
    buildDistrict,
    endTurn,
    useAbility,
    gameUpdateHandler,
  };
});
