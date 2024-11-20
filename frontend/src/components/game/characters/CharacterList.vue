<template>
  <div v-if="game?.characters">
    <div class="characters">
      <div v-for="character in game?.characters" :key="character">
        <CharacterInfo
          :character="character"
          :status="getStatus(character.number)"
        ></CharacterInfo>
      </div>
    </div>
  </div>
</template>

<script setup>
import CharacterInfo from "@/components/game/characters/CharacterInfo.vue";
import { CHAR_STATUS } from "@/utils/const";

const props = defineProps({
  game: Object,
});

function getStatus(number) {
  if (number === props.game?.character) return CHAR_STATUS.SELECTED;
  if (props.game?.discardedCharacters?.includes(number))
    return CHAR_STATUS.DISCARDED;
  if (props.game?.unavailableCharacters?.includes(number))
    return CHAR_STATUS.UNAVAILABLE;
  if (props.game?.skippedCharacters?.includes(number))
    return CHAR_STATUS.SKIPPED;
}
</script>

<style scoped>
.characters {
  position: absolute;
  height: 70vh;
  width: 12vw;
  margin-top: auto;
  left: 0.5rem;
  top: 0;
  bottom: 0;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}
</style>
