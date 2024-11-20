<template>
  <div v-if="gameStore.getGame">
    <div class="game-area"></div>
    <MemberList
      :game="gameStore.getGame"
      :character-images="cardStore.getCharacterImages"
      :district-images="cardStore.getDistrictImages"
      :user-id="stateStore.getUser.id"
    ></MemberList>
    <ActionButtons
      :abilities="visibleAbilities"
      :used-abilities="gameStore.getGame.usedAbilities"
      :on-turn="onTurn"
      @use-ability="(ability) => useAbility(ability)"
    ></ActionButtons>
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
      :closable="modalSettings.ability"
      v-model:visible="modalSettings.visible"
      modal
      :header="modalHeader"
    >
      <ResourceSelectModal
        v-if="modalSettings.type === GAME_MODAL.RESOURCE"
        @submit="(target) => modalSettings.onSubmit(target)"
      />
      <CardSelectModal
        v-else-if="modalSettings.type === GAME_MODAL.CARDS"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
      <CharacterSelectModal
        v-else-if="modalSettings.type === GAME_MODAL.CHARACTER"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
      <PlayerSelectModal
        v-else-if="modalSettings.type === GAME_MODAL.PLAYER"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { onBeforeRouteLeave, useRouter } from "vue-router";
import {
  ABILITY_TARGET,
  ABILITY_TYPE,
  CONDITIONS,
  GAME_MODAL,
  GAME_PHASE,
  RESOURCE,
} from "@/utils/const";

import { useStateStore } from "@/stores/state";
import { useWebsocketStore } from "@/stores/websocket";
import { useCardStore } from "@/stores/cards";
import { useGameStore } from "@/stores/games";

import ActionButtons from "@/components/game/ActionButtons.vue";
import CardSelectModal from "@/components/game/modals/CardSelectModal.vue";
import MemberList from "@/components/game/members/MemberList.vue";
import PlayerHand from "@/components/game/hand/PlayerHand.vue";
import ResourceSelectModal from "@/components/game/modals/ResourceSelectModal.vue";

import Button from "primevue/button";
import Dialog from "primevue/dialog";
import CharacterSelectModal from "@/components/game/modals/CharacterSelectModal.vue";
import CharacterList from "@/components/game/characters/CharacterList.vue";
import { hasCondition } from "@/utils/utils";
import PlayerSelectModal from "@/components/game/modals/PlayerSelectModal.vue";

const router = useRouter();

const stateStore = useStateStore();
const websocketStore = useWebsocketStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const lobbyCode = router.currentRoute.value.params.code;

const modalSettings = ref({
  visible: false,
  type: null,
  onSubmit: null,
  options: {},
  ability: null,
});

const onTurn = computed(() => {
  return gameStore.getCurrentPlayer.userId === stateStore.getUser.id;
});

const canBuild = computed(() => {
  return onTurn.value && gameStore.getGame.phase === GAME_PHASE.TURN;
});

