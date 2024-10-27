<template>
  <div class="grid grid-cols-3 gap-x-12 pt-1" style="margin-right: 1.25rem">
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
import Button from "primevue/button";
import { computed, onMounted, ref } from "vue";
import GameSettingCharacterCard from "@/components/lobbies/GameSettingCharacterCard.vue";

const toggleValues = ref({});

const emit = defineEmits(["save"]);

const props = defineProps({
  characters: Array,
  selectedCharacters: Array,
});

const sortedCharacters = computed(() => {
  let chars = props.characters;
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

function sortCharacters(a, b) {
  if (a.number < b.number) {
    return -1;
  }
  if (b.number < a.number) {
    return 1;
  }
  if (a.id < b.id) {
    return -1;
  }
  if (b.id < a.id) {
    return 1;
  }
  return 0;
}
</script>

<style scoped></style>
