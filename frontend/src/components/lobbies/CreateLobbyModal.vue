<template>
  <div class="grid gap-x-8 gap-y-4 grid-cols-2 p-1">
    <div>
      <div class="grid grid-cols-8">
        <SelectButton
          v-model="lobbyForm.hidden"
          :options="hiddenOptions"
          option-value="value"
          option-label="label"
          class="col-span-7 grid grid-cols-2"
        />
        <i
          v-tooltip.top="
            'A rejtett lobbi nem fog látszani a nyilvános listában, csak linkkel lehet belépni.'
          "
          class="fa fa-info-circle m-auto"
        ></i>
      </div>
    </div>
    <div>
      <div class="grid grid-cols-8">
        <SelectButton
          v-model="lobbyForm.secured"
          :options="securedOptions"
          option-value="value"
          option-label="label"
          class="col-span-7 grid grid-cols-2"
        />
        <i
          v-tooltip.top="
            'A védett lobbihoz történő belépéshez a létrehozáskor megadott jelszó szükséges.'
          "
          class="fa fa-info-circle m-auto"
        ></i>
      </div>
    </div>
    <div class="p-float-label">
      <InputText id="name" v-model="lobbyForm.name" class="w-full"></InputText>
      <label for="name">név</label>
    </div>
    <div class="p-float-label">
      <Password
        id="password"
        v-model="lobbyForm.password"
        :disabled="!lobbyForm.secured"
        :feedback="false"
        toggleMask
        class="w-full"
      ></Password>
      <label for="password">jelszó</label>
    </div>
    <div class="grid grid-cols-1 col-span-2 mb-2">
      <label for="maxMembers" class="text-center">maximum játékosok</label>
      <InputNumber
        v-model="lobbyForm.maxMembers"
        id="maxMembers"
        :min="3"
        :max="9"
        showButtons
        buttonLayout="horizontal"
        decrementButtonClass="primary"
        incrementButtonClass="primary"
        incrementButtonIcon="pi pi-plus"
        decrementButtonIcon="pi pi-minus"
      />
    </div>
    <div>
      <Button
        class="p-button-danger"
        label="mégsem"
        icon="pi pi-times"
        :disabled="loading"
        @click="dialogRef.close()"
      ></Button>
    </div>
    <div>
      <Button
        class="float-right p-button-success"
        label="létrehozás"
        icon="pi pi-check"
        :loading="loading"
        @click="createLobby()"
      ></Button>
    </div>
  </div>
</template>

<script setup>
import { inject, ref } from "vue";
import { useLobbyStore } from "@/stores/lobbies";
import Button from "primevue/button";
import InputNumber from "primevue/inputnumber";
import InputText from "primevue/inputtext";
import Password from "primevue/password";
import SelectButton from "primevue/selectbutton";

const lobbyStore = useLobbyStore();
const dialogRef = inject("dialogRef");
const loading = ref(false);

const lobbyForm = ref({
  name: null,
  password: null,
  hidden: false,
  secured: false,
  maxMembers: 8,
});

const hiddenOptions = ref([
  {
    label: "nyílvános",
    value: false,
  },
  {
    label: "rejtett",
    value: true,
  },
]);

const securedOptions = ref([
  {
    label: "nyitott",
    value: false,
  },
  {
    label: "védett",
    value: true,
  },
]);

async function createLobby() {
  loading.value = true;
  await lobbyStore.createLobby(lobbyForm.value);
  loading.value = false;
  dialogRef.value.close();
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