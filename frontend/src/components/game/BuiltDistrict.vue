<template>
  <div
    v-if="props.card"
    class="district"
    :style="{
      outline: 'thin solid',
      'outline-color': primaryColor,
    }"
  >
    <div>
      <div
        class="district-cost items-center"
        :style="{ background: secondaryColor }"
        v-tooltip="{ value: description, escape: false }"
      >
        {{ props.card.cost }}
      </div>
      <img class="district-img" :src="props.image.src" />
    </div>
  </div>
</template>

<script setup>
import { COLORS } from "@/utils/const";
import { computed } from "vue";

const props = defineProps({
  card: Object,
  image: Object,
});

const primaryColor = computed(() => {
  return COLORS[props.card.type].PRIMARY;
});

const secondaryColor = computed(() => {
  return COLORS[props.card.type].SECONDARY;
});

const description = computed(() => {
  return `<p>${props.card.name}</p>${props.card.abilities
    .map((ability) => ability.description)
    .join("")}`;
});
</script>

<style scoped>
.district {
  width: 3vh;
  height: 4.5vh;
  margin-top: 1vh;
  color: white;
  margin-left: 0.25vw;
  margin-right: 0.25vw;
  border-radius: 4px;
}
.district-cost {
  position: absolute;
  text-align: center;
  width: 3vh;
  height: 3vh;
  border-radius: 4px 4px 8px 8px;
  opacity: 0.8;
}
</style>
