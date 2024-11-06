<template>
  <div class="container mx-auto my-4">
    <Card class="p-4">
      <template #title>
        <span class="text-5xl">Profil</span>
      </template>
      <template #content>
        <Button label="Kijelentkezés" @click="logoutUser()" />
      </template>
    </Card>
  </div>
</template>

<script setup>
import { useStateStore } from "@/stores/state";
import Button from "primevue/button";
import Card from "primevue/card";

const stateStore = useStateStore();

async function logoutUser() {
  await stateStore.logoutUser();
}
</script>
