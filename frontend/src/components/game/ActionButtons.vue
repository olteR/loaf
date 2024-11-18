<template>
  <div v-if="abilities.length > 0" class="actions">
    <Card class="ml-2 mb-1 text-center">
      <template #content>
        <div class="mb-2" style="font-size: 1vw">Képességek</div>
        <div
          class="grid gap-4"
          :class="`grid-cols-${Math.min(Math.ceil(abilities.length / 2), 5)}`"
        >
          <div
            v-for="ability in abilities"
            :key="ability.id"
            class="ability-div hoverable"
            :style="{
              'outline-color': COLORS[ability.sourceType ?? 'DEFAULT'].PRIMARY,
              'background-color':
                COLORS[ability.sourceType ?? 'DEFAULT'].SECONDARY,
            }"
            v-tooltip="{
              value: `${ability.description}<p>Forrás: ${ability.sourceName}</p>`,
              escape: false,
            }"
          >
            <div class="flex flex-row justify-evenly">
              <div v-for="(icon, i) in ability.icons" :key="i">
                <i class="fa" :class="`fa-${icon}`"></i>
              </div>
            </div>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup>
import Card from "primevue/card";
import { COLORS } from "@/utils/const";

const props = defineProps({
  abilities: Array,
  onTurn: Boolean,
});
</script>

<style scoped>
.actions {
  width: fit-content;
  height: fit-content;
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  margin-top: auto;
  margin-bottom: auto;
  user-select: none;
}
.ability-div {
  width: 6vw;
  font-size: 1.5vw;
  outline: medium solid;
  border-radius: 4px;
}
::v-deep(.p-card) {
  .p-card-body {
    padding-top: 0.25rem;
  }
  .p-card-content {
    padding: 0 !important;
  }
}
</style>
