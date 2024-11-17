<template>
  <div
    v-if="district"
    class="list-card"
    :class="{ hoverable: clickable }"
    :style="{
      'outline-color': primaryColor,
      'background-image': 'url(' + imageSource + ')',
      cursor: clickable ? 'pointer' : 'default',
    }"
  >
    <div
      class="district-cost"
      :style="{
        background: secondaryColor,
        'outline-color': primaryColor,
      }"
    >
      {{ district.cost }}
    </div>
    <div
      class="card-content"
      :style="{
        'background-color': secondaryColor,
      }"
    >
      <div
        class="mt-2 select-none"
        v-tooltip="{
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
const primaryColor = computed(() => {
  return props.selected
    ? COLORS[props.district.type].PRIMARY
    : props.clickable
    ? COLORS.DISABLED.PRIMARY
    : COLORS.DEFAULT.PRIMARY;
});

const secondaryColor = computed(() => {
  return props.selected
    ? COLORS[props.district.type].SECONDARY
    : props.clickable
    ? COLORS.DISABLED.SECONDARY
    : COLORS.DEFAULT.SECONDARY;
});
</script>

<style scoped>
.list-card {
  width: 100%;
  outline: solid thick;
  border-radius: 4px;
  margin-bottom: 1.25rem;
  margin-left: 1.25rem;
  background-position: center;
  background-size: cover;
  height: 2.5rem;
  display: inline-flex;
  align-items: center;
  background-blend-mode: overlay;
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
}
</style>
