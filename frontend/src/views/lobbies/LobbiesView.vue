<template>
  <div class="container mx-auto my-4">
    <Card>
      <template #content>
        <div class="grid grid-cols-6">
          <div class="col-span-5">
            <Button
              icon="pi pi-sync"
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
    <div v-if="loading">
      <ProgressSpinner
        aria-label="loading"
        class="fixed top-1/2 left-1/2"
      ></ProgressSpinner>
    </div>
    <div v-else>
      <Card v-if="lobbyStore.getLobbies.length === 0">
        <template #content>
          <div class="text-center select-none">
            Úgy tűnik jelenleg nincs lobbi amihez csatlakozhatnál. Hozz létre
            sajátot az "Új lobbi" gomb megnyomásával!
          </div>
        </template>
      </Card>
      <Panel v-for="lobby in lobbyStore.getLobbies" :key="lobby.name">
        <template #header>
          <span>
            <i v-if="lobby.secured" class="fa fa-lock"></i>
            {{ lobby.name }}
          </span>
          <div class="ml-auto">
            {{ lobby.members.length }}/{{ lobby.maxMembers }}
          </div>
        </template>
        <div class="grid grid-cols-2">
          <div>
            játékosok:
            <span v-for="player in lobby.members" :key="player.id">
              <i v-if="player.id === lobby.owner" class="fa fa-crown" />
              {{ player.displayName }}
            </span>
          </div>
          <div class="ml-auto">
            <Button
              label="csatlakozás"
              class="w-full"
              icon="pi pi-user-plus"
            ></Button>
          </div>
        </div>
      </Panel>
    </div>
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
import ProgressSpinner from "primevue/progressspinner";

const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const dialog = useDialog();
const canRefresh = ref(false);
const loading = ref(false);

onMounted(async () => {
  stateStore.setLoading(true);
  await lobbyStore.fetchLobbies();
  stateStore.setLoading(false);
  setTimeout(() => {
    canRefresh.value = true;
  }, 3000);
});

async function getLobbies() {
  canRefresh.value = false;
  loading.value = true;
  await lobbyStore.fetchLobbies();
  loading.value = false;
  setTimeout(() => {
    canRefresh.value = true;
  }, 3000);
}

function openDialog() {
  dialog.open(CreateLobbyModal, {
    props: {
      header: "Új lobbi létrehozása",
      modal: true,
      closable: false,
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
