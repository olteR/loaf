<template>
  <form @submit.prevent="createLobby()">
    <div class="grid gap-x-8 gap-y-4 grid-cols-2 p-1">
      <div class="p-float-label self-end">
        <InputText
          id="name"
          v-model="lobbyForm.name"
          class="w-full"
          required
        ></InputText>
        <label for="name">Név</label>
      </div>

      <div class="p-float-label" v-if="!editedLobby">
        <Password
          id="password"
          v-model="lobbyForm.password"
          :disabled="!lobbyForm.secured"
          :feedback="false"
          toggleMask
          class="w-full"
          :required="lobbyForm.secured"
        ></Password>
        <label for="password">Jelszó</label>
      </div>
      <div class="grid grid-cols-1">
        <label for="maxMembers" class="text-center">Maximum játékos</label>
        <InputNumber
          v-model="lobbyForm.maxMembers"
          id="maxMembers"
          :min="MIN_LOBBY_PLAYERS"
          :max="MAX_LOBBY_PLAYERS"
          showButtons
          buttonLayout="horizontal"
          decrementButtonClass="primary"
          incrementButtonClass="primary"
          incrementButtonIcon="pi pi-plus"
          decrementButtonIcon="pi pi-minus"
        />
      </div>
      <div class="relative" v-if="!editedLobby">
        <div
          class="absolute bottom-0 w-full grid grid-cols-8"
          style="height: 3.75rem"
        >
          <SelectButton
            v-model="lobbyForm.secured"
            :options="securedOptions"
            :allow-empty="false"
            option-value="value"
            option-label="label"
            class="col-span-7 grid grid-cols-2"
          />
          <i
            v-tooltip.right="
              'A védett lobbihoz történő belépéshez a létrehozáskor megadott jelszó szükséges.'
            "
            class="fa fa-info-circle m-auto"
          ></i>
        </div>
      </div>
      <div class="col-span-2 mt-2">
        <Button
          type="submit"
          class="float-right p-button-success"
          :label="props.editedLobby?.id ? 'Szerkesztés' : 'Létrehozás'"
          icon="pi pi-check"
          :loading="loading"
        ></Button>
      </div>
    </div>
  </form>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useLobbyStore } from "@/stores/lobbies";
import Button from "primevue/button";
import InputNumber from "primevue/inputnumber";
import InputText from "primevue/inputtext";
import Password from "primevue/password";
import SelectButton from "primevue/selectbutton";
import { MAX_LOBBY_PLAYERS, MIN_LOBBY_PLAYERS } from "@/utils/const";

const props = defineProps({
  editedLobby: Object,
});

const lobbyStore = useLobbyStore();
const emit = defineEmits(["hide"]);
const loading = ref(false);

const lobbyForm = ref({
  name: null,
  secured: false,
  maxMembers: MAX_LOBBY_PLAYERS,
});

const securedOptions = ref([
  {
    label: "Nyílt",
    value: false,
  },
  {
    label: "Védett",
    value: true,
  },
]);

onMounted(() => {
  if (props.editedLobby) {
    lobbyForm.value.name = props.editedLobby.name;
    lobbyForm.value.secured = props.editedLobby.secured;
    lobbyForm.value.maxMembers = props.editedLobby.maxMembers;
  } else {
    lobbyForm.value.password = null;
  }
});

async function createLobby() {
  loading.value = true;
  try {
    if (props.editedLobby?.id) {
      await lobbyStore.editLobby(lobbyForm.value, props.editedLobby.code);
    } else {
      await lobbyStore.createLobby(lobbyForm.value);
    }
  } finally {
    loading.value = false;
  }
  emit("hide");
}
</script>

<style scoped>
::v-deep(.p-inputnumber) {
  .p-inputtext {
    text-align: center;
  }
}
::v-deep(.p-password) {
  .p-inputtext {
    width: inherit;
  }
}
</style>
