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
          <span v-for="player in lobbyStore.getLobby.members" :key="player.id">
            <Chip :key="player.id" class="mr-1">
              <i
                v-if="player.id === lobbyStore.getLobby.owner"
                class="fa fa-crown mr-1 my-3"
              />
              {{ player.displayName }}
              <span v-if="isOwner && stateStore.getUser.id !== player.id">
                <Button
                  type="button"
                  icon="fa fa-gear"
                  class="playerOptionsButton"
                  @click="toggle($event, player.id)"
                />

                <OverlayPanel
                  :ref="
                    (el) => {
                      playerPanels[player.id] = el;
                    }
                  "
                >
                  <Button
                    v-tooltip.bottom="'Tulajdonossá nevezés'"
                    icon="fa fa-crown"
                  />
                  <Button
                    v-tooltip.bottom="'Eltávolítás a lobbiból'"
                    class="ml-2 p-button-danger"
                    icon="fa fa-x"
                  />
                </OverlayPanel>
              </span>
            </Chip>
          </span>

          <Button class="float-right" @click="lobbyStore.leaveLobby(lobbyCode)"
            >Játék elhagyása</Button
          >
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
import Button from "primevue/button";
import Card from "primevue/card";
import Chip from "primevue/chip";
import OverlayPanel from "primevue/overlaypanel";
import OwnerLobbySettings from "@/views/lobbies/OwnerLobbySettings.vue";
import PlayerLobbySettings from "@/views/lobbies/PlayerLobbySettings.vue";

const router = useRouter();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const lobbyCode = router.currentRoute.value.params.code;
const socket = new SockJS("http://localhost:3000/ws?" + stateStore.getJwt);
const stompClient = Stomp.over(socket);
const playerPanels = ref({});

const connected = ref(false);

const isOwner = computed(
  () => stateStore.getUser.id === lobbyStore.getLobby?.owner
);

onMounted(async () => {
  stateStore.setLoading(true);
  await lobbyStore.fetchLobby(lobbyCode);
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
    {},
    (frame) => {
      console.log("Connected!");
      connected.value = true;
      console.log(frame);
      stompClient.subscribe("/user/topic/lobby/update", (msg) => {
        handleLobbyUpdate(JSON.parse(msg.body));
      });
    },
    (error) => {
      console.log(error);
      connected.value = false;
    }
  );
}

function handleLobbyUpdate(update) {
  switch (update.type) {
    case "JOIN": {
      lobbyStore.getLobby.members.push(update.change);
      break;
    }
    case "LEAVE": {
      const user = lobbyStore.getLobby.members.find(
        (m) => m.id === update.change
      );
      lobbyStore.getLobby.members.splice(
        lobbyStore.getLobby.members.indexOf(user),
        1
      );
      break;
    }
  }
}

function toggle(event, id) {
  playerPanels.value[id].toggle(event);
}
</script>

<style scoped>
.playerOptionsButton {
  background: transparent;
  color: rgba(255, 255, 255, 0.87) !important;
  padding: 0 !important;
  margin: 0.5rem 0 0.5rem 0.5rem;
  width: min-content !important;
}
.playerOptionsButton:hover {
  background: transparent !important;
}
.playerOptionsButton:focus {
  background: transparent !important;
}
</style>
