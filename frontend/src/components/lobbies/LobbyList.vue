<template>
  <div>
    <Panel v-for="lobby in props.lobbies" :key="lobby.name" class="mb-2">
      <template #header>
        <span class="text-4xl">
          {{ lobby.name }}
          <i
            v-if="lobby.secured"
            v-tooltip.top="{
              value: 'Jelszóval védett lobbi',
              escape: false,
            }"
            class="fa fa-lock"
          ></i>
          <i
            v-if="lobby.status === LOBBY_STATUS.ONGOING"
            v-tooltip.top="{
              value: 'Folyamatban lévő játék',
              escape: false,
            }"
            class="fa fa-gamepad"
          ></i>
        </span>
        <div class="ml-auto text-4xl">
          {{ lobby.members.length }}/{{ lobby.maxMembers }}
        </div>
      </template>
      <div class="inline-flex justify-between w-full">
        <div>
          Játékosok:
          <Chip v-for="player in lobby.members" :key="player.id" class="mr-1">
            <i v-if="player.id === lobby.owner" class="fa fa-star mr-1" />
            {{ player.name }}
          </Chip>
        </div>
        <div class="ml-auto w-auto">
          <Button
            v-if="props.type === 'search'"
            label="Csatlakozás"
            icon="pi pi-user-plus"
            @click="joinLobby(lobby)"
          ></Button>
          <Button
            v-else-if="
              props.type === 'mine' && lobby.status === LOBBY_STATUS.CREATED
            "
            label="Lobbi megnyitása"
            icon="pi pi-play"
            class="mr-2"
            @click="router.push(`lobby/${lobby.code}`)"
          ></Button>
          <Button
            v-else
            label="Játék megnyitása"
            icon="fa fa-gamepad"
            class="mr-2"
            @click="router.push(`game/${lobby.code}`)"
          ></Button>
          <Button
            v-if="
              props.type === 'mine' && lobby.status === LOBBY_STATUS.CREATED
            "
            label="Kilépés"
            icon="pi pi-sign-out"
            class="p-button-danger"
            @click="lobbyStore.leaveLobby(lobby.code)"
          ></Button>
        </div>
      </div>
    </Panel>
    <Dialog
      v-model:visible="passwordModalVisible"
      modal
      header="Jelszó megadása"
    >
      <PasswordModal
        button-label="Belépés"
        :loading="modalLoading"
        @submit="(pass) => joinSecuredLobby(pass)"
      ></PasswordModal>
    </Dialog>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";
import PasswordModal from "@/components/lobbies/PasswordModal.vue";
import Button from "primevue/button";
import Chip from "primevue/chip";
import Dialog from "primevue/dialog";
import Panel from "primevue/panel";
import { LOBBY_STATUS } from "@/utils/const";

const props = defineProps({
  lobbies: Array,
  type: String,
});

const router = useRouter();
const stateStore = useStateStore();
const lobbyStore = useLobbyStore();

const passwordModalVisible = ref(false);
const modalLoading = ref(false);
const modalCode = ref();

async function joinLobby(lobby) {
  if (lobby.secured) {
    modalCode.value = lobby.code;
    passwordModalVisible.value = true;
  } else {
    stateStore.setLoading(true);
    await lobbyStore.joinLobby({ code: lobby.code });
    stateStore.setLoading(false);
  }
}

async function joinSecuredLobby(pass) {
  modalLoading.value = true;
  try {
    await lobbyStore.joinLobby({
      code: modalCode.value,
      password: pass,
    });
  } finally {
    modalLoading.value = false;
  }
}
</script>

<style scoped></style>
