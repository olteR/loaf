<template>
  <div
    v-if="character"
    class="list-card"
    :style="{
      'outline-color': primaryColor,
      'background-image': 'url(' + imageSource + ')',
      'background-position-y': CARD_POS[character.id],
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
      <div class="text-4xl mt-3 select-none">
        {{ character.name }}
      </div>
      <div class="ml-auto mr-4 my-auto">
        <i
          class="fa fa-circle-question"
          v-tooltip="{
            value: character.cardText,
            escape: false,
          }"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { COLORS, CARD_POS } from "@/utils/const";

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
  margin-bottom: 1.25rem;
  margin-left: 1.25rem;
  background-position-y: center;
  background-size: cover;
  height: 4.5rem;
  display: inline-flex;
  align-items: center;
  background-blend-mode: overlay;
}
.card-content {
  display: inline-flex;
  width: 100%;
  height: 100%;
  opacity: 0.8;
  padding-left: 2.25rem;
  font-weight: bold;
  margin-left: -1.25rem;
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
}
</style>
