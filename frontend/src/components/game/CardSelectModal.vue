<template>
  <div class="grid grid-cols-none gap-x-12 pt-1" style="margin-right: 1.25rem">
    <div
      v-for="district in props.cards"
      :key="district.id"
      class="w-full"
      style="margin-right: 1.25rem"
    >
      <GameSettingDistrictCard :district="district" />
    </div>
  </div>
</template>

<script setup>
import GameSettingDistrictCard from "@/components/lobbies/GameSettingDistrictCard.vue";

const props = defineProps({
  cards: Array,
});
</script>

<style scoped></style>
