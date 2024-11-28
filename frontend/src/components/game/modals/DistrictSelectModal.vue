<template>
  <div
    class="grid gap-8"
    :class="`grid-cols-${Math.min(visiblePlayers.length, 3)}`"
  >
    <div
      v-for="player in visiblePlayers"
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
            :protected="isProtected(player, district)"
            @click="toggle(player, district, ind)"
          ></GameModalBuiltDistrict>
        </div>
      </div>
    </div>
    <div
      class="ml-auto"
      :class="`col-span-${Math.min(visiblePlayers.length, 3)}`"
    >
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
import { computed, ref } from "vue";
import { ABILITY, CONDITIONS, DISTRICTS } from "@/utils/const";
import Button from "primevue/button";
import GameModalBuiltDistrict from "@/components/game/modals/GameModalBuiltDistrict.vue";
import { hasCondition } from "@/utils/utils";

const emit = defineEmits(["submit"]);

const props = defineProps({
  options: Object,
  ability: Object,
  districtImages: Array,
});

const selectedPlayer = ref();
const selectedDistrict = ref();

const isEightCharacter = computed(() => {
  return [ABILITY.WARLORD, ABILITY.DIPLOMAT, ABILITY.MARSHAL].includes(
    props.ability
  );
});

const visiblePlayers = computed(() => {
  if (props.options.isOwn) {
    return props.options.players;
  }
  return props.options.players.filter(
    (p) => p.districts.length > 0 && !p.isFinished
  );
});

function isProtected(player, district) {
  return (
    district.abilities.includes(DISTRICTS.KEEP) ||
    (isEightCharacter.value && hasCondition(player, CONDITIONS.PROTECTED))
  );
}

function isSelectable(player, district) {
  if (
    (isEightCharacter.value && district.id === 26) ||
    (props.ability === ABILITY.MARSHAL &&
      props.options.districts.includes(district.id)) ||
    props.options.unselectableDistricts?.includes(district.id)
  ) {
    return false;
  }
  if (props.options.isOwn) {
    return true;
  }
  return district.cost <= props.options.maxCost ?? Infinity;
}

function toggle(player, district, index) {
  if (isSelectable(player, district) && !isProtected(player, district)) {
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
