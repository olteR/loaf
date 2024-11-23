<template>
  <div
    v-if="character"
    class="list-card"
    :class="{ hoverable: clickable }"
    :style="{
      'outline-color': getPrimaryColor,
      'background-image': 'url(' + props.image.src + ')',
    }"
    @click="select"
  >
    <div
      class="character-number"
      :style="{
        background: getSecondaryColor,
        'outline-color': getPrimaryColor,
      }"
    >
      {{ character.number }}
    </div>
    <div
      class="card-content"
      :style="{
        'background-color': selected ? 'transparent' : getSecondaryColor,
      }"
    >
      <div class="status-icon" v-if="discarded">
        <i
          class="fa fa-x"
          v-tooltip:[tooltipPosition(character.number)]="{
            value: 'Ez a karakter el lett dobva',
            escape: false,
          }"
        ></i>
      </div>
      <div class="status-icon" v-else-if="unavailable">
        <i
          class="fa fa-question"
          v-tooltip:[tooltipPosition(character.number)]="{
            value: 'Ez a karakter el lett dobva vagy már választva lett',
            escape: false,
          }"
        ></i>
      </div>
      <div class="status-icon" v-else-if="untargetable">
        <i
          class="fa fa-circle-exclamation"
          v-tooltip:[tooltipPosition(character.number)]="{
            value: 'Ez a karakter nem célozható',
            escape: false,
          }"
        ></i>
      </div>
      <div
        class="card-name"
        :style="{
          background: getSecondaryColor,
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
import {
  composeCharacterDescription,
  primaryColor,
  secondaryColor,
} from "@/utils/utils";

const emit = defineEmits(["select"]);
const props = defineProps({
  character: Object,
  image: Object,
  selected: Boolean,
  unavailable: Boolean,
  discarded: Boolean,
  untargetable: Boolean,
});

const getPrimaryColor = computed(() => {
  return clickable.value ? primaryColor(props.character.type) : "#121212";
});

const getSecondaryColor = computed(() => {
  return clickable.value ? secondaryColor(props.character.type) : "#121212";
});

const clickable = computed(() => {
  return !props.unavailable && !props.discarded && !props.untargetable;
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
