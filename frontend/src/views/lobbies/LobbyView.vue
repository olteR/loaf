<template>
  <div class="container mx-auto my-4" v-if="lobbyStore.getLobby">
    <Card>
      <template #title>{{ lobbyStore.getLobby.name }}</template>
      <template #content>
        <p>
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Inventore sed consequuntur error repudiandae numquam deserunt quisquam repellat libero asperiores earum nam nobis, culpa ratione quam perferendis esse, cupiditate neque
          quas!
        </p>
      </template>
    </Card>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";
import Card from "primevue/card";

const router = useRouter();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const lobbyCode = router.currentRoute.value.params.code;
const socket = new SockJS("http://localhost:3000/ws/lobby/" + lobbyCode);
const stompClient = Stomp.over(socket);

const received = ref([]);
const connected = ref(false);

onMounted(async () => {
  stateStore.setLoading(true);
  if (!lobbyStore.getLobby || lobbyStore.getLobby.code !== lobbyCode) {
    await lobbyStore.fetchLobby(lobbyCode);
  }
  stateStore.getBreadcrumbs.push({
    name: "lobby",
    label: lobbyStore.getLobby.name,
    params: router.currentRoute.value.params,
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
