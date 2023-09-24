<template>
  <div>{{ lobbyStore.getLobby.name }}</div>
</template>

<script setup>
import { onMounted } from "vue";
import { useLobbyStore } from "@/stores/lobbies";

const lobbyStore = useLobbyStore();

onMounted(() => {});
</script>

<style scoped></style>
