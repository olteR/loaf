<template>
  <div class="grid gap-x-8 pt-4 grid-cols-4">
    <div
      v-for="character in props.game.characters"
      :key="character.id"
      class="w-full"
    >
      <GameModalCharacterCard
        :character="character"
        :selected="selectedCharacter === character.number"
        :unavailable="game.unavailableCharacters.includes(character.number)"
        :discarded="game.discardedCharacters.includes(character.number)"
        @select="selectedCharacter = character.number"
      />
    </div>
    <div class="col-span-4 ml-auto">
      <Button :disabled="!selectedCharacter" @click="submit">
        Kiválasztás
      </Button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Button from "primevue/button";
import GameModalCharacterCard from "@/components/game/modals/GameModalCharacterCard.vue";

const emit = defineEmits(["select"]);

const props = defineProps({
  game: Object,
});

const selectedCharacter = ref();

function submit() {
  emit("select", selectedCharacter.value);
}
</script>

<style scoped></style>
