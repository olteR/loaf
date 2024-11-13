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
            v-if="lobby.status === 'ONGOING'"
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
      <div class="grid grid-cols-2">
        <div>
          játékosok:
          <Chip v-for="player in lobby.members" :key="player.id" class="mr-1">
            <i v-if="player.id === lobby.owner" class="fa fa-star mr-1" />
            {{ player.name }}
          </Chip>
        </div>
        <div class="ml-auto w-auto">
          <Button
            v-if="props.type === 'search'"
            label="csatlakozás"
            icon="pi pi-user-plus"
            @click="joinLobby(lobby)"
          ></Button>
          <Button
            v-else-if="props.type === 'mine' && lobby.status === 'CREATED'"
            label="lobbi megnyitása"
            icon="pi pi-play"
            class="mr-2"
            @click="router.push('lobby/'.concat(lobby.code))"
          ></Button>
          <Button
            v-else
            label="játék megnyitása"
            icon="fa fa-gamepad"
            class="mr-2"
            @click="router.push('game/'.concat(lobby.code))"
          ></Button>
          <Button
            v-if="props.type === 'mine'"
            label="kilépés"
            icon="pi pi-sign-out"
            class="p-button-danger"
          ></Button>
        </div>
      </div>
    </Panel>
    <Dialog
      v-model:visible="passwordModalVisible"
      modal
      header="Jelszó megadása"
    >
      <form @submit.prevent="joinSecuredLobby()">
        <span class="p-float-label">
          <Password
            id="password"
            v-model="password"
            :feedback="false"
            toggleMask
            input-class="w-full"
            required
          />
          <label for="password">Jelszó</label>
        </span>
        <Button
          type="submit"
          label="Belépés"
          :loading="modalLoading"
          class="p-button-primary mt-4 float-right"
        />
      </form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStateStore } from "@/stores/state";
import { useLobbyStore } from "@/stores/lobbies";
import Button from "primevue/button";
import Chip from "primevue/chip";
import Dialog from "primevue/dialog";
import Panel from "primevue/panel";
import Password from "primevue/password";

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
const password = ref();

async function joinLobby(lobby) {
  if (lobby.secured) {
    modalCode.value = lobby.code;
    password.value = null;
    passwordModalVisible.value = true;
  } else {
    stateStore.setLoading(true);
    await lobbyStore.joinLobby({ code: lobby.code });
    stateStore.setLoading(false);
  }
}

async function joinSecuredLobby() {
  modalLoading.value = true;
  try {
    await lobbyStore.joinLobby({
      code: modalCode.value,
      password: password.value,
    });
  } finally {
    modalLoading.value = false;
  }
}
</script>

<style scoped></style>
