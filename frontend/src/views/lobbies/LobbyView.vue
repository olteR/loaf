<template>
  <div class="container mx-auto my-4" v-if="lobbyStore.getLobby">
    {{ lobbyStore.getLobby.name }}
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";

const router = useRouter();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();

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
  stateStore.setLoading(false);
});
</script>

<style scoped></style>
