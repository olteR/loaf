<template>
  <div
    v-if="character"
    class="list-card"
    :style="{
      'outline-color': primaryColor,
      'background-image': 'url(' + imageSource + ')',
    }"
  >
    <div
      class="district-cost"
      :style="{
        background: secondaryColor,
        'outline-color': primaryColor,
      }"
    >
      {{ character.number }}
    </div>
    <div
      class="card-content"
      :style="{
        'background-color': secondaryColor,
      }"
    >
      {{ character.name }}
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { COLORS } from "@/utils/const";

const props = defineProps({
  character: Object,
});

const imageSource = computed(
  () =>
    `${window.location.origin}/src/assets/characters/${props.character?.id}.jpg`
);
const primaryColor = computed(() => {
  return (
    COLORS[props.character.districtTypeBonus]?.PRIMARY || COLORS.DEFAULT.PRIMARY
  );
});

const secondaryColor = computed(() => {
  return (
    COLORS[props.character.districtTypeBonus]?.SECONDARY ||
    COLORS.DEFAULT.SECONDARY
  );
});
</script>

<style scoped>
.list-card {
  width: 100%;
  outline: solid thick;
  border-radius: 4px;
  margin-bottom: 1rem;
  margin-left: 1.25rem;
  background-position: center;
  height: 2rem;
  display: inline-flex;
  align-items: center;
}
.card-content {
  width: 100%;
  height: 100%;
  opacity: 0.9;
  padding-left: 1rem;
  font-weight: bold;
  align-items: center;
}
.district-cost {
  z-index: 100;
  margin-left: -1.25rem;
  width: 2.5rem;
  height: 2.5rem;
  outline: thick solid;
  border-radius: 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 2.5rem;
  font-size: 1.5rem;
  font-weight: 700;
  user-select: none;
  opacity: 0.9;
}
</style>
