<template>
  <div class="members">
    <Card
      v-for="(player, i) in game.players"
      :key="player.id"
      style="width: 12vw; outline: solid medium; border-radius: 4px"
      :class="{
        player: player.userId === props.userId,
        'on-turn': player.id === game.currentPlayer,
      }"
    >
      <template #content>
        <div class="card-div">
          <div class="order-number">
            <template v-if="game.phase === 'SELECTION'">
              {{ i + 1 }}
            </template>
            <template v-else-if="player.currentCharacter">
              <img
                class="character-image"
                :src="
                  characterImages[
                    game.characters[player.currentCharacter - 1].id - 1
                  ].src
                "
              />
            </template>
            <template v-else>?</template>
          </div>
          <div
            class="font-bold"
            style="
              height: 3vh;
              margin-left: 4vw;
              font-size: 1.7vh;
              overflow: hidden;
            "
          >
            {{ player.name }}
            <template v-if="player.userId === userId">
              <i
                class="fa fa-user ml-1"
                v-tooltip:[tooltipPosition(i)]="{
                  value: `A te karakterlapod`,
                  escape: false,
                }"
              ></i>
            </template>
            <template v-if="player.id === game.crownedPlayer">
              <i
                class="fa fa-crown ml-1"
                v-tooltip:[tooltipPosition(i)]="{
                  value: `Ennél a játékosnál van a korona`,
                  escape: false,
                }"
              ></i>
            </template>
          </div>
          <div
            class="inline-flex align-middle justify-between"
            style="
              height: 4vh;
              margin-left: 3.5vw;
              font-size: 2.5vh;
              width: 8vw;
            "
          >
            <Tag
              class="my-auto"
              style="width: 2.5vw; height: 4vh; font-size: 1.5vh"
              v-tooltip:[tooltipPosition(i)]="{
                value: `${player.name} kincstartalékában ${player.gold} arany van`,
                escape: false,
              }"
            >
              {{ player.gold }}
              <i class="fa fa-coins" style="margin-left: 0.2vh"></i>
            </Tag>
            <Tag
              class="my-auto"
              style="width: 2.5vw; height: 4vh; font-size: 1.5vh"
              v-tooltip:[tooltipPosition(i)]="{
                value: `${player.name} kezében ${player.handSize} lap van`,
                escape: false,
              }"
            >
              {{ player.handSize }}
              <i class="fa fa-sheet-plastic" style="margin-left: 0.2vh"></i>
            </Tag>
            <Tag
              class="my-auto"
              style="width: 2.5vw; height: 4vh; font-size: 1.5vh"
              v-tooltip:[tooltipPosition(i)]="{
                value: `${player.name} kerületei ${calculatePoints(
                  player
                )} pontot érnek`,
                escape: false,
              }"
            >
              {{ calculatePoints(player) }}
              <i class="fa fa-star" style="margin-left: 0.2vh"></i>
            </Tag>
          </div>
          <div
            class="inline-flex w-full align-middle"
            style="height: 4vh; margin-top: 1vh; font-size: 2.5vh"
          >
            <div
              v-for="condition in player.conditions"
              :key="condition.value"
              style="margin-left: 0.5vw"
            >
              <i
                :class="`fa fa-${condition.icon}`"
                v-tooltip:[tooltipPosition(i)]="{
                  value: `<p>${condition.name}</p>${condition.description}`,
                  escape: false,
                }"
              ></i>
            </div>
          </div>
          <div class="districts-outer">
            <div class="districts">
              <BuiltDistrict
                v-for="(card, j) in player.districts.slice(0, 5)"
                :key="j"
                :card="card"
                :image="districtImages[card.id - 1]"
                :tooltip-position="tooltipPosition(i)"
              ></BuiltDistrict>
            </div>
            <div class="districts">
              <BuiltDistrict
                v-for="(card, j) in player.districts.slice(5, 10)"
                :key="j"
                :card="card"
                :image="districtImages[card.id - 1]"
                :tooltip-position="tooltipPosition(i)"
              ></BuiltDistrict>
            </div>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup>
import BuiltDistrict from "@/components/game/BuiltDistrict.vue";
import Card from "primevue/card";
import Tag from "primevue/tag";

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

function calculatePoints(player) {
  return player.districts.reduce(
    (partialSum, district) => partialSum + district.cost,
    0
  );
}
</script>

<style scoped>
.card-div {
  height: 24vh;
}

.character-image {
  border-radius: 0 9999px 9999px 9999px;
  width: 3vw;
  height: 7vh;
  object-fit: cover;
}

.order-number {
  position: absolute;
  z-index: 100;
  width: 3vw;
  height: 7vh;
  outline: solid thick;
  border-radius: 4px 9999px 9999px 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 7vh;
  font-size: 3vh;
  font-weight: 700;
  background: rgba(18, 18, 18, 0.87);
}

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

.districts-outer {
  height: 12vh;
  display: inline-flex;
  justify-content: space-around;
  flex-direction: column;
}

.districts {
  position: relative;
  display: inline-flex;
  justify-content: space-between;
  margin-left: 0.5vw;
  margin-right: 0.5vw;
  width: 11vw;
  height: 5.5vh;
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
