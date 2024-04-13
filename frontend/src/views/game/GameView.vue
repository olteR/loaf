<template>
  <div class="game-area"></div>
  <Card class="w-48 text-2xl m-2">
    <template #content>
      <div class="columns-2 text-center select-none">
        <div>
          <i class="fa fa-coins"></i> {{ gameStore.getGameState?.gold }}
        </div>
        <div><i class="fa fa-star"></i> 0</div>
      </div>
    </template>
  </Card>
  <MemberList
    :members="gameStore.getGameDetails?.members"
    :players="gameStore.getGameState?.players"
    :crownedPlayer="gameStore.getGameState?.crownedPlayer"
    :currentPlayer="gameStore.getGameState?.currentPlayer"
  ></MemberList>
  <CharacterList
    :characters="charactersInGame"
    :card-images="cardStore.getCharacterImages"
    :selected="gameStore.getGameState?.currentCharacter"
    :discarded="gameStore.getGameState?.discardedCharacters"
    :unavailable="gameStore.getGameState?.unavailableCharacters"
    :skipped="gameStore.getGameState?.skippedCharacters"
    @select="(number) => gameStore.selectCharacter(lobbyCode, number)"
  ></CharacterList>
  <div class="annoucement-message">{{ currentMessage }}</div>
  <PlayerHand
    :cards="cardsInHand"
    :card-images="cardStore.getDistrictImages"
  ></PlayerHand>
  <Button class="absolute right-2 top-2" @click="router.push('/my-games')"
    >Játék bezárása</Button
  >
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import { useGameStore } from "@/stores/games";
import Button from "primevue/button";
import Card from "primevue/card";
import CharacterList from "@/components/game/CharacterList.vue";
import MemberList from "@/components/game/MemberList.vue";
import PlayerHand from "@/components/game/PlayerHand.vue";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "webstomp-client";

const router = useRouter();
const stateStore = useStateStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const lobbyCode = router.currentRoute.value.params.code;
const socket = ref();
const stompClient = ref();

const connected = ref(false);

const onTurn = computed(() => {
  return gameStore.getGameState?.currentPlayer === stateStore.getUser.id;
});

const cardsInHand = computed(() => {
  let hand = [];
  gameStore.getGameState?.hand.forEach((card) => {
    hand.push(
      cardStore.getCards.districts.find((district) => district.id === card)
    );
  });
  return hand;
});

const charactersInGame = computed(() => {
  let characters = [];
  gameStore.getGameDetails?.characters.forEach((card) => {
    characters.push(
      cardStore.getCards.characters.find((character) => character.id === card)
    );
  });
  return characters;
});

const currentMessage = computed(() => {
  if (gameStore.getGameState?.phase === "SELECTION") {
    return stateStore.getUser.id === gameStore.getGameState?.currentPlayer
      ? "Válassz karaktert!"
      : gameStore.getGameDetails?.members.find(
          (m) => m.id === gameStore.getGameState?.currentPlayer
        ).displayName + " választ karaktert.";
  }
  return "";
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await gameStore.fetchGameDetails(lobbyCode);
  await gameStore.fetchGameState(lobbyCode);
  connect();
  stateStore.setLoading(false);
});

function handleGameUpdate(update) {
  switch (update.type) {
    case "NEXT_PLAYER": {
      gameStore.getGameState.currentPlayer = update.change;
      break;
    }
    case "PLAYER_TURN": {
      gameStore.getGameState.currentPlayer = stateStore.getUser.id;
      gameStore.getGameState.unavailableCharacters = update.change;
      break;
    }
    default: {
      console.log(update);
    }
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
