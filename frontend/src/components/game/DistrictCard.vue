<template>
  <Dialog
    v-if="props.card"
    :style="{
      width: '10rem',
      outline: 'thick solid',
      'outline-color': primaryColor,
    }"
    :visible="true"
    :closable="false"
  >
    <template #header>
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
    </template>
  </Dialog>
</template>

<script setup>
import Dialog from "primevue/dialog";
import { computed } from "vue";

const props = defineProps({
  card: Object,
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
</script>

<style>
.p-dialog .p-dialog-header {
  padding: 0 !important;
}

.p-dialog .p-dialog-content {
  padding: 0 !important;
}
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
</style>
