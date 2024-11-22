<template>
  <div class="grid gap-8 grid-cols-3">
    <div
      v-for="player in options.players.filter((p) => p.districts.length > 0)"
      :key="player.id"
      class="mt-1"
      style="width: 12vw"
    >
      <div
        class="w-full outline"
        style="line-height: 4.5vh; outline: 'solid medium'; border-radius: 4px"
      >
        <div class="px-2 text-center">
          {{ `${player.name} kerületei:` }}
        </div>
        <div class="inline-flex justify-evenly w-full">
          <GameModalBuiltDistrict
            v-for="(district, ind) in player.districts"
            :key="ind"
            :card="district"
            :image="districtImages[district.id - 1]"
            :selected="selectedPlayer === player.id && selectedDistrict === ind"
            :selectable="isSelectable(player, district)"
            @click="toggle(player, district, ind)"
          ></GameModalBuiltDistrict>
        </div>
      </div>
    </div>
    <div class="col-span-3 ml-auto">
      <Button
        :disabled="selectedPlayer == null || selectedDistrict == null"
        @click="submit"
      >
        Kiválasztás
      </Button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ABILITY } from "@/utils/const";
import Button from "primevue/button";
import GameModalBuiltDistrict from "@/components/game/modals/GameModalBuiltDistrict.vue";

const emit = defineEmits(["submit"]);

const props = defineProps({
  options: Object,
  ability: Object,
  districtImages: Array,
});

const selectedPlayer = ref();
const selectedDistrict = ref();

function isSelectable(player, district) {
  if (props.ability.enum === ABILITY.WARLORD) {
    return district.cost <= props.options.gold + 1 && district.id !== 26;
  }
}

function toggle(player, district, index) {
  if (isSelectable(player, district)) {
    if (
      selectedPlayer.value === player.id &&
      selectedDistrict.value === index
    ) {
      selectedPlayer.value = null;
      selectedDistrict.value = null;
    } else {
      selectedPlayer.value = player.id;
      selectedDistrict.value = index;
    }
  }
}

function submit() {
  emit(
    "submit",
    { id: selectedPlayer.value, index: selectedDistrict.value },
    props.ability
  );
}
</script>

<style scoped></style>
