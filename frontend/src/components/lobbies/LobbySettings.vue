<template>
  <div class="container mx-auto my-4">
    <Card v-if="props.settings">
      <template #title><h1 class="text-4xl">Játék beállításai</h1></template>
      <template #content>
        <form>
          <div class="grid grid-cols-3 gap-12">
            <div>
              <div class="text-2xl mb-4">Karakterek</div>
              <div v-for="character in settings.characters" :key="character">
                <GameSettingCharacterList
                  :character="cards.characters?.find((d) => d.id === character)"
                />
              </div>
            </div>
            <div>
              <div class="text-2xl mb-4">Egyedi kerületek</div>
              <div v-for="district in settings.uniqueCards" :key="district">
                <GameSettingDistrictList
                  :district="cards.districts?.find((d) => d.id === district)"
                />
              </div>
            </div>
            <div>
              <div class="text-2xl mb-4">Koronával kezdő játékos</div>
              <Dropdown
                id="crownedPlayerSelect"
                v-model="selectedCrowned"
                :options="playerSelect"
                optionLabel="displayName"
                class="w-full mt-1"
              />
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
import GameSettingDistrictList from "@/components/lobbies/GameSettingDistrictList.vue";
import GameSettingCharacterList from "@/components/lobbies/GameSettingCharacterList.vue";

const props = defineProps({
  isOwner: Boolean,
  settings: Object,
  players: Array,
  cards: Object,
});

const selectedCrowned = ref();

const playerSelect = computed(() => {
  let list = JSON.parse(JSON.stringify(props.players || []));
  list.unshift({ displayName: "Véletlenszerű" });
  return list;
});

onMounted(() => {
  selectedCrowned.value =
    props.settings?.crownedPlayer || playerSelect.value[0];
});
</script>

<style scoped></style>
