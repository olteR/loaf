<template>
  <div class="grid grid-cols-3 gap-x-12 pt-2" style="margin-right: 1.25rem">
    <div
      v-for="character in sortedCharacters"
      :key="character.id"
      class="w-full"
      style="margin-right: 1.25rem"
    >
      <GameSettingCharacterCard
        :character="character"
        :selected="toggleValues[character.id]"
        @click="toggle(character.id)"
      />
    </div>
    <div class="col-span-3 ml-auto" style="margin-right: -1.25rem">
      <Button @click="saveCharacters">Mentés</Button>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { DISABLED_CHARACTERS } from "@/utils/const";
import { sortCharacters } from "@/utils/utils";
import GameSettingCharacterCard from "@/components/lobbies/GameSettingCharacterCard.vue";
import Button from "primevue/button";

const toggleValues = ref({});

const emit = defineEmits(["save"]);

const props = defineProps({
  characters: Array,
  selectedCharacters: Array,
});

const sortedCharacters = computed(() => {
  let chars = props.characters.filter(
    (char) => !DISABLED_CHARACTERS.includes(char.id)
  );
  return chars.sort(sortCharacters);
});

onMounted(async () => {
  props.characters.forEach(
    (c) => (toggleValues.value[c.id] = props.selectedCharacters.includes(c.id))
  );
});

function toggle(id) {
  const char = props.characters.find((c) => c.id === id);
  const currChar = props.characters.find(
    (c) => c.number === char.number && toggleValues.value[c.id]
  );
  if (char.number === 9 && (!currChar || char.id === currChar.id)) {
    toggleValues.value[id] = !toggleValues.value[id];
  } else {
    toggleValues.value[currChar.id] = false;
    toggleValues.value[id] = true;
  }
}

function saveCharacters() {
  emit(
    "save",
    props.characters.filter((c) => toggleValues.value[c.id])
  );
}
</script>

<style scoped></style>
