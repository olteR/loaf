<template>
  <Hand
    :cards="[
      districtStore.getDistricts[0],
      districtStore.getDistricts[1],
      districtStore.getDistricts[9],
    ]"
  ></Hand>
</template>

<script setup>
import { onMounted } from "vue";
import { useStateStore } from "@/stores/state";
import { useDistrictStore } from "@/stores/districts";
import Hand from "@/components/game/Hand.vue";

const stateStore = useStateStore();
const districtStore = useDistrictStore();

onMounted(async () => {
  stateStore.setLoading(true);
  await districtStore.fetchDistricts();
  stateStore.setLoading(false);
});
</script>

<style scoped></style>
