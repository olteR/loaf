import { computed, ref } from "vue";
import { defineStore } from "pinia";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client/dist/sockjs";
import { useLobbyStore } from "@/stores/lobbies";
import { useGameStore } from "@/stores/games";
import { useStateStore } from "@/stores/state";
import { SUBSCRIPTION_TYPE } from "@/utils/const";

export const useWebsocketStore = defineStore("websocket", () => {
  const stateStore = useStateStore();
  const lobbyStore = useLobbyStore();
  const gameStore = useGameStore();
  const connected = ref(false);
  const stompClient = ref();
  const subscription = ref();
  const subType = ref();

  const isConnected = computed(() => connected.value);

  function connect() {
    if (!connected.value) {
      stompClient.value = Stomp.over(
        new SockJS(`${window.location.origin}/ws?${stateStore.getJwt}`)
      );
      stompClient.value.connect({}, connectCallback, errorCallback);
    }
  }

  function disconnect() {
    stompClient.value?.disconnect();
    subType.value = null;
    connected.value = false;
  }

  function subscribeToLobby() {
    subscription.value = stompClient.value.subscribe(
      "/user/topic/lobby/update",
      lobbyStore.lobbyUpdateHandler
    );
    subType.value = SUBSCRIPTION_TYPE.LOBBY;
  }

  function subscribeToGame() {
    subscription.value = stompClient.value.subscribe(
      "/user/topic/game/update",
      gameStore.gameUpdateHandler
    );
    subType.value = SUBSCRIPTION_TYPE.GAME;
  }

  function unsubscribe() {
    subscription.value?.unsubscribe();
    subType.value = null;
  }

  const connectCallback = function (frame) {
    console.log("Connected!");
    connected.value = true;
    console.log(frame);
    if (subType.value === SUBSCRIPTION_TYPE.LOBBY) {
      subscribeToLobby();
    } else if (subType.value === SUBSCRIPTION_TYPE.GAME) {
      subscribeToGame();
    }
  };

  const errorCallback = function (error) {
    console.log(error);
    connected.value = false;
    console.log("Reconnecting...");
    setTimeout(connect, 5000);
  };

  return {
    isConnected: isConnected,
    connect,
    disconnect,
    subscribeToLobby,
    subscribeToGame,
    unsubscribe,
  };
});
