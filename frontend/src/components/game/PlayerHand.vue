<template>
  <div v-if="cards">
    <Transition name="fade">
      <div v-if="isDragging" class="card-dropper">
        <i class="fa fa-hammer text-9xl mt-12"></i>
        <p class="text-5xl mt-12">Kerület építése</p>
      </div>
    </Transition>
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
        <DistrictCard
          v-for="(card, i) in cards"
          :card="card"
          :order="displayedCards.indexOf(card)"
          :image="cardImages[i]"
          :key="i"
          :hidden="!displayedCards.includes(card)"
          @drag-begin="isDragging = true"
          @drag-end="isDragging = false"
        ></DistrictCard>
      </div>
      <button
        v-if="page !== Math.floor(cards.length / 10) && cards.length > 10"
        class="absolute right-0 top-0 bottom-0 my-auto text-4xl w-20"
        @click="page++"
      >
        <i class="fa fa-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import DistrictCard from "@/components/game/DistrictCard.vue";
import { computed, ref } from "vue";

const props = defineProps({
  cards: Array,
  cardImages: Array,
});

const page = ref(0);
const isDragging = ref(false);

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
  height: 18rem;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  bottom: 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.card-dropper {
  position: absolute;
  height: 20rem;
  width: 32.36rem;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  bottom: 24rem;
  border: solid #9fa8da;
  border-radius: 8px;
  color: #9fa8da;
  text-align: center;
}
</style>
