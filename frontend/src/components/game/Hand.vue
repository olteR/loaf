<template>
  <div
    class="hand"
    :style="{
      width: cardsWidth + 10 + 'rem',
    }"
  >
    <button
      v-if="page !== 0"
      class="absolute top-0 bottom-0 my-auto text-4xl w-20"
      @click="page--"
    >
      <i class="fa fa-chevron-left"></i>
    </button>
    <div
      :style="{
        position: 'absolute',
        left: 0,
        right: 0,
        'margin-left': 'auto',
        'margin-right': 'auto',
        width: cardsWidth + 'rem',
        height: '16.18rem',
      }"
    >
      <DistrictCardNew
        v-for="(card, i) in displayedCards"
        :card="card"
        :order="i"
      ></DistrictCardNew>
    </div>
    <button
      v-if="page !== Math.floor(cards.length / 10) && cards.length > 10"
      class="absolute right-0 top-0 bottom-0 my-auto text-4xl w-20"
      @click="page++"
    >
      <i class="fa fa-chevron-right"></i>
    </button>
  </div>
</template>

<script setup>
import DistrictCardNew from "@/components/game/DistrictCard.vue";
import { computed, ref } from "vue";

const props = defineProps({
  cards: Array,
});

const page = ref(0);

const displayedCards = computed(() => {
  return props.cards.slice(page.value * 10, (page.value + 1) * 10);
});
const cardsWidth = computed(() => {
  return (
    10 * displayedCards.value.length - 3 * (displayedCards.value.length - 1)
  );
});
</script>

<style scoped>
.hand {
  position: absolute;
  height: 20rem;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  bottom: 0;
}
</style>
