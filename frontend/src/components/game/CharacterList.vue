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
          @select="(number) => emit('select', number)"
        ></CharacterToken>
      </div>
    </div>
  </div>
</template>

<script setup>
import CharacterToken from "@/components/game/CharacterToken.vue";
import { CHAR_STATUS } from "@/utils/const";

const props = defineProps({
  characters: Array,
  cardImages: Array,
  selected: Number,
  discarded: Array,
  unavailable: Array,
  skipped: Array,
  currentCharacter: Number,
});
const emit = defineEmits(["select"]);

function getStatus(number) {
  if (number === props.currentCharacter) return CHAR_STATUS.ON_TURN;
  if (number === props.selected) return CHAR_STATUS.SELECTED;
  if (props.discarded?.includes(number)) return CHAR_STATUS.DISCARDED;
  if (props.unavailable?.includes(number)) return CHAR_STATUS.UNAVAILABLE;
  if (props.skipped?.includes(number)) return CHAR_STATUS.SKIPPED;
  return CHAR_STATUS.SELECTABLE;
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
