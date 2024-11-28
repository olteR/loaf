<template>
  <div>
    <Card class="container mx-auto my-4">
      <template #title>
        <h1 class="text-5xl text-center">A játéknak vége!</h1>
      </template>
      <template #content>
        <div class="text-2xl">
          {{
            `A játék ${
              gameStore.getGameResult?.turn
            } körig tartott és végül ${gameStore.getGameResult?.players
              .filter((player) => player.placement === 1)
              .map((player) => player.name)
              .join(", ")} nyert!`
          }}
        </div>
        <PlayerResult
          v-for="player in sortedPlayers"
          :key="player.id"
          :player="player"
        />
      </template>
    </Card>
    <Card class="container mx-auto my-4"
      ><template #title>
        <h1 class="text-3xl text-center">A játékban használt beállítások</h1>
      </template>
      <template #content>
        <div class="grid grid-cols-2 gap-x-8">
          <div>
            <div class="w-full text-center">
              <div class="text-2xl mb-4">Karakterek</div>
            </div>
            <div class="margin-offset">
              <div
                v-for="character in gameStore.getGameResult?.characters"
                :key="character"
              >
                <GameSettingCharacterCard
                  :character="character"
                  selected
                  is-big
                />
              </div>
            </div>
          </div>
          <div>
            <div class="w-full text-center">
              <div class="text-2xl mb-4">Egyedi kerületek</div>
            </div>
            <div class="margin-offset">
              <div
                v-for="district in gameStore.getGameResult?.uniqueDistricts"
                :key="district"
              >
                <GameSettingDistrictCard
                  :district="
                    cardStore.getCards.districts?.find((d) => d.id === district)
                  "
                  :selected="true"
                />
              </div>
            </div>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { GAME_PHASE } from "@/utils/const";
import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import { useGameStore } from "@/stores/games";
import { useRouter } from "vue-router";
import Card from "primevue/card";
import PlayerResult from "@/components/game/PlayerResult.vue";
import GameSettingCharacterCard from "@/components/lobbies/GameSettingCharacterCard.vue";
import GameSettingDistrictCard from "@/components/lobbies/GameSettingDistrictCard.vue";

const stateStore = useStateStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const router = useRouter();
const lobbyCode = router.currentRoute.value.params.code;

const sortedPlayers = computed(() => {
  let players = gameStore.getGameResult?.players ?? [];
  return players.sort((a, b) => a.placement - b.placement) ?? [];
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await gameStore.fetchGame(lobbyCode);
  if (gameStore.getGame.phase === GAME_PHASE.NOT_STARTED) {
    await router.push(`/lobby/${lobbyCode}`);
  } else if (gameStore.getGame.phase !== GAME_PHASE.ENDED) {
    await router.push(`/game/${lobbyCode}`);
  } else {
    await gameStore.fetchGameResult(lobbyCode);
  }
  stateStore.setLoading(false);
});
</script>

<style scoped>
.margin-offset {
  margin-right: 1.25rem;
}
</style>
