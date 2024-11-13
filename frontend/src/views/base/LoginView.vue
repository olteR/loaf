<template>
  <div class="flex h-screen">
    <div class="m-auto justify-items-center">
      <h1 class="text-9xl text-center select-none">loaf.</h1>
      <form @submit.prevent="loginUser()">
        <span class="p-float-label m-8">
          <InputText
            id="name"
            type="text"
            v-model="name"
            class="w-full"
            required
          />
          <label for="name">Felhasználónév</label>
        </span>
        <span class="p-float-label m-8">
          <Password
            id="password"
            v-model="password"
            :feedback="false"
            toggleMask
            class="w-full"
            required
          />
          <label for="password">Jelszó</label>
        </span>
        <Button
          type="submit"
          label="Bejelentkezés"
          class="p-button-primary ml-8"
        />
        <Button
          label="Regisztráció"
          class="float-right p-button-secondary mr-8"
          @click="router.push('/register')"
        />
      </form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import SockJS from "sockjs-client/dist/sockjs";
import { useStateStore } from "@/stores/state";
import { useWebsocketStore } from "@/stores/websocket";
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import Password from "primevue/password";

const router = useRouter();
const stateStore = useStateStore();
const websocketStore = useWebsocketStore();
const name = ref();
const password = ref();

onMounted(() => {
  if (stateStore.isLoggedIn) {
    router.push("/lobbies");
  }
});

async function loginUser() {
  await stateStore.loginUser({ name: name.value, password: password.value });
  websocketStore.connect(
    new SockJS(`${window.location.origin}/ws?${stateStore.getJwt}`)
  );
}
</script>
