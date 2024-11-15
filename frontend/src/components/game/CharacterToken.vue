<template>
  <div
    v-if="props.character"
    :class="{ 'character-div': props.status === CHAR_STATUS.SELECTABLE }"
    ref="characterDiv"
  >
    <ConfirmPopup></ConfirmPopup>
    <div>
      <div
        class="character-number"
        :style="{
          background: secondaryColor,
          'outline-color': primaryColor,
        }"
      >
        {{ props.character.number }}
      </div>
      <img
        class="character-image"
        :style="{
          width: totalCharacters === 8 ? '7vw' : '6vw',
          'outline-color': primaryColor,
        }"
        :src="props.image.src"
      />
    </div>
    <div
      class="character-color"
      :style="{
        background: characterColor,
        width: totalCharacters === 8 ? '7vw' : '6vw',
        height: totalCharacters === 8 ? '7vw' : '6vw',
      }"
      v-on:click="openPopup()"
    >
      <div class="character-icon">
        <i v-if="props.status === CHAR_STATUS.DISCARDED" class="fa fa-xmark" />
        <i
          v-else-if="props.status === CHAR_STATUS.UNAVAILABLE"
          class="fa fa-question"
        />
        <i
          v-else-if="props.status === CHAR_STATUS.SELECTED"
          class="fa fa-check"
          :style="{ color: secondaryColor }"
        />
        <i v-else-if="props.status === 'SKIPPED'" class="fa fa-forward" />
      </div>
    </div>
    <div
      class="character-text"
      :style="{
        background: secondaryColor,
        'outline-color': primaryColor,
        width: totalCharacters === 8 ? '7vw' : '6vw',
      }"
    >
      {{ props.character.name }}
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { CHAR_STATUS, COLORS } from "@/utils/const";
import ConfirmPopup from "primevue/confirmpopup";

const confirm = useConfirm();

const emit = defineEmits(["select"]);
const props = defineProps({
  character: Object,
  image: Object,
  totalCharacters: Number,
  status: String,
  canSelect: Boolean,
});

const characterDiv = ref();

const primaryColor = computed(() => {
  return props.status === CHAR_STATUS.DISCARDED
    ? COLORS.DISABLED.PRIMARY
    : COLORS[props.character.districtTypeBonus]?.PRIMARY || "#9FA8DA";
});

const secondaryColor = computed(() => {
  return props.status === CHAR_STATUS.DISCARDED
    ? COLORS.DISABLED.SECONDARY
    : COLORS[props.character.districtTypeBonus]?.SECONDARY || "#121212";
});

const characterColor = computed(() => {
  switch (props.status) {
    case CHAR_STATUS.DISCARDED:
      return "#121212";
    case CHAR_STATUS.UNAVAILABLE:
    case CHAR_STATUS.SKIPPED:
      return COLORS[props.character.districtTypeBonus]?.SECONDARY || "#121212";
    case CHAR_STATUS.SELECTED:
      return COLORS[props.character.districtTypeBonus]?.PRIMARY || "#9FA8DA";
    default:
      return "transparent";
  }
});

const openPopup = () => {
  if (props.status === CHAR_STATUS.SELECTABLE && props.canSelect) {
    confirm.require({
      target: characterDiv.value,
      message: `Kiválasztod a(z) ${props.character.name} karaktert?`,
      header: "Karakter választás",
      acceptLabel: "Igen",
      rejectLabel: "Mégsem",
      accept: () => {
        emit("select", props.character.number);
      },
    });
  }
};
</script>

<style scoped>
.character-image {
  border-radius: 50%;
  margin-left: 0.5vw;
  margin-right: 0.5vw;
  outline: thick solid;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}

.character-number {
  position: relative;
  z-index: 10;
  left: 50%;
  transform: translateX(-50%);
  width: 4vh;
  height: 4vh;
  outline: thick solid;
  border-radius: 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 4vh;
  font-size: 2vh;
  font-weight: 700;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}

.character-text {
  position: relative;
  z-index: 11;
  top: -1vh;
  left: 50%;
  transform: translateX(-50%);
  height: 3.2vh;
  outline: thick solid;
  border-radius: 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 3.2vh;
  font-size: 1.6vh;
  font-weight: 700;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}
.character-color {
  position: absolute;
  top: 4vh;
  opacity: 0.5;
  border-radius: 50%;
  margin-left: 0.5vw;
}
.character-icon {
  position: relative;
  z-index: 12;
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  text-align: center;
  font-size: 5vw;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}
.character-div:hover {
  -webkit-transition: transform 0.2s;
  -moz-transition: transform 0.2s;
  -ms-transition: transform 0.2s;

  z-index: 11;
  --webkit-transform: scale(1.05);
  --moz-transform: scale(1.05);
  --o-transform: scale(1.05);
  --ms-transform: scale(1.05);
  transform: scale(1.05);
}
</style>
