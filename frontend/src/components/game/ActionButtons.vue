<template>
  <div class="actions">
    <Card class="ml-2 mb-1">
      <template #content>
        <Button class="block">
          <div class="text-3xl">
            <i class="fa-solid fa-wand-sparkles"></i>
          </div>
          <div>Képesség</div>
        </Button>
      </template>
    </Card>
  </div>
</template>

<script setup>
import Button from "primevue/button";
import Card from "primevue/card";

const props = defineProps({
  players: Array,
  crownedPlayer: Object,
  currentPlayer: Object,
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
::v-deep(.p-card) {
  .p-card-content {
    padding: 0 !important;
  }
}
</style>
