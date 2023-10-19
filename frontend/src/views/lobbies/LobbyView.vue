<template>
  <div>
    <div class="container mx-auto my-4" v-if="lobbyStore.getLobby">
      <Card>
        <template #title
          ><h1 class="text-4xl">{{ lobbyStore.getLobby.name }}</h1></template
        >
        <template #content>
          <h2 class="text-2xl mb-2">
            Játékosok ({{ lobbyStore.getLobby.members.length }}/{{
              lobbyStore.getLobby.maxMembers
            }}):
          </h2>
          <Chip
            v-for="player in lobbyStore.getLobby.members"
            :key="player.id"
            class="mr-1"
          >
            <i
              v-if="player.id === lobbyStore.getLobby.owner"
              class="fa fa-crown mr-1"
            />
            {{ player.displayName }}
          </Chip>
        </template>
      </Card>
    </div>
    <OwnerLobbySettings v-if="isOwner"></OwnerLobbySettings>
    <PlayerLobbySettings v-else></PlayerLobbySettings>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";
import Card from "primevue/card";
import Chip from "primevue/chip";
import OwnerLobbySettings from "@/views/lobbies/OwnerLobbySettings.vue";
import PlayerLobbySettings from "@/views/lobbies/PlayerLobbySettings.vue";

const router = useRouter();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const lobbyCode = router.currentRoute.value.params.code;
const socket = new SockJS("http://localhost:3000/ws/lobby/" + lobbyCode);
const stompClient = Stomp.over(socket);

const received = ref([]);
const connected = ref(false);

const isOwner = computed(
  () => stateStore.getUser.id === lobbyStore.getLobby?.owner
);

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
      stompClient.subscribe("/topic/update", (tick) => {
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
