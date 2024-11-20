<template>
  <div class="grid gap-x-8 pt-4 grid-cols-4">
    <div v-for="player in options.players" :key="player.id" class="w-full">
      {{ player.name }}
    </div>
    <div class="col-span-4 ml-auto">
      <Button :disabled="!selectedPlayer" @click="submit"> Kiválasztás </Button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Button from "primevue/button";

const emit = defineEmits(["submit"]);

const props = defineProps({
  options: Object,
  ability: Object,
});

const selectedPlayer = ref();

function submit() {
  emit("submit", selectedPlayer.value, props.ability);
}
</script>

<style scoped></style>
