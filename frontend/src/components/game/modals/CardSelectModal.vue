<template>
  <div class="inline-flex">
    <button
      :disabled="page === 0"
      class="text-4xl"
      style="width: 48px"
      @click="page--"
    >
      <i
        class="fa fa-chevron-left"
        :class="{ 'text-transparent': page === 0 }"
      ></i>
    </button>

    <div
      class="grid gap-x-8 pt-4"
      :class="`grid-cols-${Math.min(displayedCards.length, 4)}`"
    >
      <div
        v-for="(district, ind) in displayedCards"
        :key="district.id"
        class="w-full"
      >
        <GameModalDistrictCard
          :district="district"
          :image="districtImages[district.id - 1]"
          :selected="toggleValues[page * 8 + ind] || options.maxSelect === 0"
          :clickable="isClickable(page * 8 + ind)"
          @click="toggle(page * 8 + ind)"
        />
      </div>
      <div
        class="ml-auto"
        :class="`col-span-${Math.min(displayedCards.length, 4)}`"
      >
        <Button
          :disabled="
            selectedCount < options.minSelect ||
            selectedCount > options.maxSelect
          "
          @click="select"
        >
          {{ options.maxSelect > 0 ? "Kiválasztás" : "Tovább" }}
        </Button>
      </div>
    </div>
    <button
      :disabled="
        page === Math.floor(options.cards.length / 8) ||
        options.cards.length < 9
      "
      class="text-4xl"
      style="width: 48px"
      @click="page++"
    >
      <i
        class="fa fa-chevron-right"
        :class="{
          'text-transparent':
            page === Math.floor(options.cards.length / 8) ||
            options.cards.length < 9,
        }"
      ></i>
    </button>
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
  districtImages: Array,
});

const toggleValues = ref([]);
const page = ref(0);

const selectedCount = computed(
  () => toggleValues.value.filter((v) => v).length
);

const displayedCards = computed(() => {
  return props.options.cards.slice(page.value * 8, (page.value + 1) * 8);
});

onMounted(() => {
  toggleValues.value = props.options.cards.map(() => false);
});

function toggle(index) {
  if (props.options.maxSelect === 1) {
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
      selectedCount.value < props.options.maxSelect ||
      toggleValues.value[index]
    ) {
      toggleValues.value[index] = !toggleValues.value[index];
    }
  }
}

function isClickable(index) {
  if (
    props.options.maxSelect === 1 ||
    selectedCount.value < props.options.maxSelect
  ) {
    return true;
  }
  return toggleValues.value[index];
}

function select() {
  emit(
    "submit",
    toggleValues.value
      .map((val, ind) => (val ? ind : null))
      .filter((val) => val !== null),
    props.ability
  );
  toggleValues.value = props.options.cards.map(() => false);
}
</script>

<style scoped></style>
