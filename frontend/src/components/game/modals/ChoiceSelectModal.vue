<template>
  <div class="inline-flex select-none">
    <div
      v-for="choice in options.choices"
      :key="choice.value"
      class="w-40 mx-1"
    >
      <Button
        class="w-full h-40 text-8xl"
        :severity="choice.severity"
        @click="submitChoice(choice.value)"
      >
        <div class="w-full text-center">
          <i
            :class="`fa fa-${choice.icon}`"
            v-tooltip="{
              value: choice.tooltip,
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
