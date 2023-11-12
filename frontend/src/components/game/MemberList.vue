<template>
  <Card class="members">
    <template #content>
      <div v-for="(member, i) in members" :key="member.id" class="text-2xl">
        {{ i + 1 + ". " + member.displayName }}
      </div>
    </template>
  </Card>
</template>

<script setup>
import Card from "primevue/card";

const props = defineProps({
  members: Array,
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
}
</style>
