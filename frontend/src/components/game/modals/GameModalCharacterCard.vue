<template>
  <div
    v-if="character"
    class="list-card"
    :class="{ hoverable: clickable }"
    :style="{
      'outline-color': primaryColor,
      'background-image': 'url(' + imageSource + ')',
      cursor: clickable ? 'pointer' : 'default',
    }"
    @click="select"
  >
    <div
      class="character-number"
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
        'background-color': selected ? 'transparent' : secondaryColor,
      }"
    >
      <div class="status-icon" v-if="unavailable">
        <i
          class="fa fa-question"
          v-tooltip:[tooltipPosition(character.number)]="{
            value: 'Ez a karakter el lett dobva vagy már választva lett.',
            escape: false,
          }"
        ></i>
      </div>
      <div class="status-icon" v-if="discarded">
        <i
          class="fa fa-x"
          v-tooltip:[tooltipPosition(character.number)]="{
            value: 'Ez a karakter el lett dobva.',
            escape: false,
          }"
        ></i>
      </div>
      <div
        class="card-name"
        :style="{
          background: secondaryColor,
        }"
        v-tooltip:[tooltipPosition(character.number)]="{
          value: composeCharacterDescription(character),
          escape: false,
        }"
      >
        {{ character.name }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { COLORS } from "@/utils/const";
import { composeCharacterDescription } from "@/utils/utils";

const emit = defineEmits(["select"]);
const props = defineProps({
  character: Object,
  selected: Boolean,
  unavailable: Boolean,
  discarded: Boolean,
});

const imageSource = computed(
  () =>
    `${window.location.origin}/src/assets/characters/${props.character?.id}.jpg`
);
const primaryColor = computed(() => {
  if (!clickable.value) {
    return "#121212";
  }
  return (
    COLORS[props.character.districtTypeBonus]?.PRIMARY ?? COLORS.DEFAULT.PRIMARY
  );
});

const secondaryColor = computed(() => {
  if (!clickable.value) {
    return "#121212";
  }
  return (
    COLORS[props.character.districtTypeBonus]?.SECONDARY ??
    COLORS.DEFAULT.SECONDARY
  );
});

const clickable = computed(() => {
  return !props.unavailable && !props.discarded;
});

function tooltipPosition(characterNumber) {
  if ([1, 2, 5, 6].includes(characterNumber)) {
    return { position: "right" };
  }
  return { position: "left" };
}

function select() {
  if (clickable.value) {
    emit("select");
  }
}
</script>

<style scoped>
.list-card {
  width: 140px;
  outline: solid thick;
  border-radius: 4px;
  margin-bottom: 1.25rem;
  margin-left: 1.25rem;
  background-position: center;
  background-size: cover;
  height: 210px;
  display: inline-flex;
  align-items: center;
  background-blend-mode: overlay;
}
.card-content {
  width: 100%;
  height: 100%;
  opacity: 0.8;
  font-weight: bold;
  margin-left: -1.25rem;
}
.card-name {
  user-select: none;
  position: relative;
  top: 100%;
  width: 100%;
  opacity: 0.8;
  transform: translateY(-100%);
  text-align: center;
  font-weight: 700;
}
.card-info {
  position: relative;
  text-align: end;
  margin-right: 0.25rem;
  top: 0;
  transform: translateY(-100%);
}
.character-number {
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
  position: relative;
  top: -90px;
}
.status-icon {
  position: absolute;
  width: 140px;
  text-align: center;
  font-size: 7rem;
}
</style>
