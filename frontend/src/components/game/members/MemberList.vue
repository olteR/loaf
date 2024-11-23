<template>
  <div class="members">
    <Card
      v-for="(player, i) in game.players"
      :key="player.order"
      style="width: 12vw; outline: solid medium; border-radius: 4px"
      :class="{
        'on-turn': player.id === game.currentPlayer,
      }"
    >
      <template #content>
        <MemberCard
          :player="player"
          :order="i + 1"
          :image="
            characterImages[game.characters[player.character - 1]?.id - 1]
          "
          :is-player="player.userId === userId"
          :game-phase="game.phase"
          :tooltip-position="tooltipPosition(i)"
          :districtImages="districtImages"
        />
      </template>
    </Card>
  </div>
</template>

<script setup>
import Card from "primevue/card";
import MemberCard from "@/components/game/members/MemberCard.vue";

const props = defineProps({
  game: Object,
  characterImages: Array,
  districtImages: Array,
  userId: Number,
});

function tooltipPosition(playerIndex) {
  if (playerIndex < Math.floor(props.game.players.length / 2)) {
    return { position: "right" };
  } else if (playerIndex >= Math.ceil(props.game.players.length / 2)) {
    return { position: "left" };
  }
  return { position: "bottom" };
}
</script>

<style scoped>
.members {
  position: absolute;
  display: inline-flex;
  justify-content: space-evenly;
  width: 91vw;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  top: 1vh;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  z-index: 12;
}

.on-turn {
  color: #9fa8da;
  -webkit-transition: transform 0.2s;
  -moz-transition: transform 0.2s;
  -ms-transition: transform 0.2s;

  z-index: 11;
  --webkit-transform: scale(1.05);
  --moz-transform: scale(1.05);
  --o-transform: scale(1.05);
  --ms-transform: scale(1.05);
  transform: scale(1.05);
}

::v-deep(.p-card) {
  .p-card-body {
    padding: 0 !important;
  }
  .p-card-content {
    padding: 0 !important;
  }
}
</style>
