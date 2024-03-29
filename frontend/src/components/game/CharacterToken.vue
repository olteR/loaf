<template>
  <div v-if="props.character">
    <img class="w-28" :src="props.image.src" />
  </div>
</template>

<script setup>
const props = defineProps({
  character: Object,
  image: Object,
});
</script>

<style scoped></style>
