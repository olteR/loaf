<template>
  <div class="container mx-auto my-4">
    <Card>
      <template #content>
        <div class="grid grid-cols-6">
          <div class="col-span-5">
            <Button
              icon="pi pi-refresh"
              :disabled="!canRefresh"
              @click="getLobbies()"
            />
          </div>
          <div>
            <Button
              label="új lobbi"
              class="float-right"
              icon="pi pi-plus-circle"
              @click="openDialog()"
            />
          </div>
        </div>
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
    <Panel v-for="lobby in lobbyStore.getLobbies" :key="lobby.name">
      <template #header>
        <span>
          <i v-if="lobby.secured" class="fa fa-lock"></i>
          {{ lobby.name }}
        </span>
        <div class="ml-auto">{{ lobby.members.length }}/{{ lobby.maxMembers }}</div>
      </template>
      <div class="grid grid-cols-2">
        <div>
          tulajdonos: {{ lobby.members.find((m) => m.id === lobby.owner).displayName }}
        </div>
        <div class="ml-auto">
          <Button label="csatlakozás" class="w-full"></Button>
        </div>
      </div>

    </Panel>
    <DynamicDialog />
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
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
const canRefresh = ref(false);

onMounted(async () => {
  await getLobbies();
});

async function getLobbies() {
  canRefresh.value = false;
  stateStore.setLoading(true);
  await lobbyStore.fetchLobbies();
  stateStore.setLoading(false);
  setTimeout(() => {
    canRefresh.value = true;
  }, 3000);
}

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
  margin-bottom: 1rem !important;
}

.p-panel {
  margin-bottom: 0.5rem !important;
}

.p-button {
  margin-right: 1rem;
}

::v-deep(.p-panel) {
  .p-panel-header {
    font-size: 2rem;
  }
}
</style>
