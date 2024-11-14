<template>
  <div class="container mx-auto my-4" v-if="lobbyStore.getLobby">
    <Card>
      <template #title>
        <div class="inline-flex justify-between w-full">
          <div class="text-4xl">
            {{ lobbyStore.getLobby.name }}
            <i
              v-if="lobbyStore.getLobby.secured"
              v-tooltip.top="{
                value: 'Jelszóval védett lobbi',
                escape: false,
              }"
              class="fa fa-lock"
            ></i>
          </div>
          <div v-if="isOwner">
            <Button
              v-if="lobbyStore.getLobby.secured"
              v-tooltip.bottom="'Lobbi kinyitása'"
              icon="fa fa-lock-open"
              :loading="starting || modalLoading"
              @click="openLobby"
            />
            <Button
              class="ml-2"
              v-tooltip.bottom="
                lobbyStore.getLobby.secured
                  ? 'Jelszó módosítása'
                  : 'Lobbi levédése'
              "
              :icon="lobbyStore.getLobby.secured ? 'fa fa-key' : 'fa fa-lock'"
              :loading="starting"
              @click="passwordModalVisible = true"
            />
            <Button
              class="ml-2"
              v-tooltip.bottom="'Lobbi beállításai'"
              icon="fa fa-gear"
              :loading="starting"
              @click="editModalVisible = true"
            />
          </div>
        </div>
      </template>
      <template #content>
        <h2 class="text-2xl mb-2">
          Játékosok ({{ lobbyStore.getLobby.members?.length }}/{{
            lobbyStore.getLobby.maxMembers
          }}):
        </h2>
        <DataTable
          :value="lobbyStore.getLobby.members"
          :reorderableColumns="true"
          @rowReorder="onRowReorder"
        >
          <Column
            v-if="isOwner"
            rowReorder
            headerStyle="width: 3rem"
            :reorderableColumn="false"
          />
          <Column
            header="Sorrend"
            style="width: 8rem"
            bodyStyle="text-align: center"
            headerStyle="text-align: center; font-size: 1.5rem;"
          >
            <template #body="{ data }">
              {{ lobbyStore.getLobby.members.indexOf(data) + 1 + "." }}
            </template>
          </Column>
          <Column
            field="name"
            header="Név"
            bodyStyle="text-align: center"
            headerStyle="text-align: center; font-size: 1.5rem;"
          >
            <template #body="{ data }">
              {{ data.name }}
              <i
                v-if="data.id === lobbyStore.getLobby.owner"
                class="fa fa-star mr-1"
              />
            </template>
          </Column>
          <Column
            v-if="isOwner"
            header="Műveletek"
            style="width: 16rem"
            bodyStyle="text-align: center"
            headerStyle="text-align: center; font-size: 1.5rem;"
          >
            <template #body="slotProps">
              <div v-if="slotProps.data.id !== stateStore.getUser?.id">
                <Button
                  v-tooltip.bottom="'Tulajdonossá nevezés'"
                  icon="fa fa-star"
                  :loading="starting"
                  @click="
                    lobbyStore.promoteMember(lobbyCode, slotProps.data.id)
                  "
                />
                <Button
                  v-tooltip.bottom="'Eltávolítás a lobbiból'"
                  class="ml-2 p-button-danger"
                  icon="fa fa-x"
                  :loading="starting"
                  @click="lobbyStore.kickMember(lobbyCode, slotProps.data.id)"
                />
              </div>
            </template>
          </Column>
        </DataTable>

        <div class="mt-2 ml-auto">
          <ConfirmDialog></ConfirmDialog>
          <Button
            v-if="isOwner"
            class="p-button-danger mr-2"
            :loading="starting"
            @click="openDeleteModal($event)"
          >
            Lobbi törlése
          </Button>
          <Button
            v-else
            class="p-button-danger mr-2"
            :loading="starting"
            @click="lobbyStore.leaveLobby(lobbyCode)"
          >
            Játék elhagyása
          </Button>
          <Button
            v-if="isOwner"
            :disabled="lobbyStore.getLobby.members?.length < 4"
            :loading="starting"
            @click="start()"
          >
            Játék indítása
          </Button>
        </div>
      </template>
    </Card>
    <LobbySettings
      :is-owner="isOwner"
      :settings="lobbyStore.getLobby?.gameSettings"
      :players="lobbyStore.getLobby?.members"
      :cards="cardStore.getCards"
      :loading="starting"
      @characters="(characters) => updateCharacters(characters)"
      @districts="(districts) => updateDistricts(districts)"
      @crown="(player) => crownPlayer(player)"
    />
    <Dialog
      v-model:visible="editModalVisible"
      modal
      header="Lobbi szerkesztése"
    >
      <LobbyModal
        :edited-lobby="lobbyStore.getLobby"
        @hide="editModalVisible = false"
      />
    </Dialog>
    <Dialog
      v-model:visible="passwordModalVisible"
      modal
      header="Jelszó megadása"
    >
      <PasswordModal
        button-label="Küldés"
        :loading="modalLoading"
        @submit="(pass) => updateSecurity(pass)"
      />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { onBeforeRouteLeave, useRouter } from "vue-router";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { useStateStore } from "@/stores/state";
