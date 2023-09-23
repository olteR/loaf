<template>
  <div class="container mx-auto my-4">
    <Card>
      <template #content>
        <Button
          label="Új lobbi"
          class="new-lobby-btn"
          icon="pi pi-plus-circle"
          @click="openDialog()"
        />
      </template>
    </Card>
    <Card v-if="lobbyStore.getLobbies.length === 0">
      <template #content>
        <div class="text-center select-none">
          Úgy tűnik jelenleg nincs aktív lobbi. Hozz létre sajátot az "Új lobbi"
          gomb megnyomásával!
        </div>
      </template>
    </Card>
    <Panel
      v-for="lobby in lobbyStore.getLobbies"
      header="lobby.name"
      :key="lobby.name"
      >{{ lobby.name }}</Panel
    >
    <DynamicDialog />
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useLobbyStore } from "@/stores/lobbies";
import { useStateStore } from "@/stores/state";
import { useDialog } from "primevue/usedialog";
import CreateLobbyModal from "@/components/lobbies/CreateLobbyModal.vue";
import Button from "primevue/button";
import Card from "primevue/card";
import DynamicDialog from "primevue/dynamicdialog";
import Panel from "primevue/panel";

const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const dialog = useDialog();

onMounted(async () => {
  stateStore.setLoading(true);
  await lobbyStore.fetchLobbies();
  stateStore.setLoading(false);
});

function openDialog() {
  dialog.open(CreateLobbyModal, {
    props: {
      header: "Új lobbi létrehozása",
      modal: true,
    },
  });
}
</script>

<style scoped>
.p-card {
  margin-bottom: 0.5rem !important;
}
.p-button {
  margin-right: 1rem;
}
.new-lobby-btn {
  display: block;
  margin-left: auto;
}
</style>