const currentMessage = computed(() => {
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
              gameStore.getCurrentPlayer.character - 1
            ].name
          } (${gameStore.getCurrentPlayer.name}) gyűjt nyersanyagot`;
    case GAME_PHASE.TURN:
      return onTurn.value
        ? "Te vagy körön!"
        : `A(z) ${
            gameStore.getGame.characters[
              gameStore.getCurrentPlayer.character - 1
            ].name
          } (${gameStore.getCurrentPlayer.name}) van körön`;
    default:
      return "";
  }
});

const modalHeader = computed(() => {
  switch (modalSettings.value.type) {
    case GAME_MODAL.CHARACTER:
      return "Válassz karaktert!";
    case GAME_MODAL.RESOURCE:
      return "Válassz nyersanyagot!";
    case GAME_MODAL.CARDS:
      return "Válassz kártyát!";
    default:
      return "Válassz!";
  }
});

const visibleAbilities = computed(() => {
  const characterAbilities =
    gameStore.getCharacter?.abilities
      .filter((ability) => ability.type === ABILITY_TYPE.MANUAL)
      .map((ability) => {
        ability.sourceName = gameStore.getCharacter.name;
        ability.sourceType = gameStore.getCharacter.type;
        return ability;
      }) ?? [];
  const districtAbilities =
    gameStore.getGame.players
      .find((player) => player.userId === stateStore.getUser.id)
      .districts.map((district) =>
        district.abilities.map((ability) => {
          ability.sourceName = district.name;
          ability.sourceType = district.type;
          return ability;
        })
      )
      .flat(Infinity)
      .filter((ability) => ability.type === ABILITY_TYPE.AFTER_BUILD) ?? [];
  const handAbilities =
    gameStore.getGame.hand
      .map((district) =>
        district.abilities.map((ability) => {
          ability.sourceName = district.name;
          ability.sourceType = district.type;
          return ability;
        })
      )
      .flat(Infinity)
      .filter((ability) => ability.type === ABILITY_TYPE.BEFORE_BUILD) ?? [];
  return characterAbilities.concat(districtAbilities).concat(handAbilities);
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await gameStore.fetchGame(lobbyCode);
  if (gameStore.getGame.phase === GAME_PHASE.NOT_STARTED) {
    router.push(`/lobby/${lobbyCode}`);
  }
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
        openModal(GAME_MODAL.CHARACTER, selectCharacter, {
          characters: gameStore.getGame.characters,
          unavailable: gameStore.getGame.unavailableCharacters,
          discarded: gameStore.getGame.discardedCharacters,
        });
      } else if (
        gameStore.getGame.phase === GAME_PHASE.RESOURCE &&
        onTurn.value
      ) {
        if (gameStore.getGame.drawnCards.length === 0) {
          openModal(GAME_MODAL.RESOURCE, gatherResources);
        } else {
          openModal(GAME_MODAL.CARDS, drawCards, {
            cards: gameStore.getGame.drawnCards,
            selectCount: hasCondition(
              gameStore.getCurrentPlayer,
              CONDITIONS.KNOWLEDGE
            )
              ? 2
              : 1,
          });
        }
      }
    }
  }
);

function openModal(type, submit, options = {}) {
  modalSettings.value.visible = true;
  modalSettings.value.type = type;
  modalSettings.value.onSubmit = submit;
  modalSettings.value.options = options;
}

function closeModal() {
  modalSettings.value.visible = false;
  modalSettings.value.type = null;
  modalSettings.value.ability = null;
}

async function selectCharacter(number) {
  await gameStore.selectCharacter(lobbyCode, number);
  closeModal();
}

async function gatherResources(resource) {
  const response = await gameStore.gatherResources(lobbyCode, resource);
  if (resource === RESOURCE.CARDS) {
    const hasKnowledge = hasCondition(
      gameStore.getCurrentPlayer,
      CONDITIONS.KNOWLEDGE
    );
    if (
      hasKnowledge &&
      !hasCondition(gameStore.getCurrentPlayer, CONDITIONS.STAR_GUIDANCE)
    ) {
      gameStore.getCurrentPlayer.hand =
        gameStore.getCurrentPlayer.hand.concat(response);
    } else {
      openModal(GAME_MODAL.CARDS, drawCards, {
        cards: response,
        selectCount: hasKnowledge ? 2 : 1,
      });
    }
  } else {
    closeModal();
  }
}

async function drawCards(cards) {
  await gameStore.drawCards(lobbyCode, cards);
  closeModal();
}

async function buildDistrict(index) {
  await gameStore.buildDistrict(lobbyCode, index);
  gameStore.getGame.hand.splice(index, 1);
}

async function useAbility(ability) {
  if (ability.target === ABILITY_TARGET.NONE) {
    await gameStore.useAbility({
      ability: ability.enum,
      code: lobbyCode,
    });
  } else {
    modalSettings.value.ability = ability;
    switch (ability.target) {
      case ABILITY_TARGET.CHARACTER:
        openModal(GAME_MODAL.CHARACTER, useTargetedAbility, {
          characters: gameStore.getGame.characters,
          discarded: gameStore.getGame.discardedCharacters,
          untargetable: gameStore.getGame.characters
            .filter(
              (character) =>
                character.number <= gameStore.getCurrentPlayer.character
            )
            .map((character) => character.number),
        });
        break;
      case ABILITY_TARGET.PLAYER:
        openModal(GAME_MODAL.PLAYER, useTargetedAbility, {
          players: gameStore.getGame.players.filter(
            (player) => player.id !== gameStore.getGame.currentPlayer
          ),
        });
        break;
      default:
        console.log(ability);
    }
  }
}

async function useTargetedAbility(target, ability) {
  switch (ability.target) {
    case ABILITY_TARGET.CHARACTER:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target: {
          index: target,
        },
      });
      break;
    case ABILITY_TARGET.PLAYER:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target: {
          id: target,
        },
      });
      break;
    default:
      console.log(ability);
  }
  closeModal();
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
