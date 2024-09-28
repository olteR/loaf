<template>
  <div v-if="details?.characters">
    <div class="characters">
      <div
        v-for="character in details?.characters"
        :key="character"
        class="inline-flex"
      >
        <CharacterToken
          :character="character"
          :image="cardImages[character.id - 1]"
          :total-characters="details?.characters?.length"
          :status="getStatus(character.number)"
          :can-select="canSelect"
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
  details: Object,
  cardImages: Array,
  canSelect: Boolean,
});
const emit = defineEmits(["select"]);

function getStatus(number) {
  if (number === props.details?.currentPlayer?.currentCharacter)
    return CHAR_STATUS.ON_TURN;
  if (number === props.details?.currentCharacter) return CHAR_STATUS.SELECTED;
  if (props.details?.discardedCharacters?.includes(number))
    return CHAR_STATUS.DISCARDED;
  if (props.details?.unavailableCharacters?.includes(number))
    return CHAR_STATUS.UNAVAILABLE;
  if (props.details?.skippedCharacters?.includes(number))
    return CHAR_STATUS.SKIPPED;
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
