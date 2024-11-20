<template>
  <div class="grid gap-8 pt-4 grid-cols-3">
    <div v-for="player in options.players" :key="player.id" class="w-full">
      <GameModalPlayerCard
        :player="player"
        :tooltip-position="{ position: 'bottom' }"
        :selected="selectedPlayer === player.id"
        @click="selectedPlayer = player.id"
      />
    </div>
    <div class="col-span-3 ml-auto">
      <Button :disabled="!selectedPlayer" @click="submit"> Kiválasztás </Button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Button from "primevue/button";
import GameModalPlayerCard from "@/components/game/modals/GameModalPlayerCard.vue";

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
