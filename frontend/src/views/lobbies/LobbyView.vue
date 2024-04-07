<template>
  <div>
    <div class="container mx-auto my-4" v-if="lobbyStore.getLobby">
      <Card>
        <template #title
          ><h1 class="text-4xl">{{ lobbyStore.getLobby.name }}</h1></template
        >
        <template #content>
          <h2 class="text-2xl mb-2">
            Játékosok ({{ lobbyStore.getLobby.members.length }}/{{
              lobbyStore.getLobby.maxMembers
            }}):
          </h2>
          <DataTable
            :value="lobbyStore.getLobby.members"
            :reorderableColumns="true"
            @columnReorder="onColReorder"
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
              field="displayName"
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
                <div v-if="slotProps.data.id !== stateStore.getUser.id">
                  <Button
                    v-tooltip.bottom="'Tulajdonossá nevezés'"
                    icon="fa fa-star"
                    @click="lobbyStore.promoteMember(lobbyCode, player.id)"
                  />
                  <Button
                    v-tooltip.bottom="'Eltávolítás a lobbiból'"
                    class="ml-2 p-button-danger"
                    icon="fa fa-x"
                    @click="lobbyStore.kickMember(lobbyCode, player.id)"
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
              @click="openDeleteModal($event)"
            >
              Játék törlése
            </Button>
            <Button
              v-else
              class="p-button-danger mr-2"
              @click="lobbyStore.leaveLobby(lobbyCode)"
            >
              Játék elhagyása
            </Button>
            <Button
              @click="start()"
              :disabled="lobbyStore.getLobby.members.length < 2"
            >
              Játék indítása
            </Button>
          </div>
        </template>
      </Card>
    </div>

    <LobbySettings
      :is-owner="isOwner"
      :settings="lobbyStore.getLobby?.gameSettings"
      :players="lobbyStore.getLobby?.members"
      :cards="cardStore.getCards"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";
import { useCardStore } from "@/stores/cards";
import Button from "primevue/button";
import Card from "primevue/card";
import ConfirmDialog from "primevue/confirmpopup";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import LobbySettings from "@/components/lobbies/LobbySettings.vue";

const router = useRouter();
const confirm = useConfirm();
const toast = useToast();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();
const cardStore = useCardStore();
const lobbyCode = router.currentRoute.value.params.code;
const socket = ref();
const stompClient = ref();

const connected = ref(false);

const isOwner = computed(
  () => stateStore.getUser.id === lobbyStore.getLobby?.owner
);

onMounted(async () => {
  stateStore.setLoading(true);
  await lobbyStore.fetchLobby(lobbyCode);
  await cardStore.fetchCards();
  stateStore.getBreadcrumbs.push({
    name: "lobby",
    label: lobbyStore.getLobby.name,
    params: router.currentRoute.value.params,
  });
  connect();
  stateStore.setLoading(false);
});

function connect() {
  socket.value = new SockJS("http://localhost:3000/ws?" + stateStore.getJwt);
  stompClient.value = Stomp.over(socket);
  stompClient.value.connect({}, connectCallback, errorCallback);
}

const connectCallback = function (frame) {
  console.log("Connected!");
  connected.value = true;
  console.log(frame);
  stompClient.value.subscribe("/user/topic/lobby/update", (msg) => {
    handleLobbyUpdate(JSON.parse(msg.body));
  });
};

const errorCallback = function (error) {
  console.log(error);
  connected.value = false;
  console.log("Reconnecting...");
  setTimeout(connect, 5000);
};

function start() {
  lobbyStore.startGame(lobbyCode);
  stompClient.value.disconnect();
  router.push("/game/" + lobbyCode);
}

const openDeleteModal = (event) => {
  confirm.require({
    target: event.currentTarget,
    message: "Biztosan kitörlöd a lobbit?",
    header: "Lobbi törlése",
    accept: () => {
      lobbyStore.deleteLobby(lobbyCode);
      stompClient.value.disconnect();
      router.push("/my-games");
    },
    acceptClass: "p-button-danger",
    acceptLabel: "Igen",
    rejectClass: "p-button-primary",
    rejectLabel: "Nem",
  });
};

function handleLobbyUpdate(update) {
  switch (update.type) {
    case "JOIN": {
      lobbyStore.getLobby.members.push(update.change);
      toast.add({
        severity: "success",
        summary: "Felhasználó csatlakozott!",
        detail: `${update.change.displayName} csatlakozott a lobbihoz.`,
        life: 3000,
      });
      break;
    }
    case "LEAVE": {
      const user = lobbyStore.getLobby.members.find(
        (m) => m.id === update.change
      );
      lobbyStore.getLobby.members.splice(
        lobbyStore.getLobby.members.indexOf(user),
        1
      );
      toast.add({
        severity: "warn",
        summary: "Felhasználó kilépett!",
        detail: `${user.displayName} elhagyta a lobbit.`,
        life: 3000,
      });
      break;
    }
    case "KICK": {
      const user = lobbyStore.getLobby.members.find(
        (m) => m.id === update.change
      );
      lobbyStore.getLobby.members.splice(
        lobbyStore.getLobby.members.indexOf(user),
        1
      );
      if (update.change === stateStore.getUser.id) {
        router.push("/my-games");
        toast.add({
          severity: "error",
          summary: "Eltávolítva!",
          detail: `A tulajdonos eltávolított a lobbiból.`,
          life: 3000,
        });
      } else {
        toast.add({
          severity: "error",
          summary: "Felhasználó eltávolítva!",
          detail: `${user.displayName} eltávolítva a lobbiból.`,
          life: 3000,
        });
      }
      break;
    }
    case "OWNER": {
      lobbyStore.getLobby.owner = update.change;
      toast.add({
        severity: "info",
        summary: "Új lobbi tulajdonos!",
        detail: `${
          lobbyStore.getLobby.members.find((m) => m.id === update.change)
            .displayName
        } a lobbi új tulajdonosa.`,
        life: 3000,
      });
      break;
    }
    case "START": {
      toast.add({
        severity: "info",
        summary: "A játék indul!",
        detail:
          "A lobbitulajdonos elindította a játékot, ami rögtön kezdetét veszi!",
        life: 3000,
      });
      stompClient.value.disconnect();
      router.push("/game/" + lobbyCode);
    }
  }
}
</script>

<style scoped>
::v-deep(.p-reorderable-column) {
  .p-column-header-content {
    display: block;
  }
}
</style>
