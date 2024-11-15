<template>
  <div class="container mx-auto my-4">
    <Card v-if="props.settings">
      <template #title>
        <div class="inline-flex w-full">
          <h1 class="text-4xl">Játék beállításai</h1>
        </div>
      </template>
      <template #content>
        <form>
          <div class="grid grid-cols-3 gap-12">
            <div>
              <div class="inline-flex w-full">
                <div class="text-2xl mb-4">Karakterek</div>
                <div class="ml-auto mb-4" v-if="isOwner">
                  <Button
                    :loading="loading"
                    @click="characterSettingsVisible = true"
                    >Módosítás</Button
                  >
                </div>
              </div>
              <div class="margin-offset">
                <div v-for="character in settings.characters" :key="character">
                  <GameSettingCharacterCard
                    :character="
                      cards.characters?.find((d) => d.id === character)
                    "
                    selected
                    is-big
                  />
                </div>
              </div>
            </div>
            <div>
              <div class="inline-flex w-full">
                <div class="text-2xl mb-4">Egyedi kerületek</div>
                <div class="ml-auto mb-4" v-if="isOwner">
                  <Button
                    :loading="loading"
                    @click="districtSettingsVisible = true"
                    >Módosítás</Button
                  >
                </div>
              </div>
              <div class="margin-offset">
                <div
                  v-for="district in settings.uniqueDistricts"
                  :key="district"
                >
                  <GameSettingDistrictCard
                    :district="cards.districts?.find((d) => d.id === district)"
                    :selected="true"
                  />
                </div>
              </div>
            </div>
            <div>
              <div class="text-2xl mb-4">Koronával kezdő játékos</div>
              <Dropdown
                id="crownedPlayerSelect"
                v-model="selectedCrowned"
                :options="playerSelect"
                optionLabel="name"
                class="w-full mt-1"
                :disabled="!isOwner"
                @change="(option) => emit('crown', option.value)"
              />
            </div>
          </div>
        </form>
      </template>
    </Card>
    <Dialog
      v-model:visible="characterSettingsVisible"
      modal
      header="Karakterek módosítása"
    >
      <CharacterSettingModal
        :characters="cards.characters"
        :selected-characters="settings.characters"
        @save="(c) => saveCharacters(c)"
      />
    </Dialog>
    <Dialog
      v-model:visible="districtSettingsVisible"
      modal
      header="Egyedi kerületek módosítása"
    >
      <DistrictSettingModal
        :districts="cards.districts.filter((d) => d.type === 'UNIQUE')"
        :selected-districts="settings.uniqueDistricts"
        @save="(d) => saveDistricts(d)"
      />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import Button from "primevue/button";
import Card from "primevue/card";
import Dialog from "primevue/dialog";
import Dropdown from "primevue/dropdown";
import CharacterSettingModal from "@/components/lobbies/CharacterSettingModal.vue";
import DistrictSettingModal from "@/components/lobbies/DistrictSettingModal.vue";
import GameSettingCharacterCard from "@/components/lobbies/GameSettingCharacterCard.vue";
import GameSettingDistrictCard from "@/components/lobbies/GameSettingDistrictCard.vue";

const emit = defineEmits(["characters", "districts", "crown"]);

const props = defineProps({
  isOwner: Boolean,
  settings: Object,
  players: Array,
  cards: Object,
  loading: Boolean,
});

const selectedCrowned = ref();
const characterSettingsVisible = ref(false);
const districtSettingsVisible = ref(false);

const playerSelect = computed(() => {
  let list = JSON.parse(JSON.stringify(props.players || []));
  list.unshift({ name: "Véletlenszerű" });
  return list;
});

onMounted(() => {
  setSelectedCrowned(props.settings.crownedPlayer);
});

watch(
  () => props.settings.crownedPlayer,
  (newValue) => {
    setSelectedCrowned(newValue);
  }
);

function setSelectedCrowned(userId) {
  selectedCrowned.value = userId
    ? props.players?.find((player) => player.id === userId)
    : playerSelect.value[0];
}

function saveDistricts(districts) {
  emit("districts", districts);
  districtSettingsVisible.value = false;
}

function saveCharacters(characters) {
  emit("characters", characters);
  characterSettingsVisible.value = false;
}
</script>

<style scoped>
.margin-offset {
  margin-right: 1.25rem;
}
</style>
