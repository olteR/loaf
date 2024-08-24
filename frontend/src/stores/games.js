import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { REQ_TYPE, storeRequest, storeUrls } from "@/stores/storeUtils";

export const useGameStore = defineStore("game", () => {
  const urls = storeUrls({
    details: (code) => `game/${code}/details`,
    select: (code) => `game/${code}/select`,
    resource: (code) => `game/${code}/resource`,
  });

  const gameDetails = ref();

  const getGameDetails = computed(() => gameDetails.value);

  async function fetchGameDetails(code) {
    const response = await storeRequest(urls.details(code), REQ_TYPE.GET);
    gameDetails.value = response.data;
  }

  async function selectCharacter(code, character) {
    const response = await storeRequest(
      urls.select(code) + "?character=" + character,
      REQ_TYPE.GET
    );
    gameDetails.value.currentCharacter = character;
    gameDetails.value.skippedCharacters = response.data;
  }

  async function gatherResources(code, resource) {
    const response = await storeRequest(
      urls.resource(code) + "?type=" + resource,
      REQ_TYPE.GET
    );
    gameDetails.value.drawnCards = response.data;
  }

  return {
    getGameDetails: getGameDetails,
    fetchGameDetails,
    selectCharacter,
    gatherResources,
  };
});
