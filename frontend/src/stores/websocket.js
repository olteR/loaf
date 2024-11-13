import { computed, ref } from "vue";
import { defineStore } from "pinia";
import Stomp from "webstomp-client";
import { useLobbyStore } from "@/stores/lobbies";
import { useGameStore } from "@/stores/games";

export const useWebsocketStore = defineStore("websocket", () => {
  const lobbyStore = useLobbyStore();
  const gameStore = useGameStore();
  const connected = ref(false);
  const socket = ref();
  const stompClient = ref();
  const subscription = ref();

  const isConnected = computed(() => connected.value);

  function connect(sock) {
    if (!isConnected.value) {
      socket.value = sock;
      stompClient.value = Stomp.over(socket);
      stompClient.value.connect({}, connectCallback, errorCallback);
    }
  }

  function disconnect() {
    stompClient.value?.disconnect();
  }

  function subscribeToLobby() {
    subscription.value = stompClient.value.subscribe(
      "/user/topic/lobby/update",
      lobbyStore.lobbyUpdateHandler
    );
  }

  function subscribeToGame() {
    subscription.value = stompClient.value.subscribe(
      "/user/topic/game/update",
      gameStore.gameUpdateHandler
    );
  }

  function unsubscribe() {
    subscription.value.unsubscribe();
  }

  const connectCallback = function (frame) {
    console.log("Connected!");
    connected.value = true;
    console.log(frame);
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
