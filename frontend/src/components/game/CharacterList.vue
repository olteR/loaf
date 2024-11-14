<template>
  <div v-if="game?.characters">
    <div class="characters">
      <div
        v-for="character in game?.characters"
        :key="character"
        class="inline-flex justify-between"
      >
        <CharacterToken
          :character="character"
          :image="cardImages[character.id - 1]"
          :total-characters="game?.characters?.length"
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

const emit = defineEmits(["select"]);
const props = defineProps({
  game: Object,
  cardImages: Array,
  canSelect: Boolean,
});

function getStatus(number) {
  if (number === props.game?.currentPlayer?.currentCharacter)
    return CHAR_STATUS.ON_TURN;
  if (number === props.game?.currentCharacter) return CHAR_STATUS.SELECTED;
  if (props.game?.discardedCharacters?.includes(number))
    return CHAR_STATUS.DISCARDED;
  if (props.game?.unavailableCharacters?.includes(number))
    return CHAR_STATUS.UNAVAILABLE;
  if (props.game?.skippedCharacters?.includes(number))
    return CHAR_STATUS.SKIPPED;
  return CHAR_STATUS.SELECTABLE;
}
</script>

<style scoped>
.characters {
  position: absolute;
  width: 64vw;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  top: 5vh;
}
</style>
