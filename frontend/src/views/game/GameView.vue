<template>
  <Card class="w-48 text-2xl m-4">
    <template #content>
      <div class="columns-2 text-center">
        <div><i class="fa fa-coins"></i> 4</div>
        <div><i class="fa fa-star"></i> 0</div>
      </div>
    </template>
  </Card>
  <MemberList :members="lobbyStore.getLobby?.members"></MemberList>
  <PlayerHand
    :cards="cardStore.getCards?.districts"
    :card-images="cardStore.getDistrictImages"
  ></PlayerHand>
</template>

<script setup>
import { onMounted } from "vue";
import router from "@/router";
import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import { useLobbyStore } from "@/stores/lobbies";
import Card from "primevue/card";
import MemberList from "@/components/game/MemberList.vue";
import PlayerHand from "@/components/game/PlayerHand.vue";

const stateStore = useStateStore();
const cardStore = useCardStore();
const lobbyStore = useLobbyStore();
const lobbyCode = router.currentRoute.value.params.code;

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await lobbyStore.fetchLobby(lobbyCode);
  stateStore.setLoading(false);
});
</script>

<style scoped>
.p-card {
  border: 1px solid rgba(255, 255, 255, 0.12);
}
</style>
