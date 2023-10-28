<template>
  <div>
    <DistrictCard :card="districtStore.getDistricts[0]"></DistrictCard>
    <DistrictCard :card="districtStore.getDistricts[1]"></DistrictCard>
    <DistrictCard :card="districtStore.getDistricts[9]"></DistrictCard>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useStateStore } from "@/stores/state";
import { useDistrictStore } from "@/stores/districts";
import DistrictCard from "@/components/game/DistrictCard.vue";

const stateStore = useStateStore();
const districtStore = useDistrictStore();

onMounted(async () => {
  stateStore.setLoading(true);
  await districtStore.fetchDistricts();
  stateStore.setLoading(false);
});
</script>

<style scoped></style>
