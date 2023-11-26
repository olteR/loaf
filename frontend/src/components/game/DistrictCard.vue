<template>
  <div
    v-if="props.card"
    ref="districtCard"
    id="draggable-container"
    :style="{
      'outline-color': primaryColor,
      left: order * 7 + 'rem',
    }"
  >
    <div id="draggable-header" @mousedown="dragMouseDown">
      <div
        class="district-cost"
        :style="{
          background: secondaryColor,
          'outline-color': primaryColor,
        }"
      >
        {{ props.card.cost }}
      </div>
      <img class="district-img" :src="image.src" />
      <div
        v-if="props.card"
        class="district-text"
        :style="{
          background: secondaryColor,
        }"
      >
        {{ props.card.cardName }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { COLORS } from "@/utils/const";

const emit = defineEmits(["drag-begin", "drag-end"]);

const districtCard = ref();
const positions = ref({
  clientX: undefined,
  clientY: undefined,
  movementX: 0,
  movementY: 0,
});

const props = defineProps({
  card: Object,
  order: Number,
  image: Object,
});

const primaryColor = computed(() => {
  return COLORS[props.card.type].PRIMARY;
});

const secondaryColor = computed(() => {
  return COLORS[props.card.type].SECONDARY;
});

function dragMouseDown(event) {
  event.preventDefault();
  emit("drag-begin");
  delete districtCard.value.style.transition;
  positions.value.clientX = event.clientX;
  positions.value.clientY = event.clientY;
  document.onmousemove = elementDrag;
  document.onmouseup = closeDragElement;
}

function elementDrag(event) {
  event.preventDefault();
  positions.value.movementX = positions.value.clientX - event.clientX;
  positions.value.movementY = positions.value.clientY - event.clientY;
  positions.value.clientX = event.clientX;
  positions.value.clientY = event.clientY;
  districtCard.value.style.top =
    Math.min(districtCard.value.offsetTop - positions.value.movementY, 48) +
    "px";
  districtCard.value.style.left =
    districtCard.value.offsetLeft - positions.value.movementX + "px";
}

function closeDragElement() {
  emit("drag-end");
  document.onmouseup = null;
  document.onmousemove = null;
  const resetPos = districtCard.value.animate(
    { top: 0, left: props.order * 7 + "rem" },
    200
  );
  resetPos.onfinish = () => {
    districtCard.value.style.top = 0;
    districtCard.value.style.left = props.order * 7 + "rem";
  };
}
</script>

<style scoped>
.district-img {
  -moz-user-select: none;
  -webkit-user-select: none;
  user-select: none;
  pointer-events: none;
  border-radius: 4px;
}
.district-cost {
  position: absolute;
  z-index: 100;
  top: -0.3rem;
  left: -0.3rem;
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
.district-text {
  text-align: center;
  font-weight: 700;
  font-size: 1.5rem;
  margin: 0;
  user-select: none;
  position: absolute;
  bottom: 0;
  width: 100%;
  opacity: 0.8;
}
#draggable-container {
  position: absolute;
  z-index: 9;
  width: 10rem;
  outline: thick solid;
  border-radius: 4px;
  top: 0;
  transition: transform 0.2s;
}
#draggable-container:hover {
  z-index: 11;
  --webkit-transform: scale(1.05);
  --moz-transform: scale(1.05);
  --o-transform: scale(1.05);
  --ms-transform: scale(1.05);
  transform: scale(1.05);
}
#draggable-header {
  z-index: 10;
}
@keyframes resetPos {
  100% {
    top: 0;
    left: 0;
  }
}
</style>
