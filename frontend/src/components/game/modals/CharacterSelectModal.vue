<template>
  <div class="grid gap-x-8 pt-4 grid-cols-4">
    <div
      v-for="character in options.characters"
      :key="character.id"
      class="w-full"
    >
      <GameModalCharacterCard
        :character="character"
        :image="characterImages[character.id - 1]"
        :selected="selectedCharacter === character.number"
        :unavailable="options.unavailable?.includes(character.number)"
        :discarded="options.discarded?.includes(character.number)"
        :untargetable="options.untargetable?.includes(character.number)"
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

const emit = defineEmits(["submit"]);

const props = defineProps({
  options: Object,
  ability: Object,
  characterImages: Array,
});

const selectedCharacter = ref();

function submit() {
  emit("submit", selectedCharacter.value, props.ability);
}
</script>

<style scoped></style>
