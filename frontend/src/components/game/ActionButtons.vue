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
            :key="ability.enum"
            class="ability-div"
            :class="{ hoverable: canUseAbility(ability) }"
            :style="{
              'outline-color': primaryColorOrDisabled(
                ability.sourceType,
                canUseAbility(ability)
              ),
              'background-color': secondaryColorOrDisabled(
                ability.sourceType,
                canUseAbility(ability)
              ),
              color: primaryColorOrDisabled(
                ability.sourceType,
                canUseAbility(ability)
              ),
            }"
            v-tooltip="{
              value: `${ability.description}<p>Forrás: ${
                ability.sourceName
              }</p>${
                isAbilityUsed(ability)
                  ? '<p><b>Ezt a képességet már használtad</b></p>'
                  : ''
              }`,
              escape: false,
            }"
            @click="useAbility(ability)"
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
import {
  primaryColorOrDisabled,
  secondaryColorOrDisabled,
} from "@/utils/utils";

const emit = defineEmits(["use-ability"]);
const props = defineProps({
  abilities: Array,
  usedAbilities: Array,
  onTurn: Boolean,
});

function isAbilityUsed(ability) {
  return props.usedAbilities.includes(ability.enum);
}

function canUseAbility(ability) {
  return props.onTurn && !isAbilityUsed(ability);
}

function useAbility(ability) {
  if (canUseAbility(ability)) {
    emit("use-ability", ability.enum);
  }
}
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
