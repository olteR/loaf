<template>
  <div
    v-if="character"
    class="list-card"
    :class="{
      'big-character': isBig,
      'small-character': !isBig,
      hoverable: !selected,
    }"
    :style="{
      'outline-color': primaryColorOrDisabled(character.type, selected),
      'background-image': 'url(' + imageSource + ')',
      'background-position-y': CARD_POS[character.id],
    }"
  >
    <div
      class="character-number"
      :style="{
        background: secondaryColorOrDisabled(character.type, selected),
        'outline-color': primaryColorOrDisabled(character.type, selected),
      }"
    >
      {{ character.number }}
    </div>
    <div
      class="card-content"
      :style="{
        'background-color': secondaryColorOrDisabled(character.type, selected),
      }"
    >
      <div
        class="mt-3 select-none"
        :class="{ 'text-4xl': isBig }"
        v-tooltip="{
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
import { CARD_POS } from "@/utils/const";
import {
  composeCharacterDescription,
  primaryColorOrDisabled,
  secondaryColorOrDisabled,
} from "@/utils/utils";

const props = defineProps({
  character: Object,
  selected: Boolean,
  isBig: Boolean,
});

const imageSource = computed(
  () =>
    `${window.location.origin}/src/assets/characters/${props.character?.id}.jpg`
);
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
  display: inline-flex;
  align-items: center;
  background-blend-mode: overlay;
}
.big-character {
  height: 4.5rem;
}
.small-character {
  height: 2.5rem;
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
}
</style>
