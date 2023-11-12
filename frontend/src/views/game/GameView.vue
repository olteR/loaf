<template>
  <Card class="w-48 text-2xl m-4">
    <template #content>
      <div class="columns-2 text-center">
        <div><i class="fa fa-coins"></i> 4</div>
        <div><i class="fa fa-star"></i> 0</div>
      </div>
    </template>
  </Card>
  <MemberList :members="lobbyStore.getLobby.members"></MemberList>
  <Hand :cards="districtStore.getDistricts"></Hand>
</template>

<script setup>
import { onMounted } from "vue";
import { useStateStore } from "@/stores/state";
import { useDistrictStore } from "@/stores/districts";
import { useLobbyStore } from "@/stores/lobbies";
import Card from "primevue/card";
import MemberList from "@/components/game/MemberList.vue";
import Hand from "@/components/game/Hand.vue";

const stateStore = useStateStore();
const districtStore = useDistrictStore();
const lobbyStore = useLobbyStore();

onMounted(async () => {
  stateStore.setLoading(true);
  await districtStore.fetchDistricts();
  stateStore.setLoading(false);
});
</script>

<style scoped>
.p-card {
  border: 1px solid rgba(255, 255, 255, 0.12);
}
</style>