import { useWebsocketStore } from "@/stores/websocket";
import { useLobbyStore } from "@/stores/lobbies";
import { useCardStore } from "@/stores/cards";
import Button from "primevue/button";
import Card from "primevue/card";
import ConfirmDialog from "primevue/confirmpopup";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import LobbySettings from "@/components/lobbies/LobbySettings.vue";
import { LOBBY_STATUS } from "@/utils/const";
import LobbyModal from "@/components/lobbies/LobbyModal.vue";
import Dialog from "primevue/dialog";
import PasswordModal from "@/components/lobbies/PasswordModal.vue";

const router = useRouter();
const confirm = useConfirm();
const toast = useToast();
const stateStore = useStateStore();
const websocketStore = useWebsocketStore();
const lobbyStore = useLobbyStore();
const cardStore = useCardStore();
const lobbyCode = router.currentRoute.value.params.code;

const starting = ref(false);
const editModalVisible = ref(false);
const passwordModalVisible = ref(false);
const modalLoading = ref(false);

const isOwner = computed(
  () => stateStore.getUser.id === lobbyStore.getLobby?.owner
);

onMounted(async () => {
  stateStore.setLoading(true);
  await lobbyStore.fetchLobby(lobbyCode);
  await cardStore.fetchCards();
  if (lobbyStore.getLobby?.status === LOBBY_STATUS.ONGOING) {
    await router.push("/game/" + lobbyCode);
  }
  stateStore.getBreadcrumbs.push({
    name: "lobby",
    label: lobbyStore.getLobby.name,
    params: router.currentRoute.value.params,
  });
  websocketStore.subscribeToLobby();
  stateStore.setLoading(false);
});

onBeforeRouteLeave(() => {
  websocketStore.unsubscribe();
});

async function openLobby() {
  modalLoading.value = true;
  await lobbyStore.updateSecurity({ secured: false }, lobbyCode);
  toast.add({
    severity: "info",
    summary: "Kinyitva",
    detail: `A lobbi jelszava eltávolítva`,
    life: 3000,
  });
  modalLoading.value = false;
}

async function updateSecurity(pass) {
  modalLoading.value = true;
  await lobbyStore.updateSecurity({ secured: true, password: pass }, lobbyCode);
  toast.add({
    severity: "info",
    summary: "Levédve",
    detail: `A lobbi jelszava frissítve lett`,
    life: 3000,
  });
  modalLoading.value = false;
  passwordModalVisible.value = false;
}

async function start() {
  starting.value = true;
  try {
    await lobbyStore.startGame(lobbyCode);
    toast.add({
      severity: "success",
      summary: "Indítás sikeres",
      detail: `Jó játékot!`,
      life: 3000,
    });
  } finally {
    starting.value = false;
  }
}

async function updateCharacters(characters) {
  await lobbyStore.updateCharacters(
    lobbyCode,
    characters.map((character) => character.id)
  );
}

async function updateDistricts(districts) {
  await lobbyStore.updateDistricts(
    lobbyCode,
    districts.map((district) => district.id)
  );
}

async function crownPlayer(player) {
  await lobbyStore.crownMember(lobbyCode, player.id);
}

const openDeleteModal = (event) => {
  confirm.require({
    target: event.currentTarget,
    message: "Biztosan kitörlöd a lobbit?",
    header: "Lobbi törlése",
    accept: async () => {
      await lobbyStore.deleteLobby(lobbyCode);
    },
    acceptClass: "p-button-danger",
    acceptLabel: "Igen",
    rejectClass: "p-button-primary",
    rejectLabel: "Nem",
  });
};
</script>

<style scoped>
::v-deep(.p-reorderable-column) {
  .p-column-header-content {
    display: block;
  }
}
</style>
