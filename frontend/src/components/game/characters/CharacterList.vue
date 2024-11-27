<template>
  <div v-if="game?.characters">
    <div class="characters">
      <div v-for="character in game?.characters" :key="character">
        <CharacterInfo
          :character="character"
          :status="getStatus(character.number)"
          :status-by-one="getOneStatus(character.number)"
          :status-by-two="getTwoStatus(character.number)"
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
  if (props.game?.character === number) return CHAR_STATUS.SELECTED;
  if (props.game?.discardedCharacters?.includes(number))
    return CHAR_STATUS.DISCARDED;
  if (props.game?.unavailableCharacters?.includes(number))
    return CHAR_STATUS.UNAVAILABLE;
  if (props.game?.skippedCharacters?.includes(number))
    return CHAR_STATUS.SKIPPED;
}

function getOneStatus(number) {
  if (props.game?.killedCharacter === number) return CHAR_STATUS.KILLED;
  if (props.game?.bewitchedCharacter === number) return CHAR_STATUS.BEWITCHED;
  if (props.game?.warrantedCharacter === number)
    return CHAR_STATUS.WARRANTED_SIGNED;
  if (props.game?.warrantedCharacters.includes(number))
    return CHAR_STATUS.WARRANTED;
}

function getTwoStatus(number) {
  if (props.game?.robbedCharacter === number) return CHAR_STATUS.ROBBED;
  if (props.game?.threatenedCharacter === number)
    return CHAR_STATUS.THREATENED_REAL;
  if (props.game?.threatenedCharacters.includes(number))
    return CHAR_STATUS.THREATENED;
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
