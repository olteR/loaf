<template>
  <div v-if="gameStore.getGame">
    <div class="game-area"></div>
    <MemberList
      :game="gameStore.getGame"
      :character-images="cardStore.getCharacterImages"
      :district-images="cardStore.getDistrictImages"
      :user-id="stateStore.getUser.id"
    ></MemberList>
    <ActionButtons v-if="onTurn"></ActionButtons>
    <CharacterList :game="gameStore.getGame"></CharacterList>
    <div class="annoucement-message">{{ currentMessage }}</div>
    <PlayerHand
      :cards="gameStore.getGame.hand"
      :card-images="cardStore.getDistrictImages"
      :can-build="canBuild"
      @build="(index) => buildDistrict(index)"
    ></PlayerHand>
    <Button
      class="absolute right-2 top-2 min-w-0"
      @click="router.push('/my-games')"
    >
      <i class="fa fa-x"></i>
    </Button>
    <Button
      :disabled="!onTurn || gameStore.getGame.phase !== 'TURN'"
      class="absolute right-2 bottom-2 block rounded-full"
      style="width: 10vw; height: 10vw"
      @click="gameStore.endTurn(lobbyCode)"
    >
      <div
        class="whitespace-nowrap"
        style="font-size: min(1vw, 24px); margin-top: 1vw"
      >
        Kör befejezése
      </div>
      <div style="font-size: min(6vw, 160px); margin-top: -2vw">
        <i class="fa fa-forward"></i>
      </div>
    </Button>
    <Dialog
      :closable="false"
      v-model:visible="isModalOpen"
      modal
      :header="modalHeader"
    >
      <ResourceSelectModal
        v-if="currentModal === GAME_MODAL.RESOURCE"
        @gather="(resource) => gatherResources(resource)"
      />
      <CardSelectModal
        v-else-if="currentModal === GAME_MODAL.CARDS"
        :cards="gameStore.getGame.drawnCards"
        :max-selects="1"
        @select="(cards) => drawCards(cards)"
      />
      <CharacterSelectModal
        v-else-if="currentModal === GAME_MODAL.CHARACTER"
        :game="gameStore.getGame"
        @select="(number) => selectCharacter(number)"
      />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { onBeforeRouteLeave, useRouter } from "vue-router";
import { useToast } from "primevue/usetoast";
import { GAME_MODAL, GAME_PHASE, RESOURCE } from "@/utils/const";

import { useStateStore } from "@/stores/state";
import { useWebsocketStore } from "@/stores/websocket";
import { useCardStore } from "@/stores/cards";
import { useGameStore } from "@/stores/games";

import ActionButtons from "@/components/game/ActionButtons.vue";
import CardSelectModal from "@/components/game/modals/CardSelectModal.vue";
import MemberList from "@/components/game/MemberList.vue";
import PlayerHand from "@/components/game/PlayerHand.vue";
import ResourceSelectModal from "@/components/game/modals/ResourceSelectModal.vue";

import Button from "primevue/button";
import Dialog from "primevue/dialog";
import CharacterSelectModal from "@/components/game/modals/CharacterSelectModal.vue";
import CharacterList from "@/components/game/CharacterList.vue";

const router = useRouter();

const stateStore = useStateStore();
const websocketStore = useWebsocketStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const lobbyCode = router.currentRoute.value.params.code;

const currentModal = ref();

const onTurn = computed(() => {
  return gameStore.getCurrentPlayer.userId === stateStore.getUser.id;
});

const canBuild = computed(() => {
  return onTurn.value && gameStore.getGame.phase === GAME_PHASE.TURN;
});

const isModalOpen = computed(() => {
  return !!currentModal.value;
});

const currentMessage = computed(() => {
  console.log(gameStore.getCurrentPlayer);
  switch (gameStore.getGame.phase) {
    case GAME_PHASE.SELECTION:
      return onTurn.value
        ? "Válassz karaktert!"
        : `${gameStore.getCurrentPlayer.name} választ karaktert`;
    case GAME_PHASE.RESOURCE:
      return onTurn.value
        ? "Gyűjts nyersanyagot!"
        : `A(z) ${
            gameStore.getGame.characters[
              gameStore.getCurrentPlayer.currentCharacter - 1
            ].name
          } (${gameStore.getCurrentPlayer.name}) gyűjt nyersanyagot`;
    case GAME_PHASE.TURN:
      return onTurn.value
        ? "Te vagy körön!"
        : `A(z) ${
            gameStore.getGame.characters[
              gameStore.getCurrentPlayer.currentCharacter - 1
            ].name
          } (${gameStore.getCurrentPlayer.name}) van körön`;
    default:
      return "";
  }
});

const modalHeader = computed(() => {
  switch (currentModal.value) {
    case GAME_MODAL.CHARACTER:
      return "Válassz karaktert!";
    case GAME_MODAL.RESOURCE:
      return "Válassz nyersanyagot!";
    case GAME_MODAL.CARDS:
      return "Válassz kártyát!";
    default:
      return "";
  }
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await gameStore.fetchGame(lobbyCode);
  websocketStore.subscribeToGame();
  stateStore.setLoading(false);
});

onBeforeRouteLeave(() => {
  websocketStore.unsubscribe();
});

watch(
  () => gameStore.getCurrentPlayer,
  (newValue) => {
    if (newValue.userId === stateStore.getUser.id) {
      if (gameStore.getGame.phase === GAME_PHASE.SELECTION) {
        currentModal.value = GAME_MODAL.CHARACTER;
      } else if (
        gameStore.getGame.phase === GAME_PHASE.RESOURCE &&
        onTurn.value
      ) {
        currentModal.value =
          gameStore.getGame.drawnCards.length === 0
            ? GAME_MODAL.RESOURCE
            : GAME_MODAL.CARDS;
      }
    }
  }
);

async function selectCharacter(number) {
  await gameStore.selectCharacter(lobbyCode, number);
  currentModal.value = null;
}

async function gatherResources(resource) {
  await gameStore.gatherResources(lobbyCode, resource);
  if (resource === RESOURCE.CARDS) {
    currentModal.value = GAME_MODAL.CARDS;
  } else {
    currentModal.value = null;
  }
}

async function drawCards(cards) {
  await gameStore.drawCards(lobbyCode, cards);
  currentModal.value = null;
}

async function buildDistrict(index) {
  await gameStore.buildDistrict(lobbyCode, index);
  gameStore.getGame.hand.splice(index, 1);
}
</script>

<style scoped>
.p-card {
  border: 1px solid rgba(255, 255, 255, 0.12);
}
.annoucement-message {
  width: 100%;
  position: absolute;
  margin-top: 5vh;
  top: 22vh;
  text-align: center;
  font-size: min(2vw, 24px);
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
}
.game-area {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-image: url("@/assets/gamebg.jpg");
  background-position: center;
  filter: brightness(30%);
  z-index: -1000;
}
</style>
