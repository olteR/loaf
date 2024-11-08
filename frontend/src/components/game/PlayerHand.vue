<template>
  <div v-if="cards">
    <Transition name="fade">
      <div v-if="isDragging && canBuild" class="card-dropper" ref="cardDropper">
        <div>
          <i class="fa fa-hammer" style="font-size: min(4vw, 64px)"></i>
          <p class="mt-2" style="font-size: min(2vw, 24px)">Kerület építése</p>
        </div>
      </div>
    </Transition>
    <div
      class="hand"
      :style="{
        width: cardsWidth + 96 + 'px',
      }"
    >
      <button
        v-if="page !== 0"
        class="absolute top-0 bottom-0 my-auto text-4xl"
        style="width: 48px"
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
          width: cardsWidth + 'px',
        }"
      >
        <DistrictCard
          v-for="(card, i) in displayedCards"
          :card="card"
          :order="i"
          :image="cardImages[card.id - 1]"
          :key="i"
          :hidden="!displayedCards.includes(card)"
          @drag-begin="isDragging = true"
          @drag-end="(c) => onDragEnd(c, card, i)"
        ></DistrictCard>
      </div>
      <button
        v-if="page !== Math.floor(cards.length / 10) && cards.length > 10"
        class="absolute right-0 top-0 bottom-0 my-auto text-4xl"
        style="width: 48px"
        @click="page++"
      >
        <i class="fa fa-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import DistrictCard from "@/components/game/DistrictCard.vue";
import { computed, onMounted, ref } from "vue";

const emit = defineEmits(["build"]);

const props = defineProps({
  cards: Array,
  cardImages: Array,
  canBuild: Boolean,
});

const cardDropper = ref();
const page = ref(0);
const isDragging = ref(false);
const vw = ref(window.innerWidth * 0.01);
const vh = ref(window.innerHeight * 0.01);

const displayedCards = computed(() => {
  return props.cards.slice(page.value * 10, (page.value + 1) * 10);
});
const cardsWidth = computed(() => {
  return 20 * vh.value + 4 * (displayedCards.value.length - 1) * vw.value;
});

onMounted(() => {
  window.addEventListener("resize", () => {
    vw.value = window.innerWidth * 0.01;
    vh.value = window.innerHeight * 0.01;
  });
});

function onDragEnd(elem, card, ind) {
  if (doElementsCollide(elem.value, cardDropper.value)) {
    emit("build", card, ind);
  }
  isDragging.value = false;
}

function doElementsCollide(elem1, elem2) {
  if (elem1 && elem2) {
    const rect1 = elem1?.getBoundingClientRect();
    const rect2 = elem2?.getBoundingClientRect();
    return !(
      rect1.right < rect2.left ||
      rect1.left > rect2.right ||
      rect1.bottom < rect2.top ||
      rect1.top > rect2.bottom
    );
  }
  return false;
}
</script>

<style scoped>
.hand {
  position: absolute;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  bottom: 0;
  height: 35vh;
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
  height: 25vh;
  width: 45vw;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  top: 45%;
  transform: translateY(-50%);
  border: solid #9fa8da;
  border-radius: 8px;
  color: #9fa8da;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
