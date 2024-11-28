<template>
  <div
    v-if="card"
    class="district"
    :class="{ hoverable: selectable && !props.protected }"
    :style="{
      outline: 'medium solid',
      'outline-color': getPrimaryColor,
    }"
  >
    <div
      class="district-cost inline-flex items-center justify-center"
      :style="{ background: getSecondaryColor }"
      v-tooltip:[tooltipPosition]="{
        value: `${composeDistrictDescription(card, true)}${
          selectable ? '' : '<p><b>Ezt a kerületet nem választhatod.</b></p>'
        }`,
        escape: false,
      }"
    >
      <i v-if="props.protected" class="fa fa-shield-halved"></i>
      <span
        :class="{
          'text-red-500':
            hasGreatWall &&
            !card.abilities
              .map((ability) => ability.enum)
              .includes(DISTRICTS.GREAT_WALL),
        }"
        v-else
      >
        {{
          hasGreatWall &&
          !card.abilities
            .map((ability) => ability.enum)
            .includes(DISTRICTS.GREAT_WALL)
            ? card.cost + 1
            : card.cost
        }}
      </span>
    </div>
    <img class="district-img" :src="image.src" />
  </div>
</template>

<script setup>
import {
  composeDistrictDescription,
  primaryColor,
  secondaryColor,
} from "@/utils/utils";
import { computed } from "vue";
import { DISTRICTS } from "../../../utils/const";

const props = defineProps({
  card: Object,
  image: Object,
  selected: Boolean,
  selectable: Boolean,
  protected: Boolean,
  hasGreatWall: Boolean,
  tooltipPosition: Object,
});

const getPrimaryColor = computed(() => {
  return props.selected ? primaryColor(props.card.type) : "#121212";
});

const getSecondaryColor = computed(() => {
  return props.selected ? secondaryColor(props.card.type) : "#121212";
});
</script>

<style scoped>
.district {
  width: 3vh;
  height: 4.5vh;
  margin-top: 0.5vh;
  color: white;
  border-radius: 4px;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
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
