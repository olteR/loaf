import { computed, ref } from "vue";
import { defineStore } from "pinia";
import router from "@/router";
import { useToast } from "primevue/usetoast";
import { REQ_TYPE, useRequestStore } from "@/stores/request";
import { LOBBY_UPDATE } from "@/utils/const";
import { useStateStore } from "@/stores/state";

export const useLobbyStore = defineStore("lobby", () => {
  const toast = useToast();
  const requestStore = useRequestStore();
  const stateStore = useStateStore();
  const lobby = ref();
  const lobbies = ref([]);

  const getLobby = computed(() => lobby.value);
  const getLobbies = computed(() => lobbies.value);

  const urls = {
    myGames: `my-games`,
    lobby: `lobby`,
    lobbies: `lobbies`,
    join: `lobby/join`,
    leave: (code) => `lobby/${code}/leave`,
    promote: `lobby/promote`,
    kick: `lobby/kick`,
    characters: `lobby/characters`,
    districts: `lobby/districts`,
    crown: `lobby/crown`,
    start: (code) => `lobby/${code}/start`,
    delete: (code) => `lobby/${code}/delete`,
  };

  async function fetchLobby(code) {
    const response = await requestStore.request(
      `${urls.lobby}/${code}`,
      REQ_TYPE.GET
    );
    lobby.value = response.data;
  }

  async function fetchLobbies() {
    const response = await requestStore.request(urls.lobbies, REQ_TYPE.GET);
    lobbies.value = response.data;
  }

  async function fetchMyGames() {
    const response = await requestStore.request(urls.myGames, REQ_TYPE.GET);
    lobbies.value = response.data;
  }

  async function createLobby(request) {
    const response = await requestStore.request(
      urls.lobby,
      REQ_TYPE.POST,
      request
    );
    lobby.value = response.data;
    await router.push(`/lobby/${lobby.value.code}`);
  }

  async function editLobby(request, code) {
    const response = await requestStore.request(
      `${urls.lobby}/${code}`,
      REQ_TYPE.POST,
      request
    );
    lobby.value = response.data;
  }

  async function joinLobby(request) {
    const response = await requestStore.request(
      urls.join,
      REQ_TYPE.PATCH,
      request
    );
    lobby.value = response.data;
    await router.push(`/lobby/${lobby.value.code}`);
  }

  async function leaveLobby(code) {
    await requestStore.request(urls.leave(code), REQ_TYPE.POST);
    lobby.value = null;
    await router.push("/my-games");
  }

  async function promoteMember(code, id) {
    await requestStore.request(urls.promote, REQ_TYPE.POST, {
      code: code,
      memberId: id,
    });
  }

  async function kickMember(code, id) {
    await requestStore.request(urls.kick, REQ_TYPE.POST, {
      code: code,
      memberId: id,
    });
  }

  async function updateCharacters(code, characters) {
    await requestStore.request(urls.characters, REQ_TYPE.POST, {
      code: code,
      ids: characters,
    });
  }

  async function updateDistricts(code, districts) {
    await requestStore.request(urls.districts, REQ_TYPE.POST, {
      code: code,
      ids: districts,
    });
  }

  async function crownMember(code, id) {
    await requestStore.request(urls.crown, REQ_TYPE.POST, {
      code: code,
      memberId: id,
    });
  }

  async function startGame(code) {
    await requestStore.request(urls.start(code), REQ_TYPE.POST);
  }

  async function deleteLobby(code) {
    await requestStore.request(urls.delete(code), REQ_TYPE.DELETE);
    lobby.value = null;
  }

  const lobbyUpdateHandler = function handleLobbyUpdate(msg) {
    const update = JSON.parse(msg.body);
    if (update.code === lobby.value.code) {
      switch (update.type) {
        case LOBBY_UPDATE.JOIN: {
          lobby.value.members.push(update.change);
          toast.add({
            severity: "success",
            summary: "Felhasználó csatlakozott",
            detail: `${update.change.name} csatlakozott a lobbihoz!`,
            life: 3000,
          });
          break;
        }
        case LOBBY_UPDATE.LEAVE: {
          const user = lobby.value.members.find((m) => m.id === update.change);
          lobby.value.members.splice(lobby.value.members.indexOf(user), 1);
          toast.add({
            severity: "warn",
            summary: "Felhasználó kilépett",
            detail: `${user.name} elhagyta a lobbit!`,
            life: 3000,
          });
          break;
        }
        case LOBBY_UPDATE.OWNER: {
          lobby.value.owner = update.change;
          toast.add({
            severity: "info",
            summary: "Új lobbi tulajdonos",
            detail: `${
              lobby.value.members.find((m) => m.id === update.change).name
            } a lobbi új tulajdonosa!`,
            life: 3000,
          });
          break;
        }
        case LOBBY_UPDATE.KICK: {
          const user = lobby.value.members.find((m) => m.id === update.change);
          lobby.value.members.splice(lobby.value.members.indexOf(user), 1);
          if (update.change === stateStore.getUser.id) {
            router.push("/my-games");
            toast.add({
              severity: "error",
              summary: "Eltávolítva",
              detail: `A tulajdonos eltávolított a lobbiból!`,
              life: 3000,
            });
          } else {
            toast.add({
              severity: "error",
              summary: "Felhasználó eltávolítva",
              detail: `${user.name} eltávolítva a lobbiból!`,
              life: 3000,
            });
          }
          break;
        }
        case LOBBY_UPDATE.CHARACTERS: {
          lobby.value.gameSettings.characters = update.change;
          break;
        }
        case LOBBY_UPDATE.DISTRICTS: {
          lobby.value.gameSettings.uniqueDistricts = update.change;
          break;
        }
        case LOBBY_UPDATE.CROWN: {
          lobby.value.gameSettings.crownedPlayer = update.change;
          break;
        }
        case LOBBY_UPDATE.START: {
          toast.add({
            severity: "info",
            summary: "A játék indul",
            detail:
              "A lobbitulajdonos elindította a játékot, ami rögtön kezdetét veszi!",
            life: 3000,
          });
          router.push("/game/" + lobby.value.code);
          break;
        }
        case LOBBY_UPDATE.DELETE: {
          toast.add({
            severity: "error",
            summary: "Játék törölve",
            detail: "A lobbitulajdonos kitörölte a játékot!",
            life: 3000,
          });
          router.push("/my-games");
          break;
        }
      }
    }
  };

  return {
    getLobby: getLobby,
    getLobbies: getLobbies,
    fetchLobby,
    fetchLobbies,
    fetchMyGames,
    createLobby,
    editLobby,
    joinLobby,
    leaveLobby,
    promoteMember,
    kickMember,
    updateCharacters,
    updateDistricts,
    crownMember,
    startGame,
    deleteLobby,
    lobbyUpdateHandler,
  };
});
