import { computed, ref } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import { useToast } from "primevue/usetoast";

export const useLobbyStore = defineStore("lobby", () => {
  const toast = useToast();

  const lobbies = ref([]);

  const getLobbies = computed(() => lobbies.value);

  const urls = {
    lobbies: "http://localhost:3000/api/lobbies",
  };

  async function fetchLobbies() {
    try {
      const response = await axios.get(urls.lobbies);
      lobbies.value = response.data;
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "error.",
        detail: error,
        life: 3000,
      });
    }
  }

  return {
    getLobbies,
    fetchLobbies,
  };
});
