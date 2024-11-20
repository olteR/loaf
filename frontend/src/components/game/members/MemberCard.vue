<template>
  <div class="card-div">
    <div class="order-number">
      <template v-if="gamePhase === GAME_PHASE.SELECTION">
        {{ order }}
      </template>
      <template v-else-if="player.character">
        <img class="character-image" :src="image.src" />
      </template>
      <template v-else>?</template>
    </div>
    <div
      class="font-bold"
      style="height: 3vh; margin-left: 4vw; font-size: 1.7vh; overflow: hidden"
    >
      {{ player.name }}
      <template v-if="isPlayer">
        <i
          class="fa fa-user ml-1"
          v-tooltip:[tooltipPosition]="{
            value: `A te karakterlapod`,
            escape: false,
          }"
        ></i>
      </template>
    </div>
    <div
      class="inline-flex align-middle justify-between"
      style="height: 4vh; margin-left: 3.5vw; font-size: 2.5vh; width: 8vw"
    >
      <Tag
        class="my-auto"
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
        class="my-auto"
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
        class="my-auto"
        style="width: 2.5vw; height: 4vh; font-size: 1.5vh"
        v-tooltip:[tooltipPosition]="{
          value: `${player.name} kerületei ${calculatePoints(
            player
          )} pontot érnek`,
          escape: false,
        }"
      >
        {{ calculatePoints(player) }}
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
    <div class="districts-outer">
      <div class="districts">
        <BuiltDistrict
          v-for="(card, j) in player.districts.slice(0, 5)"
          :key="j"
          :card="card"
          :image="districtImages[card.id - 1]"
          :tooltip-position="tooltipPosition"
        ></BuiltDistrict>
      </div>
      <div class="districts">
        <BuiltDistrict
          v-for="(card, j) in player.districts.slice(5, 10)"
          :key="j"
          :card="card"
          :image="districtImages[card.id - 1]"
          :tooltip-position="tooltipPosition"
        ></BuiltDistrict>
      </div>
    </div>
  </div>
</template>

<script setup>
import BuiltDistrict from "@/components/game/members/BuiltDistrict.vue";
import Tag from "primevue/tag";
import { GAME_PHASE } from "@/utils/const";

const props = defineProps({
  player: Object,
  order: Number,
  image: Object,
  isPlayer: Boolean,
  gamePhase: String,
  tooltipPosition: Object,
  districtImages: Array,
});

function calculatePoints() {
  return props.player.districts.reduce(
    (partialSum, district) => partialSum + district.cost,
    0
  );
}
</script>

<style scoped>
.card-div {
  height: 24vh;
}

.character-image {
  border-radius: 0 9999px 9999px 9999px;
  width: 3vw;
  height: 7vh;
  object-fit: cover;
}

.order-number {
  position: absolute;
  z-index: 100;
  width: 3vw;
  height: 7vh;
  outline: solid thick;
  border-radius: 4px 9999px 9999px 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 7vh;
  font-size: 3vh;
  font-weight: 700;
  background: rgba(18, 18, 18, 0.87);
}

.districts-outer {
  height: 12vh;
  display: inline-flex;
  justify-content: space-around;
  flex-direction: column;
}

.districts {
  position: relative;
  display: inline-flex;
  justify-content: space-between;
  margin-left: 0.5vw;
  margin-right: 0.5vw;
  width: 11vw;
  height: 5.5vh;
}
</style>
