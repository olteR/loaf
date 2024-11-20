<template>
  <div class="grid gap-x-8 pt-3">
    <div
      v-for="(district, ind) in options.cards"
      :key="district.id"
      class="w-full"
    >
      <GameModalDistrictCard
        :district="district"
        :selected="toggleValues[ind]"
        :clickable="isClickable(ind)"
        @click="toggle(ind)"
      />
    </div>
    <div class="col-span-3 ml-auto">
      <Button :disabled="selectedCount !== options.selectCount" @click="select"
        >Kiválasztás</Button
      >
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import Button from "primevue/button";
import GameModalDistrictCard from "@/components/game/modals/GameModalDistrictCard.vue";

const emit = defineEmits(["submit"]);

const props = defineProps({
  options: Object,
  ability: Object,
});

const toggleValues = ref([]);

const selectedCount = computed(
  () => toggleValues.value.filter((v) => v).length
);

onMounted(() => {
  toggleValues.value = props.options.cards.map(() => false);
});

function toggle(index) {
  if (props.options.cards.length > 2) {
    if (
      selectedCount.value < props.options.selectCount ||
      toggleValues.value[index]
    ) {
      toggleValues.value[index] = !toggleValues.value[index];
    }
  } else {
    if (
      toggleValues.value.every((val) => val === false) ||
      toggleValues.value[index]
    ) {
      toggleValues.value[index] = !toggleValues.value[index];
    } else {
      toggleValues.value.reverse();
    }
  }
}

function isClickable(index) {
  return props.options.cards.length > 2
    ? (selectedCount.value === props.options.selectCount &&
        toggleValues[index]) ||
        selectedCount.value < props.options.selectCount
    : true;
}

function select() {
  emit(
    "submit",
    toggleValues.value
      .map((val, ind) => (val ? ind : null))
      .filter((val) => val !== null),
    props.ability
  );
}
</script>

<style scoped></style>
