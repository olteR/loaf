<template>
  <div v-if="props.character">
    <img
      class="character-image"
      :style="{
        width: totalCharacters === 8 ? '8rem' : '7rem',
        'outline-color': primaryColor,
      }"
      :src="props.image.src"
    />
    <div
      class="character-number"
      :style="{
        background: secondaryColor,
        'outline-color': primaryColor,
      }"
    >
      {{ props.character.number }}
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { COLORS } from "@/utils/const";

const props = defineProps({
  character: Object,
  image: Object,
  totalCharacters: Number,
});

const primaryColor = computed(() => {
  return COLORS[props.character.districtTypeBonus]?.PRIMARY || "#9FA8DA";
});

const secondaryColor = computed(() => {
  return COLORS[props.character.districtTypeBonus]?.SECONDARY || "#121212";
});
</script>

<style scoped>
.character-image {
  border-radius: 50%;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  outline: thick solid;
}

.character-number {
  position: relative;
  z-index: 100;
  bottom: 0.5rem;
  left: 50%;
  transform: translateX(-50%);
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
}
</style>
