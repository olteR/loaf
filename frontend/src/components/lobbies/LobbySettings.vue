<template>
  <div class="container mx-auto my-4">
    <Card v-if="props.settings">
      <template #title>
        <div class="inline-flex w-full">
          <h1 class="text-4xl">Játék beállításai</h1>
          <div class="ml-auto" v-if="isOwner">
            <Button>Mentés</Button>
          </div>
        </div>
      </template>
      <template #content>
        <form>
          <div class="grid grid-cols-3 gap-12">
            <div>
              <div class="inline-flex w-full">
                <div class="text-2xl mb-4">Karakterek</div>
                <div class="ml-auto mb-4" v-if="isOwner">
                  <Button @click="characterSettingsVisible = true"
                    >Módosítás</Button
                  >
                </div>
              </div>
              <div class="margin-offset">
                <div v-for="character in settings.characters" :key="character">
                  <GameSettingCharacterList
                    :character="
                      cards.characters?.find((d) => d.id === character)
                    "
                  />
                </div>
              </div>
            </div>
            <div>
              <div class="inline-flex w-full">
                <div class="text-2xl mb-4">Egyedi kerületek</div>
                <div class="ml-auto mb-4" v-if="isOwner">
                  <Button @click="districtSettingsVisible = true"
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
                optionLabel="displayName"
                class="w-full mt-1"
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
      <CharacterSettingModal :characters="cards.characters" />
    </Dialog>
    <Dialog
      v-model:visible="districtSettingsVisible"
      modal
      header="Egyedi kerületek módosítása"
    >
      <DistrictSettingModal
        :districts="cards.districts.filter((d) => d.type === 'UNIQUE')"
        :selected-districts="settings.uniqueDistricts"
      />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import Button from "primevue/button";
import Card from "primevue/card";
import Dialog from "primevue/dialog";
import Dropdown from "primevue/dropdown";
import CharacterSettingModal from "@/components/lobbies/CharacterSettingModal.vue";
import DistrictSettingModal from "@/components/lobbies/DistrictSettingModal.vue";
import GameSettingDistrictCard from "@/components/lobbies/GameSettingDistrictCard.vue";
import GameSettingCharacterList from "@/components/lobbies/GameSettingCharacterList.vue";

const props = defineProps({
  isOwner: Boolean,
  settings: Object,
  players: Array,
  cards: Object,
});

const selectedCrowned = ref();
const characterSettingsVisible = ref(false);
const districtSettingsVisible = ref(false);

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

<style scoped>
.margin-offset {
  margin-right: 1.25rem;
}
</style>
