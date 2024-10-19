<template>
  <div class="grid grid-cols-3 gap-x-12 pt-1" style="margin-right: 1.25rem">
    <div
      v-for="(character, ind) in sortedCharacters"
      :key="ind"
      class="w-full"
      style="margin-right: 1.25rem"
    >
      <GameSettingCharacterCard :character="character" />
    </div>
    <div class="col-span-3 ml-auto" style="margin-right: -1.25rem">
      <Button>Mentés</Button>
    </div>
  </div>
</template>

<script setup>
import Button from "primevue/button";
import { computed } from "vue";
import GameSettingCharacterCard from "@/components/lobbies/GameSettingCharacterCard.vue";

const props = defineProps({
  characters: Array,
});

const sortedCharacters = computed(() => {
  let chars = props.characters;
  return chars.sort(sortCharacters);
});

function sortCharacters(a, b) {
  if (a.number < b.number) {
    return -1;
  }
  if (b.number < a.number) {
    return 1;
  }
  if (a.id < b.id) {
    return -1;
  }
  if (b.id < a.id) {
    return 1;
  }
  return 0;
}
</script>

<style scoped></style>
