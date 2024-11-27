<template>
  <Card class="container mx-auto my-4">
    <template #title>
      <h1 class="text-5xl text-center">A játéknak vége!</h1>
    </template>
    <template #content>
      <div v-for="player in sortedPlayers">
        {{ `${player.name}: ${player.points}` }}
      </div>
    </template>
  </Card>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { GAME_PHASE } from "@/utils/const";
import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import { useGameStore } from "@/stores/games";
import { useRouter } from "vue-router";
import Card from "primevue/card";

const stateStore = useStateStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const router = useRouter();
const lobbyCode = router.currentRoute.value.params.code;

const sortedPlayers = computed(() => {
  let players = gameStore.getGame?.players ?? [];
  return players.sort((a, b) => b.points - a.points) ?? [];
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await gameStore.fetchGame(lobbyCode);
  if (gameStore.getGame.phase === GAME_PHASE.NOT_STARTED) {
    await router.push(`/lobby/${lobbyCode}`);
  } else if (gameStore.getGame.phase === GAME_PHASE.ENDED) {
    await router.push(`/game-results/${lobbyCode}`);
  }
  stateStore.setLoading(false);
});
</script>

<style scoped></style>
