<template>
  <div class="members">
    <Card
      class="ml-2 mb-0.5"
      v-for="(member, i) in props.members"
      :key="member.id"
    >
      <template #content>
        <div class="text-2xl">
          {{ i + 1 + ". " + member.displayName }}
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup>
import Card from "primevue/card";

const props = defineProps({
  members: Array,
  players: Array,
});
</script>

<style scoped>
.members {
  width: fit-content;
  height: fit-content;
  position: absolute;
  top: 0;
  bottom: 0;
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
