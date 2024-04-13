<template>
  <div class="members">
    <Card
      v-for="(member, i) in playingMembers"
      :key="member.id"
      class="ml-2 mb-1"
      :class="{
        crowned: member.id === props.crownedPlayer,
        'on-turn': member.id === props.currentPlayer,
      }"
    >
      <template #content>
        <div class="text-2xl">
          <span>{{ i + 1 + ". " }}</span>
          <span class="mr-1"
            ><Chip style="color: inherit">
              <i class="fa fa-coins mr-1" />
              {{ member.gold }}
            </Chip></span
          >
          <span class="mr-1"
            ><Chip style="color: inherit">
              <i class="fa fa-hand mr-1" />
              {{ member.handSize }}
            </Chip></span
          >
          <span>{{ member.displayName }}</span>
          <div v-if="member.id === props.crownedPlayer" class="crowned-icon">
            <i class="fa fa-crown" />
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup>
import { computed } from "vue";
import Card from "primevue/card";
import Chip from "primevue/chip";

const props = defineProps({
  members: Array,
  players: Array,
  crownedPlayer: Number,
  currentPlayer: Number,
});

const playingMembers = computed(() => {
  let pm = [];
  props.players?.forEach((p) => {
    let m = props.members?.find((m) => p.userId === m.id);
    if (m) {
      pm.push({
        id: m.id,
        name: m.name,
        displayName: m.displayName,
        currentCharacter: p.currentCharacter,
        districts: p.districts,
        gold: p.gold,
        handSize: p.handSize,
      });
    }
  });
  return pm;
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
.crowned {
  outline: 2px solid #f6e012;
}
.on-turn {
  background: #9fa8da;
  color: #121212;
}
.crowned-icon {
  display: inline;
  position: absolute;
  right: -2.25rem;
  color: #f6e012;
}
::v-deep(.p-card) {
  .p-card-content {
    padding: 0 !important;
  }
}
</style>
