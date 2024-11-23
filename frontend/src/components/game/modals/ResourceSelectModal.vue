<template>
  <div class="columns-2 select-none">
    <div class="w-40">
      <Button
        class="w-full h-40 text-8xl"
        @click="gatherResource(RESOURCE.GOLD)"
      >
        <div class="w-full text-center">
          <i
            class="fa fa-coins"
            v-tooltip="{
              value: `${options.gold} arany`,
              escape: false,
            }"
          ></i>
        </div>
      </Button>
    </div>
    <div class="w-40">
      <Button
        class="w-full h-40 text-8xl"
        @click="gatherResource(RESOURCE.CARDS)"
      >
        <div class="w-full text-center">
          <i
            class="fa fa-sheet-plastic"
            v-tooltip="{
              value:
                options.cards === options.cardsToKeep
                  ? `${options.cards} lap húzása`
                  : `${options.cards} lap húzása, amiből ${options.cardsToKeep}-t választhatsz`,
              escape: false,
            }"
          ></i>
        </div>
      </Button>
    </div>
  </div>
</template>

<script setup>
import Button from "primevue/button";
import { RESOURCE } from "@/utils/const";

const emit = defineEmits(["submit"]);
const props = defineProps({
  options: Object,
  ability: Object,
});

function gatherResource(resource) {
  emit("submit", resource, props.ability);
}
</script>

<style scoped></style>
