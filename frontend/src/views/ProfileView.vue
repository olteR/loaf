<template>
  <div class="container mx-auto my-4">
    <Card class="p-4">
      <template #title>
        <span class="text-5xl">profil</span>
      </template>
      <template #content>
        <Button label="kijelentkezés" @click="logoutUser()" />
      </template>
    </Card>
  </div>
</template>

<script setup>
import { useStateStore } from "@/stores/state";
import { useRouter } from "vue-router";
import Button from "primevue/button";
import Card from "primevue/card";

const stateStore = useStateStore();
const router = useRouter();

async function logoutUser() {
  await stateStore.logoutUser();
  await router.push("/login");
}
</script>
