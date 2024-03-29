<template>
  <Card class="w-48 text-2xl m-4">
    <template #content>
      <div class="columns-2 text-center">
        <div><i class="fa fa-coins"></i> 4</div>
        <div><i class="fa fa-star"></i> 0</div>
      </div>
    </template>
  </Card>
  <MemberList :members="gameStore.getGameState?.players"></MemberList>
  <PlayerHand
    :cards="cardsInHand"
    :card-images="cardStore.getDistrictImages"
  ></PlayerHand>
</template>

<script setup>
import { computed, onMounted } from "vue";
import router from "@/router";
import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import Card from "primevue/card";
import MemberList from "@/components/game/MemberList.vue";
import PlayerHand from "@/components/game/PlayerHand.vue";
import { useGameStore } from "@/stores/games";

const stateStore = useStateStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const lobbyCode = router.currentRoute.value.params.code;
const cardsInHand = computed(() => {
  let hand = [];
  gameStore.getGameState?.hand.forEach((card) => {
    hand.push(
      cardStore.getCards.districts.find((district) => district.id === card)
    );
  });
  return hand;
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await gameStore.fetchGameState(lobbyCode);
  stateStore.setLoading(false);
});
</script>

<style scoped>
.p-card {
  border: 1px solid rgba(255, 255, 255, 0.12);
}
</style>
