<template>
  <Panel v-for="lobby in props.lobbies" :key="lobby.name" class="mb-2">
    <template #header>
      <span class="text-4xl">
        {{ lobby.name }}
        <i v-if="lobby.secured" class="fa fa-lock"></i>
      </span>
      <div class="ml-auto text-4xl">
        {{ lobby.members.length }}/{{ lobby.maxMembers }}
      </div>
    </template>
    <div class="grid grid-cols-2">
      <div>
        játékosok:
        <Chip v-for="player in lobby.members" :key="player.id" class="mr-1">
          <i v-if="player.id === lobby.owner" class="fa fa-star mr-1" />
          {{ player.displayName }}
        </Chip>
      </div>
      <div class="ml-auto w-auto">
        <Button
          v-if="props.type === 'search'"
          label="csatlakozás"
          icon="pi pi-user-plus"
          @click="joinLobby(lobby.code)"
        ></Button>
        <Button
          v-if="props.type === 'mine'"
          label="megnyitás"
          icon="pi pi-play"
          class="mr-2"
          @click="router.push('lobby/'.concat(lobby.code))"
        ></Button>
        <Button
          v-if="props.type === 'mine'"
          label="kilépés"
          icon="pi pi-sign-out"
          class="p-button-danger"
        ></Button>
      </div>
    </div>
  </Panel>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";
import Button from "primevue/button";
import Chip from "primevue/chip";
import Panel from "primevue/panel";

const props = defineProps({
  lobbies: Array,
  type: String,
});

const router = useRouter();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();

async function joinLobby(code) {
  stateStore.setLoading(true);
  lobbyStore.joinLobby(code);
  stateStore.setLoading(false);
}
</script>

<style scoped></style>
