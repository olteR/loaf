import { computed, ref } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import { useToast } from "primevue/usetoast";
import router from "@/router";

export const useLobbyStore = defineStore("lobby", () => {
  const toast = useToast();

  const lobby = ref();
  const lobbies = ref([]);

  const getLobby = computed(() => lobby.value);
  const getLobbies = computed(() => lobbies.value);

  const urls = {
    join: (code) => `http://localhost:3000/api/lobby/${code}/join`,
    leave: (code) => `http://localhost:3000/api/lobby/${code}/leave`,
    kick: "http://localhost:3000/api/lobby/kick",
    promote: "http://localhost:3000/api/lobby/promote",
    myGames: "http://localhost:3000/api/my-games",
    lobby: "http://localhost:3000/api/lobby",
    lobbies: "http://localhost:3000/api/lobbies",
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

  return {
    getLobby,
    getLobbies,
    fetchLobby,
    fetchLobbies,
    fetchMyGames,
    createLobby,
    joinLobby,
    leaveLobby,
    kickMember,
    promoteMember,
  };
});
