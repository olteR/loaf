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
          :image="cardImages[character.id]"
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
});
</script>

<style scoped>
.characters {
  position: absolute;
  height: 20rem;
  width: 50%;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  top: 0.5rem;
}
</style>
