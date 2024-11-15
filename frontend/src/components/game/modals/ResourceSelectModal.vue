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
              value: '2 arany',
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
              value: '2 lap húzása a pakliból, amiből egyet választhatsz',
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

const emit = defineEmits(["gather"]);

function gatherResource(resource) {
  emit("gather", resource);
}
</script>

<style scoped></style>
