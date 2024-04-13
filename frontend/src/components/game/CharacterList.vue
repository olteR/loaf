<template>
  <div v-if="characters">
    <div class="characters">
      <div
        v-for="character in props.characters"
        :key="character"
        class="inline-flex"
      >
        <CharacterToken
          :character="character"
          :image="cardImages[character.id - 1]"
          :total-characters="characters?.length"
          :status="getStatus(character.number)"
          :selected="character.number === props.selected"
          :discarded="props.discarded?.includes(character.number)"
          :unavailable="props.unavailable?.includes(character.number)"
          :skipped="props.skipped?.includes(character.number)"
          @select="(number) => emit('select', number)"
        ></CharacterToken>
      </div>
    </div>
  </div>
</template>

<script setup>
import CharacterToken from "@/components/game/CharacterToken.vue";

const props = defineProps({
  characters: Array,
  cardImages: Array,
  selected: Number,
  discarded: Array,
  unavailable: Array,
  skipped: Array,
});
const emit = defineEmits(["select"]);

function getStatus(number) {
  if (number === props.selected) return "SELECTED";
  if (props.discarded?.includes(number)) return "DISCARDED";
  if (props.unavailable?.includes(number)) return "UNAVAILABLE";
  if (props.skipped?.includes(number)) return "SKIPPED";
  return "DEFAULT";
}
</script>

<style scoped>
.characters {
  position: absolute;
  height: 10rem;
  width: 72rem;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  top: 3rem;
}
</style>
