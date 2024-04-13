<template>
  <div
    v-if="props.character"
    :class="{ 'character-div': selectable }"
    ref="characterDiv"
  >
    <ConfirmPopup></ConfirmPopup>
    <div>
      <img
        class="character-image"
        :style="{
          width: totalCharacters === 8 ? '8rem' : '7rem',
          'outline-color': primaryColor,
        }"
        :src="props.image.src"
      />
    </div>
    <div
      class="character-color"
      :style="{
        background: characterColor,
        width: totalCharacters === 8 ? '8rem' : '7rem',
        height: totalCharacters === 8 ? '8rem' : '7rem',
      }"
      v-on:click="openPopup()"
    >
      <div class="character-icon">
        <i v-if="props.status === 'DISCARDED'" class="fa fa-xmark" />
        <i v-else-if="props.status === 'UNAVAILABLE'" class="fa fa-question" />
        <i
          v-else-if="props.status === 'SELECTED'"
          class="fa fa-check"
          :style="{ color: secondaryColor }"
        />
        <i v-else-if="props.status === 'SKIPPED'" class="fa fa-forward" />
      </div>
    </div>
    <div
      class="character-number"
      :style="{
        background: secondaryColor,
        'outline-color': primaryColor,
      }"
    >
      {{ props.character.number }}
    </div>
    <div
      class="character-text"
      :style="{
        background: secondaryColor,
        'outline-color': primaryColor,
        width: totalCharacters === 8 ? '8rem' : '7rem',
      }"
    >
      {{ props.character.name }}
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { COLORS } from "@/utils/const";
import ConfirmPopup from "primevue/confirmpopup";

const confirm = useConfirm();
const props = defineProps({
  character: Object,
  image: Object,
  totalCharacters: Number,
  status: String,
});
const emit = defineEmits(["select"]);
const characterDiv = ref();

const selectable = computed(() => {
  return !props.discarded && !props.unavailable;
});

const primaryColor = computed(() => {
  return props.status === "DISCARDED"
    ? COLORS["DISABLED"].PRIMARY
    : COLORS[props.character.districtTypeBonus]?.PRIMARY || "#9FA8DA";
});

const secondaryColor = computed(() => {
  return props.status === "DISCARDED"
    ? COLORS["DISABLED"].SECONDARY
    : COLORS[props.character.districtTypeBonus]?.SECONDARY || "#121212";
});

const characterColor = computed(() => {
  switch (props.status) {
    case "DISCARDED":
      return "#121212";
    case "UNAVAILABLE":
    case "SKIPPED":
      return COLORS[props.character.districtTypeBonus]?.SECONDARY || "#121212";
    case "SELECTED":
      return COLORS[props.character.districtTypeBonus]?.PRIMARY || "#9FA8DA";
    default:
      return "transparent";
  }
});

const openPopup = () => {
  if (selectable.value) {
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
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  outline: thick solid;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}

.character-number {
  position: relative;
  z-index: 100;
  top: -10rem;
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
  -webkit-user-select: none;
  -moz-user-select: none;
}

.character-text {
  position: relative;
  z-index: 100;
  top: -3rem;
  left: 50%;
  transform: translateX(-50%);
  height: 2.5rem;
  outline: thick solid;
  border-radius: 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 2.5rem;
  font-size: 1.25rem;
  font-weight: 700;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}
.character-color {
  position: absolute;
  top: 0;
  opacity: 0.5;
  border-radius: 50%;
  margin-left: 0.5rem;
}
.character-icon {
  position: relative;
  z-index: 101;
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  text-align: center;
  font-size: 6rem;
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
