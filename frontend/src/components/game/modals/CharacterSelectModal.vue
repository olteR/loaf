<template>
  <div class="grid gap-x-8 pt-4 grid-cols-4">
    <div
      v-for="(character, ind) in options.characters"
      :key="character.id"
      class="w-full"
    >
      <GameModalCharacterCard
        :character="character"
        :image="characterImages[character.id - 1]"
        :selected="toggleValues[ind]"
        :unavailable="options.unavailable?.includes(character.number)"
        :discarded="options.discarded?.includes(character.number)"
        :untargetable="options.untargetable?.includes(character.number)"
        @select="toggle(ind)"
      />
    </div>
    <div class="col-span-4 ml-auto">
      <Button :disabled="selectedCount !== options.selectCount" @click="submit">
        Kiválasztás
      </Button>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import Button from "primevue/button";
import GameModalCharacterCard from "@/components/game/modals/GameModalCharacterCard.vue";

const emit = defineEmits(["submit"]);

const props = defineProps({
  options: Object,
  ability: Object,
  characterImages: Array,
});

const toggleValues = ref([]);

const selectedCount = computed(
  () => toggleValues.value.filter((v) => v).length
);

onMounted(() => {
  toggleValues.value = props.options.characters.map(() => false);
});

function toggle(index) {
  if (props.options.selectCount === 1) {
    if (
      toggleValues.value.every((val) => val === false) ||
      toggleValues.value[index]
    ) {
      toggleValues.value[index] = !toggleValues.value[index];
    } else {
      toggleValues.value = toggleValues.value.map((_, ind) => ind === index);
    }
  } else {
    if (
      selectedCount.value < props.options.selectCount ||
      toggleValues.value[index]
    ) {
      toggleValues.value[index] = !toggleValues.value[index];
    }
  }
}

function submit() {
  emit(
    "submit",
    toggleValues.value
      .map((val, ind) => (val ? ind + 1 : null))
      .filter((val) => val !== null),
    props.ability
  );
  toggleValues.value = props.options.characters.map(() => false);
}
</script>

<style scoped></style>
