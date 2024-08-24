import { computed, ref } from "vue";
import { defineStore } from "pinia";
import router from "@/router";
import { REQ_TYPE, storeRequest, storeUrls } from "@/stores/storeUtils";

export const useLobbyStore = defineStore("lobby", () => {
  const lobby = ref();
  const lobbies = ref([]);

  const getLobby = computed(() => lobby.value);
  const getLobbies = computed(() => lobbies.value);

  const urls = storeUrls({
    join: (code) => `lobby/${code}/join`,
    leave: (code) => `lobby/${code}/leave`,
    kick: `lobby/kick`,
    promote: `lobby/promote`,
    myGames: `my-games`,
    lobby: `lobby`,
    lobbies: `lobbies`,
    start: (code) => `lobby/${code}/start`,
    delete: (code) => `lobby/${code}/delete`,
  });

  async function fetchLobby(code) {
    const response = await storeRequest(
      urls.lobby.concat("/").concat(code),
      REQ_TYPE.GET
    );
    lobby.value = response.data;
  }

  async function fetchLobbies() {
    const response = await storeRequest(urls.lobbies, REQ_TYPE.GET);
    lobbies.value = response.data;
  }

  async function fetchMyGames() {
    const response = await storeRequest(urls.myGames, REQ_TYPE.GET);
    lobbies.value = response.data;
  }

  async function createLobby(request) {
    const response = await storeRequest(urls.lobby, REQ_TYPE.POST, request);
    lobby.value = response.data;
    await router.push("/lobby/".concat(lobby.value.code));
  }

  async function deleteLobby(code) {
    await storeRequest(urls.delete(code), REQ_TYPE.DELETE);
    lobby.value = null;
  }

  async function joinLobby(code) {
    const response = await storeRequest(urls.join(code), REQ_TYPE.PATCH);
    lobby.value = response.data;
    await router.push("/lobby/".concat(lobby.value.code));
  }

  async function leaveLobby(code) {
    await storeRequest(urls.leave(code), REQ_TYPE.POST);
    lobby.value = null;
    await router.push("/my-games");
  }

  async function kickMember(code, id) {
    await storeRequest(urls.kick, REQ_TYPE.POST, {
      code: code,
      memberId: id,
    });
  }

  async function promoteMember(code, id) {
    await storeRequest(urls.promote, REQ_TYPE.POST, {
      code: code,
      memberId: id,
    });
  }

  async function startGame(code) {
    await storeRequest(urls.start(code), REQ_TYPE.POST);
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
