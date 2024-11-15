<template>
  <div class="members">
    <Card
      v-for="(player, i) in game.players"
      :key="player.id"
      class="ml-2 mb-2"
      style="width: 15vw; outline: solid medium; border-radius: 4px"
      :class="{
        player: player.userId === props.userId,
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
                :style="{
                  width: '6vh',
                }"
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
              margin-left: 8vh;
              font-size: 1.7vh;
              overflow: hidden;
            "
          >
            {{ player.name }}
          </div>
          <div
            class="inline-flex w-full align-middle"
            style="height: 4vh; margin-left: 8vh; font-size: 2.5vh"
          >
            <Tag class="my-auto mr-2" style="width: 3vh; height: 3vh"
              >{{ player.gold }}
              <i class="fa fa-coins" style="margin-left: 0.2vh"></i
            ></Tag>
            <div
              v-for="condition in player.conditions"
              :key="condition.value"
              class="mr-2"
            >
              <i
                :class="`fa fa-${condition.icon}`"
                v-tooltip="{
                  value: `<p>${condition.name}</p>${condition.description}`,
                  escape: false,
                }"
              ></i>
            </div>
          </div>
          <div class="districts">
            <BuiltDistrict
              v-for="(card, i) in game.hand"
              :key="i"
              :card="card"
              :image="districtImages[card.id - 1]"
            ></BuiltDistrict>
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
</script>

<style scoped>
.card-div {
  height: 13vh;
}

.character-image {
  border-radius: 9999px;
}

.order-number {
  position: absolute;
  z-index: 100;
  width: 6vh;
  height: 6vh;
  outline: solid thick;
  border-radius: 4px 9999px 9999px 9999px;
  text-align: center;
  vertical-align: middle;
  line-height: 6vh;
  font-size: 3vh;
  font-weight: 700;
  background: rgba(18, 18, 18, 0.87);
}

.members {
  width: fit-content;
  height: fit-content;
  position: absolute;
  top: 0;
  bottom: 0;
  margin-top: auto;
  margin-bottom: auto;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  z-index: 12;
}

.districts {
  position: absolute;
  display: inline-flex;
  width: 100%;
  height: 12vh;
}

.player {
  color: #9fa8da;
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
