<template>
  <div class="container mx-auto my-4">
    <Card v-if="lobbyStore.getLobbies.length === 0">
      <template #content>
        <div class="text-center select-none">
          Még nem csatlakoztál egy játékhoz sem! Keress vagy hozz létre egyet a
          lobbi keresőben.
        </div>
      </template>
    </Card>
    <Panel v-for="lobby in lobbyStore.getLobbies" :key="lobby.name">
      <template #header>
        <span>
          <i v-if="lobby.secured" class="fa fa-lock"></i>
          {{ lobby.name }}
        </span>
        <div class="ml-auto">
          {{ lobby.members.length }}/{{ lobby.maxMembers }}
        </div>
      </template>
      <div class="grid grid-cols-2">
        <div>
          játékosok:
          <span v-for="player in lobby.members" :key="player.id">
            <i v-if="player.id === lobby.owner" class="fa fa-crown" />
            {{ player.displayName }}
          </span>
        </div>
        <div class="w-full">
          <Button
            label="kilépés"
            class="float-right p-button-danger"
            icon="pi pi-sign-out"
          ></Button>
          <Button
            label="megnyitás"
            class="float-right"
            icon="pi pi-play"
          ></Button>
        </div>
      </div>
    </Panel>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useLobbyStore } from "@/stores/lobbies";
import { useStateStore } from "@/stores/state";
import Button from "primevue/button";
import Card from "primevue/card";
import Panel from "primevue/panel";

const stateStore = useStateStore();
const lobbyStore = useLobbyStore();

onMounted(async () => {
  stateStore.setLoading(true);
  await lobbyStore.fetchMyGames();
  stateStore.setLoading(false);
});
</script>

<style scoped>
.p-card {
  margin-bottom: 1rem !important;
}

.p-panel {
  margin-bottom: 0.5rem !important;
}

.p-button {
  margin-right: 1rem;
}

::v-deep(.p-panel) {
  .p-panel-header {
    font-size: 2rem;
  }
}
</style>
