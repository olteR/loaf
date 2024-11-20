<template>
  <div
    v-if="district"
    class="list-card"
    :class="{ hoverable: clickable }"
    :style="{
      'outline-color': COLORS[district.type].PRIMARY,
      'background-image': 'url(' + imageSource + ')',
    }"
  >
    <div
      class="district-cost"
      :style="{
        background: COLORS[district.type].SECONDARY,
        'outline-color': COLORS[district.type].PRIMARY,
      }"
    >
      {{ district.cost }}
    </div>
    <div
      class="card-content"
      :style="{
        'background-color': selected
          ? 'transparent'
          : COLORS[district.type].SECONDARY,
      }"
    >
      <div
        class="card-name"
        :style="{
          background: COLORS[district.type].SECONDARY,
        }"
        v-tooltip.top="{
          value: composeDistrictDescription(district),
          escape: false,
        }"
      >
        {{ district.name }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { COLORS } from "@/utils/const";
import { composeDistrictDescription } from "@/utils/utils";

const props = defineProps({
  district: Object,
  selected: Boolean,
  selectable: Boolean,
  clickable: Boolean,
});

const imageSource = computed(
  () =>
    `${window.location.origin}/src/assets/districts/${props.district?.id}.jpg`
);
</script>

<style scoped>
.list-card {
  width: 140px;
  outline: solid thick;
  border-radius: 4px;
  margin-bottom: 1.25rem;
  margin-left: 1.25rem;
  background-position: center;
  background-size: cover;
  height: 210px;
  display: inline-flex;
  align-items: center;
  background-blend-mode: overlay;
}
.card-content {
  width: 100%;
  height: 100%;
  opacity: 0.8;
  font-weight: bold;
  margin-left: -1.25rem;
}
.card-name {
  user-select: none;
  position: relative;
  top: 100%;
  width: 100%;
  opacity: 0.8;
  transform: translateY(-100%);
  text-align: center;
  font-weight: 700;
}
.card-info {
  position: relative;
  text-align: end;
  margin-right: 0.25rem;
  top: 0;
  transform: translateY(-100%);
}
.district-cost {
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
  position: relative;
  top: -90px;
}
</style>
