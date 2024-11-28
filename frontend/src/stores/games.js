import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { REQ_TYPE, useRequestStore } from "@/stores/request";
import { ABILITY, GAME_PHASE, GAME_UPDATE, RESOURCE } from "@/utils/const";
import { useStateStore } from "@/stores/state";
import router from "@/router";

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
    result: (code) => `game/${code}/result`,
  };

  const game = ref({});
  const gameResult = ref();

  const getGame = computed(() => game.value);
  const getGameResult = computed(() => gameResult.value);
  const getCurrentPlayer = computed(
    () =>
      game.value.players?.find(
        (player) => player.id === game.value.currentPlayer
      ) ?? {}
  );
  const getCharacter = computed(
    () => game.value.characters?.[game.value.character - 1]
  );

  async function fetchGame(code) {
    const response = await requestStore.request(
      urls.details(code),
      REQ_TYPE.GET
    );
    game.value = response.data;
  }

  async function fetchGameResult(code) {
    const response = await requestStore.request(
      urls.result(code),
      REQ_TYPE.GET
    );
    gameResult.value = response.data;
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
    return await requestStore.request(
      urls.resource(code) + "?type=" + resource,
      REQ_TYPE.GET
    );
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
          if (
            getCharacter.value.abilities.find(
              (ability) => ability.enum === ABILITY.WITCH
            ) &&
            game.value.bewitchedCharacter === null
          ) {
            game.value.usingAbility = ABILITY.WITCH;
          }
          if (
            game.value.bewitchedCharacter === getCurrentPlayer.value.character
          ) {
            game.value.currentPlayer = game.value.players.find(
              (player) => player.character === 1
            ).id;
          }
          if (
            game.value.threatenedCharacters.includes(
              getCurrentPlayer.value.character
            ) &&
            getCurrentPlayer.value.userId === stateStore.getUser.id
          ) {
            game.value.usingAbility = ABILITY.PAY_OFF;
          }
          break;
        }
        case GAME_UPDATE.END_GAME:
          router.push(`/game-results/${game.value.code}`);
          break;
        default: {
          game.value = update.change;
          game.value.players.sort((a, b) => a.order - b.order);
          break;
        }
      }
    }
  };

  return {
    getGame: getGame,
    getGameResult: getGameResult,
    getCurrentPlayer: getCurrentPlayer,
    getCharacter: getCharacter,
    fetchGame,
    fetchGameResult,
    selectCharacter,
    gatherResources,
    drawCards,
    buildDistrict,
    endTurn,
    useAbility,
    gameUpdateHandler,
  };
});
