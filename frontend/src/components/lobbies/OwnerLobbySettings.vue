<template>
  <div class="container mx-auto my-4">
    <Card v-if="props.settings">
      <template #title><h1 class="text-4xl">Játék beállításai</h1></template>
      <template #content>
        <form>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label for="crownedPlayerSelect">
                <span class="text-2xl">Koronával kezdő játékos</span>
              </label>
              <Dropdown
                id="crownedPlayerSelect"
                v-model="selectedCrowned"
                :options="playerSelect"
                optionLabel="displayName"
                class="w-full mt-1"
              ></Dropdown>
            </div>
            <div class="col-span-2">
              <span class="text-2xl">Karakterek</span>
            </div>
            <div class="col-span-2">
              <span class="text-2xl">Egyedi kerületek</span>
            </div>
          </div>
        </form>
      </template>
    </Card>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import Card from "primevue/card";
import Dropdown from "primevue/dropdown";

const props = defineProps({
  settings: Object,
  players: Array,
  districts: Array,
});

const selectedCrowned = ref();

const playerSelect = computed(() => {
  let list = JSON.parse(JSON.stringify(props.players));
  list.unshift({ displayName: "Véletlenszerű" });
  return list;
});

onMounted(() => {
  selectedCrowned.value = props.settings.crownedPlayer || playerSelect.value[0];
});
</script>

<style scoped></style>
