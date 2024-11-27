<template>
  <div
    class="card-div"
    :class="{ hoverable: !selected }"
    :style="{ 'outline-color': selected ? '#9fa8da' : 'inherit' }"
  >
    <div
      class="font-bold text-center"
      style="height: 3vh; font-size: 1.7vh; overflow: hidden"
    >
      {{ player.name }}
    </div>
    <div
      class="inline-flex align-middle justify-evenly w-full"
      style="height: 4vh; font-size: 2.5vh"
    >
      <Tag
        class="my-auto mx-1"
        style="width: 2.5vw; height: 4vh; font-size: 1.5vh"
        v-tooltip:[tooltipPosition]="{
          value: `${player.name} kincstartalékában ${player.gold} arany van`,
          escape: false,
        }"
      >
        {{ player.gold }}
        <i class="fa fa-coins" style="margin-left: 0.2vh"></i>
      </Tag>
      <Tag
        class="my-auto mx-1"
        style="width: 2.5vw; height: 4vh; font-size: 1.5vh"
        v-tooltip:[tooltipPosition]="{
          value: `${player.name} kezében ${player.handSize} lap van`,
          escape: false,
        }"
      >
        {{ player.handSize }}
        <i class="fa fa-sheet-plastic" style="margin-left: 0.2vh"></i>
      </Tag>
      <Tag
        class="my-auto mx-1"
        style="width: 2.5vw; height: 4vh; font-size: 1.5vh"
        v-tooltip:[tooltipPosition]="{
          value: `${player.name} ${player.points} pontot gyűjtött`,
          escape: false,
        }"
      >
        {{ player.points }}
        <i class="fa fa-star" style="margin-left: 0.2vh"></i>
      </Tag>
    </div>
    <div
      class="inline-flex w-full align-middle"
      style="height: 4vh; margin-top: 1vh; font-size: 2.5vh"
    >
      <div
        v-for="condition in player.conditions"
        :key="condition.value"
        style="margin-left: 0.5vw"
      >
        <i
          :class="`fa fa-${condition.icon}`"
          v-tooltip:[tooltipPosition]="{
            value: `<p>${condition.name}</p>${condition.description}`,
            escape: false,
          }"
        ></i>
      </div>
    </div>
  </div>
</template>

<script setup>
import Tag from "primevue/tag";

const props = defineProps({
  player: Object,
  tooltipPosition: Object,
  selected: Boolean,
});
</script>

<style scoped>
.card-div {
  height: 12vh;
  outline: solid medium;
  border-radius: 4px;
}
</style>
