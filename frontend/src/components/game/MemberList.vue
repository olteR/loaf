<template>
  <div class="members">
    <Card
      v-for="(player, i) in players"
      :key="player.id"
      class="ml-2 mb-1"
      :class="{
        crowned: player.id === props.crownedPlayer.id,
        'on-turn': player.id === props.currentPlayer.id,
      }"
    >
      <template #content>
        <div class="text-2xl">
          <span>{{ i + 1 + ". " }}</span>
          <span class="mr-1"
            ><Chip style="color: inherit">
              <i class="fa fa-coins mr-1" />
              {{ player.gold }}
            </Chip></span
          >
          <span class="mr-1"
            ><Chip style="color: inherit">
              <i class="fa fa-hand mr-1" />
              {{ player.handSize }}
            </Chip></span
          >
          <span>{{ player.displayName }}</span>
          <div v-if="player.id === props.crownedPlayer.id" class="crowned-icon">
            <i class="fa fa-crown" />
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup>
import Card from "primevue/card";
import Chip from "primevue/chip";

const props = defineProps({
  players: Array,
  crownedPlayer: Object,
  currentPlayer: Object,
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
