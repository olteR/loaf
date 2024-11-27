<template>
  <Panel
    class="my-6"
    style="outline: solid thick"
    :class="{
      'outline-amber-400': placement === 1,
      'outline-gray-400': placement === 2,
      'outline-amber-800': placement === 3,
    }"
  >
    <template #header>
      <div :class="`text-${Math.max(5 - placement, 2)}xl font-bold`">
        <i
          v-if="placement < 4"
          class="mr-2 fa fa-trophy"
          :class="{
            'text-amber-400': placement === 1,
            'text-gray-400': placement === 2,
            'text-amber-800': placement === 3,
          }"
        />
        <span>
          {{ `${placement}. ${player.name}` }}
        </span>
      </div>
    </template>
    <div class="grid grid-cols-2 gap-x-8">
      <div>
        <div class="font-bold mb-2">Város:</div>
        <div v-if="player.districts.length > 0" class="inline-flex w-full">
          <BuiltDistrict
            v-for="(district, ind) in player.districts"
            :key="ind"
            :card="district"
            :image="cardStore.getDistrictImages[district.id - 1]"
            class="mr-4"
          />
        </div>
        <div v-else>A játékosnak nincs megépített kerülete.</div>
        <div class="font-bold mb-2 mt-8">Kézben maradt lapok:</div>
        <div v-if="player.hand.length > 0" class="inline-flex w-full">
          <BuiltDistrict
            v-for="(district, ind) in player.hand"
            :key="ind"
            :card="district"
            :image="cardStore.getDistrictImages[district.id - 1]"
            class="mr-4"
          />
        </div>
        <div v-else>A játékosnak nem maradt lap a kezében.</div>
      </div>
      <div>
        <div class="text-2xl font-bold">
          Végső pontszám: {{ player.points }}
          <i class="fa fa-star" />
        </div>
        <div>
          <span>
            Pontszám kerületek értékéből:
            {{
              player.districts.reduce(
                (partialSum, district) => partialSum + district.cost,
                0
              )
            }}</span
          >
          <i class="fa fa-star ml-1" />
        </div>
        <div>
          <span> Van minden típusú kerülete: </span>
          <i v-if="allDistricts(player)" class="fa fa-check ml-1" />
          <i v-else class="fa fa-x ml-1" />
        </div>
        <div>
          <span>Megmaradt arany: {{ player.gold }}</span>
          <i class="fa fa-coins ml-1" />
        </div>
      </div>
    </div>
  </Panel>
</template>

<script setup>
import Panel from "primevue/panel";
import BuiltDistrict from "@/components/game/members/BuiltDistrict.vue";
import { useCardStore } from "@/stores/cards";
import { DISTRICT_TYPE } from "@/utils/const";

const cardStore = useCardStore();

const props = defineProps({
  player: Object,
  placement: Number,
});

function allDistricts(player) {
  const districtTypes = player.districts.map((district) => district.type);
  if (!districtTypes.includes(DISTRICT_TYPE.NOBLE)) {
    return false;
  }
  if (!districtTypes.includes(DISTRICT_TYPE.RELIGIOUS)) {
    return false;
  }
  if (!districtTypes.includes(DISTRICT_TYPE.TRADE)) {
    return false;
  }
  if (!districtTypes.includes(DISTRICT_TYPE.MILITARY)) {
    return false;
  }
  if (!districtTypes.includes(DISTRICT_TYPE.UNIQUE)) {
    return false;
  }
  return true;
}
</script>

<style scoped></style>
