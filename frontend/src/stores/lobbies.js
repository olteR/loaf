import { computed, ref } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import { useToast } from "primevue/usetoast";

export const useLobbyStore = defineStore("lobby", () => {
  const toast = useToast();

  const lobby = ref();
  const lobbies = ref([]);

  const getLobby = computed(() => lobby.value);
  const getLobbies = computed(() => lobbies.value);

  const urls = {
    join: "http://localhost:3000/api/join/",
    myGames: "http://localhost:3000/api/my-games",
    lobby: "http://localhost:3000/api/lobby",
    lobbies: "http://localhost:3000/api/lobbies",
  };

  async function fetchLobbies() {
    try {
      const response = await axios.get(urls.lobbies);
      lobbies.value = response.data;
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function fetchMyGames() {
    try {
      const response = await axios.get(urls.myGames);
      lobbies.value = response.data;
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function createLobby(request) {
    try {
      const response = await axios.post(urls.lobby, request);
      lobby.value = response.data;
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function joinLobby(code) {
    try {
      const response = await axios.patch(urls.join.concat(code));
      lobby.value = response.data;
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
    getLobby,
    getLobbies,
    fetchLobbies,
    fetchMyGames,
    createLobby,
    joinLobby,
  };
});
