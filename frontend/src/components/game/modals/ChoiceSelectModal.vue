<template>
  <div class="columns-2 select-none">
    <div class="w-40">
      <Button
        class="w-full h-40 text-8xl"
        severity="success"
        @click="submitChoice(true)"
      >
        <div class="w-full text-center">
          <i
            :class="`fa fa-${options.yesIcon}`"
            v-tooltip="{
              value: options.yesTooltip,
              escape: false,
            }"
          ></i>
        </div>
      </Button>
    </div>
    <div class="w-40">
      <Button
        class="w-full h-40 text-8xl"
        severity="danger"
        @click="submitChoice(false)"
      >
        <div class="w-full text-center">
          <i
            :class="`fa fa-${options.noIcon}`"
            v-tooltip="{
              value: options.noTooltip,
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

const emit = defineEmits(["submit"]);
const props = defineProps({
  options: Object,
  ability: Object,
});

function submitChoice(choice) {
  emit("submit", choice, props.ability);
}
</script>

<style scoped></style>
