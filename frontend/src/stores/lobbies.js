import { computed, ref } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import { useToast } from "primevue/usetoast";
import router from "@/router";

export const useLobbyStore = defineStore("lobby", () => {
  const baseUrl = window.location.origin;
  const toast = useToast();

  const lobby = ref();
  const lobbies = ref([]);

  const getLobby = computed(() => lobby.value);
  const getLobbies = computed(() => lobbies.value);

  const urls = {
    join: (code) => `${baseUrl}/api/lobby/${code}/join`,
    leave: (code) => `${baseUrl}/api/lobby/${code}/leave`,
    kick: `${baseUrl}/api/lobby/kick`,
    promote: `${baseUrl}/api/lobby/promote`,
    myGames: `${baseUrl}/api/my-games`,
    lobby: `${baseUrl}/api/lobby`,
    lobbies: `${baseUrl}/api/lobbies`,
    start: (code) => `${baseUrl}/api/lobby/${code}/start`,
    delete: (code) => `${baseUrl}/api/lobby/${code}/delete`,
  };

  async function fetchLobby(code) {
    try {
      const response = await axios.get(urls.lobby.concat("/").concat(code));
      lobby.value = response.data;
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
      await router.push("/lobbies");
    }
  }

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
      await router.push("/lobby/".concat(lobby.value.code));
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function deleteLobby(code) {
    try {
      await axios.delete(urls.delete(code));
      lobby.value = null;
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
      const response = await axios.patch(urls.join(code));
      lobby.value = response.data;
      await router.push("/lobby/".concat(lobby.value.code));
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function leaveLobby(code) {
    try {
      await axios.post(urls.leave(code));
      lobby.value = null;
      await router.push("/my-games");
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function kickMember(code, id) {
    try {
      await axios.post(urls.kick, {
        code: code,
        memberId: id,
      });
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function promoteMember(code, id) {
    try {
      await axios.post(urls.promote, {
        code: code,
        memberId: id,
      });
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
      });
    }
  }

  async function startGame(code) {
    try {
      const response = await axios.get(urls.start(code));
      console.log(response);
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
    fetchLobby,
    fetchLobbies,
    fetchMyGames,
    createLobby,
    deleteLobby,
    joinLobby,
    leaveLobby,
    kickMember,
    promoteMember,
    startGame,
  };
});
