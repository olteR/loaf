<template>
  <div class="grid grid-cols-3 gap-x-12 pt-1" style="margin-right: 1.25rem">
    <div
      v-for="(district, ind) in districts"
      :key="ind"
      class="w-full"
      style="margin-right: 1.25rem"
    >
      <GameSettingDistrictCard
        :district="district"
        :selected="toggleValues[ind]"
        :clickable="
          (selectedCount === MAX_UNIQUE_DISTRICTS && toggleValues[ind]) ||
          selectedCount < MAX_UNIQUE_DISTRICTS
        "
        @click="toggle(ind)"
      />
    </div>
    <div class="col-span-3 ml-auto" style="margin-right: -1.25rem">
      <Button
        :disabled="selectedCount !== MAX_UNIQUE_DISTRICTS"
        @click="saveDistricts"
        >Mentés</Button
      >
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { MAX_UNIQUE_DISTRICTS } from "@/utils/const";
import GameSettingDistrictCard from "@/components/lobbies/GameSettingDistrictCard.vue";
import Button from "primevue/button";

const toggleValues = ref([]);

const props = defineProps({
  districts: Array,
  selectedDistricts: Array,
});

const emit = defineEmits(["save"]);

const selectedCount = computed(
  () => toggleValues.value.filter((v) => v).length
);

onMounted(async () => {
  props.districts.forEach((d) =>
    toggleValues.value.push(props.selectedDistricts.includes(d.id))
  );
});

function toggle(index) {
  if (selectedCount.value < MAX_UNIQUE_DISTRICTS || toggleValues.value[index]) {
    toggleValues.value[index] = !toggleValues.value[index];
  }
}

function saveDistricts() {
  emit(
    "save",
    props.districts.filter((_, ind) => toggleValues.value[ind])
  );
}
</script>

<style scoped></style>
