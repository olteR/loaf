<template>
  <div
    v-if="props.card"
    class="district"
    :style="{
      outline: 'medium solid',
      'outline-color': COLORS[props.card.type].PRIMARY,
    }"
  >
    <div>
      <div
        class="district-cost items-center"
        :style="{ background: COLORS[props.card.type].SECONDARY }"
        v-tooltip:[tooltipPosition]="{
          value: composeDistrictDescription(card, true),
          escape: false,
        }"
      >
        {{ props.card.cost }}
      </div>
      <img class="district-img" :src="props.image.src" />
    </div>
  </div>
</template>

<script setup>
import { COLORS } from "@/utils/const";
import { composeDistrictDescription } from "@/utils/utils";

const props = defineProps({
  card: Object,
  image: Object,
  tooltipPosition: Object,
});
</script>

<style scoped>
.district {
  width: 3vh;
  height: 4.5vh;
  margin-top: 0.5vh;
  color: white;
  border-radius: 4px;
}
.district-cost {
  position: absolute;
  text-align: center;
  width: 3vh;
  height: 3vh;
  border-radius: 4px 4px 9999px 9999px;
  opacity: 0.8;
}
</style>
