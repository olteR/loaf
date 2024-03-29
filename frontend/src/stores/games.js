import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { useToast } from "primevue/usetoast";
import axios from "axios";

export const useGameStore = defineStore("game", () => {
  const baseUrl = window.location.origin;
  const toast = useToast();

  const urls = {
    state: (code) => `${baseUrl}/api/game/${code}/state`,
  };

  const gameState = ref();

  const getGameState = computed(() => gameState.value);

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
    fetchGameState,
  };
});
