import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { useToast } from "primevue/usetoast";
import axios from "axios";

export const useGameStore = defineStore("game", () => {
  const baseUrl = window.location.origin;
  const toast = useToast();

  const urls = {
    details: (code) => `${baseUrl}/api/game/${code}/details`,
    select: (code) => `${baseUrl}/api/game/${code}/select`,
    resource: (code) => `${baseUrl}/api/game/${code}/resource`,
  };

  const gameDetails = ref();

  const getGameDetails = computed(() => gameDetails.value);

  async function fetchGameDetails(code) {
    try {
      const response = await axios.get(urls.details(code));
      gameDetails.value = response.data;
    } catch (error) {
      handleError(error);
    }
  }

  async function selectCharacter(code, character) {
    try {
      const response = await axios.get(
        urls.select(code) + "?character=" + character
      );
      gameDetails.value.currentCharacter = character;
      gameDetails.value.skippedCharacters = response.data;
    } catch (error) {
      handleError(error);
    }
  }

  async function gatherResources(code, resource) {
    try {
      const response = await axios.get(
        urls.resource(code) + "?type=" + resource
      );
      gameDetails.value.drawnCards = response.data;
    } catch (error) {
      handleError(error);
    }
  }

  function handleError(error) {
    toast.add({
      severity: "error",
      summary: "hiba.",
      detail: error,
      life: 3000,
    });
  }

  return {
    getGameDetails: getGameDetails,
    fetchGameDetails,
    selectCharacter,
    gatherResources,
  };
});
