<template>
  <div
    v-if="props.card"
    ref="districtCard"
    id="draggable-container"
    :style="{
      'outline-color': primaryColor,
      left: order * 6 + 'rem',
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
      <img class="district-img" :src="imageSource" />
      <div
        v-if="props.card"
        class="district-text"
        :style="{
          background: secondaryColor,
        }"
      >
        {{ props.card.name }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";

const districtCard = ref();
const positions = ref({
  clientX: undefined,
  clientY: undefined,
  movementX: 0,
  movementY: 0,
});
const width = ref(window.innerWidth);
const height = ref(window.innerHeight);

const props = defineProps({
  card: Object,
  order: Number,
});

const imageSource = computed(
  () => `${window.location.origin}/src/assets/districts/${props.card?.id}.jpg`
);
const primaryColor = computed(() => {
  switch (props.card.type) {
    case "NOBLE":
      return "#F6E012";
    case "RELIGIOUS":
      return "#40AFD5";
    case "TRADE":
      return "#92BF48";
    case "MILITARY":
      return "#E24549";
  }
  return "#C05EA1";
});

const secondaryColor = computed(() => {
  switch (props.card.type) {
    case "NOBLE":
      return "#44381F";
    case "RELIGIOUS":
      return "#020B2B";
    case "TRADE":
      return "#002F0D";
    case "MILITARY":
      return "#23040B";
  }
  return "#0D0236";
});

function dragMouseDown(event) {
  event.preventDefault();
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
    districtCard.value.offsetTop - positions.value.movementY + "px";
  districtCard.value.style.left =
    districtCard.value.offsetLeft - positions.value.movementX + "px";
}

function closeDragElement() {
  document.onmouseup = null;
  document.onmousemove = null;
}
</script>

<style>
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
  opacity: 0.9;
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
  opacity: 0.9;
}
#draggable-container {
  position: absolute;
  z-index: 9;
  width: 10rem;
  outline: thick solid;
  border-radius: 4px;
  top: 0;
}
#draggable-header {
  z-index: 10;
}
</style>
