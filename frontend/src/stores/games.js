import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { useToast } from "primevue/usetoast";
import axios from "axios";

export const useGameStore = defineStore("game", () => {
  const baseUrl = window.location.origin;
  const toast = useToast();

  const urls = {
    details: (code) => `${baseUrl}/api/game/${code}/details`,
    state: (code) => `${baseUrl}/api/game/${code}/state`,
  };

  const gameDetails = ref();
  const gameState = ref();

  const getGameDetails = computed(() => gameDetails.value);
  const getGameState = computed(() => gameState.value);

  async function fetchGameDetails(code) {
    try {
      const response = await axios.get(urls.details(code));
      gameDetails.value = response.data;
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function fetchGameState(code) {
    try {
      const response = await axios.get(urls.state(code));
      gameState.value = response.data;
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  return {
    getGameState: getGameState,
    getGameDetails: getGameDetails,
    fetchGameState,
    fetchGameDetails,
  };
});
