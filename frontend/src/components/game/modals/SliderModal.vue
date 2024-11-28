<template>
  <div>
    <div class="inline-flex select-none">
      <div class="w-40 text-8xl text-center">
        <div>
          <i class="fa fa-coins"></i>
        </div>
        <div class="text-base">
          {{ options.resourceCount - cardAmount }} arany
        </div>
      </div>
      <div class="w-40 my-auto">
        <Slider :max="options.resourceCount" v-model="cardAmount"></Slider>
      </div>
      <div class="w-40 text-8xl text-center">
        <div>
          <i class="fa fa-sheet-plastic"></i>
        </div>
        <div class="text-base">{{ cardAmount }} lap</div>
      </div>
    </div>
    <div class="flex justify-end mt-4">
      <Button @click="submit"> Kiválasztás </Button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Slider from "primevue/slider";
import Button from "primevue/button";

const emit = defineEmits(["submit"]);
const props = defineProps({
  options: Object,
  ability: Object,
});

const cardAmount = ref(0);

function submit() {
  emit(
    "submit",
    {
      gold: props.options.resourceCount - cardAmount.value,
      cards: cardAmount.value,
    },
    props.ability
  );
}
</script>

<style scoped></style>
