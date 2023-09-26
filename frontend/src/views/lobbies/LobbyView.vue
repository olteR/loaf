<template>
  <div class="container mx-auto my-4" v-if="lobbyStore.getLobby">
    {{ lobbyStore.getLobby.name }}
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";

const router = useRouter();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const socket = new SockJS("http://localhost:3000/ws");
const stompClient = Stomp.over(socket);

const received = ref([]);
const connected = ref(false);

onMounted(async () => {
  stateStore.setLoading(true);
  const params = router.currentRoute.value.params;
  if (!lobbyStore.getLobby || lobbyStore.getLobby.code !== params.code) {
    await lobbyStore.fetchLobby(params.code);
  }
  stateStore.getBreadcrumbs.push({
    name: "lobby",
    label: lobbyStore.getLobby.name,
    params: params,
  });
  connect();
  stateStore.setLoading(false);
});

function connect() {
  stompClient.connect(
    {
      Authorization: "Bearer ".concat(stateStore.getJwt),
    },
    (frame) => {
      connected.value = true;
      console.log(frame);
      stompClient.subscribe("/topic/greetings", (tick) => {
        console.log(tick);
        received.value.push(JSON.parse(tick.body).content);
      });
    },
    (error) => {
      console.log(error);
      connected.value = false;
    }
  );
}
</script>

<style scoped></style>
