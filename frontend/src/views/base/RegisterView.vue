<template>
  <div class="flex h-screen">
    <div class="m-auto justify-items-center">
      <h1 class="text-4xl text-center select-none">
        új felhasználó létrehozása.
      </h1>
      <form @submit.prevent="registerUser()">
        <span class="p-float-label m-8">
          <InputText
            id="name"
            type="text"
            v-model="userForm.username"
            class="w-full"
            required
          />
          <label for="name">felhasználónév</label>
        </span>
        <span class="p-float-label m-8">
          <InputText
            id="email"
            type="email"
            v-model="userForm.email"
            class="w-full"
            required
          />
          <label for="name">e-mail</label>
        </span>
        <span class="p-float-label m-8">
          <Password
            id="password"
            v-model="userForm.password"
            :feedback="false"
            toggleMask
            class="w-full"
            required
          />
          <label for="password">jelszó</label>
        </span>
        <div class="grid grid-cols-1 sm:grid-cols-2">
          <div class="mx-8 mb-4">
            <Button
              label="vissza a bejelentkezéshez"
              class="p-button-danger w-full sm:w-auto"
              icon="pi pi-chevron-left"
              @click="router.push('/login')"
            />
          </div>
          <div class="mx-8">
            <Button
              type="submit"
              label="regisztráció"
              class="float-right p-button-success w-full sm:w-auto"
            />
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useStateStore } from "@/stores/state";
import { useUserStore } from "@/stores/users";
import { useRouter } from "vue-router";
import InputText from "primevue/inputtext";
import Password from "primevue/password";
import Button from "primevue/button";

const router = useRouter();
const stateStore = useStateStore();
const userStore = useUserStore();
const userForm = ref({
  username: null,
  email: null,
  password: null,
});

onMounted(() => {
  if (stateStore.isLoggedIn) {
    router.push("/lobbies");
  }
});

async function registerUser() {
  await userStore.registerUser(userForm.value);
  await router.push("/login");
}
</script>

<style scoped>
::v-deep(.p-password) {
  .p-inputtext {
    width: inherit;
  }
}
</style>
