<template>
  <div class="hand">
    <div
      :style="{
        position: 'absolute',
        left: 0,
        right: 0,
        'margin-left': 'auto',
        'margin-right': 'auto',
        width: 10 * cards.length - 3 * (cards.length - 1) + 'rem',
        height: '16.18rem',
      }"
    >
      <DistrictCardNew
        v-for="(card, i) in cards"
        :card="card"
        :order="i"
      ></DistrictCardNew>
    </div>
  </div>
</template>

<script setup>
import DistrictCardNew from "@/components/game/DistrictCard.vue";

const props = defineProps({
  cards: Array,
});
</script>

<style scoped>
.hand {
  position: absolute;
  width: 100%;
  height: 20rem;
  bottom: 0;
}
</style>
