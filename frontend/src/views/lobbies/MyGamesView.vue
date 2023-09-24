<template>
  <div class="container mx-auto my-4">
    <Card v-if="lobbyStore.getLobbies.length === 0" class="mb-4">
      <template #content>
        <div class="text-center select-none">
          Még nem csatlakoztál egy játékhoz sem! Keress vagy hozz létre egyet a
          lobbi keresőben.
        </div>
      </template>
    </Card>
    <LobbyList :lobbies="lobbyStore.getLobbies" :type="'mine'" />
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useLobbyStore } from "@/stores/lobbies";
import { useStateStore } from "@/stores/state";
import Card from "primevue/card";
import LobbyList from "@/components/lobbies/LobbyList.vue";

const stateStore = useStateStore();
const lobbyStore = useLobbyStore();

onMounted(async () => {
  stateStore.setLoading(true);
  await lobbyStore.fetchMyGames();
  stateStore.setLoading(false);
});
</script>

<style scoped>
</style>
