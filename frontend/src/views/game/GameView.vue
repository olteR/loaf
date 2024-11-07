<template>
  <div class="game-area"></div>
  <Card class="m-2" style="width: 10vw; font-size: min(1.5vw, 32px)">
    <template #content>
      <div class="columns-2 text-center select-none">
        <div>
          <i class="fa fa-coins"></i> {{ gameStore.getGameDetails?.gold }}
        </div>
        <div><i class="fa fa-star"></i> 0</div>
      </div>
    </template>
  </Card>
  <MemberList
    :players="gameStore.getGameDetails?.players"
    :crownedPlayer="gameStore.getGameDetails?.crownedPlayer"
    :currentPlayer="gameStore.getGameDetails?.currentPlayer"
  ></MemberList>
  <ActionButtons v-if="onTurn"></ActionButtons>
  <CharacterList
    :details="gameStore.getGameDetails"
    :card-images="cardStore.getCharacterImages"
    :can-select="canSelect"
    @select="(number) => gameStore.selectCharacter(lobbyCode, number)"
  ></CharacterList>
  <div class="annoucement-message">{{ currentMessage }}</div>
  <PlayerHand
    :cards="cardStore.getCards.districts"
    :card-images="cardStore.getDistrictImages"
    :can-build="canBuild"
    @build="(cost, cardIndex) => buildDistrict(cost, cardIndex)"
  ></PlayerHand>
  <Button class="absolute right-2 top-2" @click="router.push('/my-games')"
    >Játék bezárása</Button
  >
  <Dialog
    :closable="false"
    v-model:visible="currentModal"
    modal
    :header="modalHeader"
  >
    <ResourceSelectModal
      v-if="currentModal === GAME_MODAL.RESOURCE"
      @gather="(resource) => gatherResources(resource)"
    />
    <CardSelectModal
      v-else-if="currentModal === GAME_MODAL.CARDS"
      :cards="gameStore.getGameDetails?.drawnCards"
    />
  </Dialog>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";
import { useRouter } from "vue-router";

import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import { useGameStore } from "@/stores/games";

import CharacterList from "@/components/game/CharacterList.vue";
import MemberList from "@/components/game/MemberList.vue";
import PlayerHand from "@/components/game/PlayerHand.vue";
import ResourceSelectModal from "@/components/game/ResourceSelectModal.vue";

import Button from "primevue/button";
import Card from "primevue/card";
import Dialog from "primevue/dialog";
import { GAME_MODAL, GAME_PHASE, GAME_UPDATE, RESOURCE } from "@/utils/const";
import CardSelectModal from "@/components/game/CardSelectModal.vue";
import ActionButtons from "@/components/game/ActionButtons.vue";
import { useToast } from "primevue/usetoast";

const router = useRouter();
const toast = useToast();

const stateStore = useStateStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const lobbyCode = router.currentRoute.value.params.code;

const socket = ref();
const stompClient = ref();
const currentModal = ref();

const connected = ref(false);

const onTurn = computed(() => {
  return (
    gameStore.getGameDetails?.currentPlayer.userId === stateStore.getUser.id
  );
});

const canSelect = computed(() => {
  return (
    onTurn.value && gameStore.getGameDetails?.phase === GAME_PHASE.SELECTION
  );
});

const canBuild = computed(() => {
  return onTurn.value && gameStore.getGameDetails?.phase === GAME_PHASE.TURN;
});

const currentMessage = computed(() => {
  switch (gameStore.getGameDetails?.phase) {
    case GAME_PHASE.SELECTION:
      return onTurn.value
        ? "Válassz karaktert!"
        : gameStore.getGameDetails?.currentPlayer.displayName +
            " választ karaktert.";
    case GAME_PHASE.RESOURCE:
      return onTurn.value
        ? "Gyűjts nyersanyagot!"
        : gameStore.getGameDetails?.currentPlayer.displayName +
            " gyűjt nyersanyagot.";
    case GAME_PHASE.TURN:
      return onTurn.value
        ? "Te vagy körön!"
        : "A(z) " +
            gameStore.getGameDetails?.characters[
              gameStore.getGameDetails?.currentPlayer.currentCharacter - 1
            ].name +
            " (" +
            gameStore.getGameDetails?.currentPlayer.displayName +
            ") van körön.";
    default:
      return "";
  }
});

const modalHeader = computed(() => {
  switch (currentModal.value) {
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
  await gameStore.fetchGameDetails(lobbyCode);
  if (gameStore.getGameDetails?.phase === GAME_PHASE.RESOURCE) {
    currentModal.value =
      gameStore.getGameDetails?.drawnCards.length === 0
        ? GAME_MODAL.RESOURCE
        : GAME_MODAL.CARDS;
  }
  connect();
  stateStore.setLoading(false);
});

function handleGameUpdate(update) {
  switch (update.type) {
    case GAME_UPDATE.NEXT_PLAYER: {
      gameStore.getGameDetails.currentPlayer =
        gameStore.getGameDetails?.players.find((p) => p.id === update.change);
      break;
    }
    case GAME_UPDATE.PLAYER_TURN: {
      gameStore.getGameDetails.currentPlayer = stateStore.getUser.id;
      gameStore.getGameDetails.unavailableCharacters = update.change;
      break;
    }
    default: {
      console.log(update);
    }
  }
}

async function gatherResources(resource) {
  await gameStore.gatherResources(lobbyCode, resource);
  if (resource === RESOURCE.CARDS) {
    currentModal.value = GAME_MODAL.CARDS;
  } else {
    currentModal.value = null;
  }
}

async function buildDistrict(cost, cardIndex) {
  if (cost < gameStore.getGameDetails.gold) {
    toast.add({
      severity: "warn",
      summary: "Hiányzó arany",
      detail: "Nincs elég aranyad, hogy megépítsd a kerületet!",
      life: 3000,
    });
  } else {
    await gameStore.buildDistrict(lobbyCode, cardIndex);
  }
}

function connect() {
  if (!connected.value) {
    socket.value = new SockJS("http://localhost:3000/ws?" + stateStore.getJwt);
    stompClient.value = Stomp.over(socket);
    stompClient.value.connect({}, connectCallback, errorCallback);
  }
}

const connectCallback = function (frame) {
  console.log("Connected!");
  connected.value = true;
  console.log(frame);
  stompClient.value.subscribe("/user/topic/game/update", (msg) => {
    handleGameUpdate(JSON.parse(msg.body));
  });
};

const errorCallback = function (error) {
  console.log(error);
  connected.value = false;
  console.log("Reconnecting...");
  setTimeout(connect, 5000);
};
</script>

<style scoped>
.p-card {
  border: 1px solid rgba(255, 255, 255, 0.12);
}
.annoucement-message {
  width: 100%;
  position: absolute;
  top: 25%;
  text-align: center;
  font-size: xx-large;
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
