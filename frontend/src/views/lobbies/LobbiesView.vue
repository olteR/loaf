<template>
  <div class="container mx-auto my-4">
    <Card>
      <template #content>
        <div class="grid grid-cols-6">
          <div class="col-span-5 flex">
            <Button
              class="w-12 h-12 my-auto"
              icon="pi pi-sync"
              :disabled="!canRefresh"
              @click="getLobbies()"
            />
            <span class="p-float-label">
              <InputText id="search" type="text" v-model="search" />
              <label for="search">keresés</label>
            </span>
            <span class="my-auto ml-8">
              <InputSwitch
                class="mr-2"
                v-model="showLocked"
                inputId="lockedSwitch"
              />
              <label class="select-none" for="lockedSwitch"
                >Zárt lobbik mutatása</label
              >
            </span>
            <span class="my-auto ml-8">
              <InputSwitch
                class="mr-2"
                v-model="showFull"
                inputId="fullSwitch"
              />
              <label class="select-none" for="fullSwitch"
                >Teli lobbik mutatása</label
              >
            </span>
          </div>
          <div>
            <Button
              label="új lobbi"
              class="float-right"
              icon="pi pi-plus-circle"
              @click="createModalVisible = true"
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
      <LobbyList :lobbies="lobbyStore.getLobbies" :type="'search'" />
    </div>
    <Dialog
      v-model:visible="createModalVisible"
      modal
      header="Új lobbi létrehozása"
    >
      <CreateLobbyModal />
    </Dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useLobbyStore } from "@/stores/lobbies";
import { useStateStore } from "@/stores/state";
import CreateLobbyModal from "@/components/lobbies/CreateLobbyModal.vue";
import Button from "primevue/button";
import Card from "primevue/card";
import Dialog from "primevue/dialog";
import InputSwitch from "primevue/inputswitch";
import ProgressSpinner from "primevue/progressspinner";
import LobbyList from "@/components/lobbies/LobbyList.vue";
import InputText from "primevue/inputtext";

const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const canRefresh = ref(false);
const createModalVisible = ref(false);
const loading = ref(false);
const search = ref();
const showLocked = ref(true);
const showFull = ref(true);

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
</script>

<style scoped>
.p-card {
  margin-bottom: 1rem !important;
}

.p-button {
  margin-right: 1rem;
}
</style>
