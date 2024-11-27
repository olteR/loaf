<template>
  <div
    v-if="character"
    class="list-card small-character"
    :style="{
      'outline-color': primaryColor(character.type),
      'background-image': 'url(' + imageSource + ')',
      'background-position-y': CARD_POS[character.id],
    }"
  >
    <div
      class="character-number"
      :style="{
        background: secondaryColor(character.type),
        'outline-color': primaryColor(character.type),
      }"
    >
      <span style="font-size: 1rem" v-if="statusByOne && statusByTwo">
        <i
          class="mr-1"
          :class="`fa fa-${resolvedOneStatus.icon}`"
          v-tooltip="resolvedOneStatus.description"
        ></i>
        <i
          :class="`fa fa-${resolvedTwoStatus.icon}`"
          v-tooltip="resolvedTwoStatus.description"
        ></i>
      </span>
      <i v-else-if="statusByOne">
        <i
          :class="`fa fa-${resolvedOneStatus.icon}`"
          v-tooltip="resolvedOneStatus.description"
        ></i>
      </i>
      <i v-else-if="statusByTwo">
        <i
          :class="`fa fa-${resolvedTwoStatus.icon}`"
          v-tooltip="resolvedTwoStatus.description"
        ></i>
      </i>
      <span v-else>
        {{ character.number }}
      </span>
    </div>
    <div
      class="character-status"
      :style="{
        background: secondaryColor(character.type),
        'outline-color': primaryColor(character.type),
      }"
    >
      <i
        :class="`fa fa-${resolvedStatus.icon}`"
        v-tooltip="resolvedStatus.description"
      ></i>
    </div>
    <div
      class="card-content"
      :style="{
        'background-color': secondaryColor(character.type),
      }"
    >
      <div
        class="mt-3 select-none"
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
import { CARD_POS, CHAR_STATUS } from "@/utils/const";
import {
  composeCharacterDescription,
  primaryColor,
  secondaryColor,
} from "@/utils/utils";

const props = defineProps({
  character: Object,
  status: String,
  statusByOne: String,
  statusByTwo: String,
});

const imageSource = computed(
  () =>
    `${window.location.origin}/src/assets/characters/${props.character?.id}.jpg`
);

const resolvedStatus = computed(() => {
  if (props.status === CHAR_STATUS.SELECTED) {
    return {
      icon: "check",
      description: "Ezt a karaktert kiválasztottad.",
    };
  }
  if (props.status === CHAR_STATUS.DISCARDED) {
    return { icon: "x", description: "Ez a karakter eldobódott." };
  }
  if (props.status === CHAR_STATUS.SKIPPED) {
    return {
      icon: "forward",
      description: "Ezt a karaktert nem választottad.",
    };
  }
  return {
    icon: "question",
    description:
      "Ez a karakter eldobódott vagy egy másik játékos már választotta.",
  };
});

const resolvedOneStatus = computed(() => {
  if (props.statusByOne === CHAR_STATUS.KILLED) {
    return {
      icon: "skull",
      description: "Ezt a karaktert az orgyilkos megölte.",
    };
  }
  if (props.statusByOne === CHAR_STATUS.BEWITCHED) {
    return {
      icon: "wand-sparkles",
      description: "Ezt a karaktert a boszorkány megbabonázta.",
    };
  }
  if (props.statusByOne === CHAR_STATUS.WARRANTED_SIGNED) {
    return {
      icon: "file-signature",
      description: "Ennek a karaktert a magisztrátus aláírt parancsot adott.",
    };
  }
  if (props.statusByOne === CHAR_STATUS.WARRANTED) {
    return {
      icon: "scroll",
      description: "Ennek a karaktert a magisztrátus parancsot adott.",
    };
  }
  return null;
});

const resolvedTwoStatus = computed(() => {
  if (props.statusByTwo === CHAR_STATUS.ROBBED) {
    return {
      icon: "sack-dollar",
      description: "Ezt a karaktert a tolvaj kirabolta.",
    };
  }
  if (props.statusByTwo === CHAR_STATUS.THREATENED_REAL) {
    return {
      icon: "envelope-open-text",
      description: "Ezt a karaktert a zsaroló megzsarolta.",
    };
  }
  if (props.statusByTwo === CHAR_STATUS.THREATENED) {
    return {
      icon: "envelope",
      description: "Ezt a karaktert a zsaroló megfenyegette.",
    };
  }
  return null;
});
</script>

<style scoped>
.list-card {
  width: 100%;
  height: 6vh;
  outline: solid thick;
  border-radius: 4px;
  margin-bottom: 1.5vh;
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
  width: 6vh;
  height: 6vh;
  outline: thick solid;
  border-radius: 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 6vh;
  font-size: 1.5rem;
  font-weight: 700;
  user-select: none;
}

.character-status {
  position: absolute;
  right: -3.25rem;
  z-index: 100;
  width: 6vh;
  height: 6vh;
  outline: thick solid;
  border-radius: 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 6vh;
  font-size: 1.5rem;
  font-weight: 700;
  user-select: none;
}
</style>
