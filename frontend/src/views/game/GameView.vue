<template>
  <div v-if="gameStore.getGame.phase">
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
      :closable="!!modalSettings.ability"
      v-model:visible="modalSettings.visible"
      modal
      :header="modalHeader"
      @hide="closeModal"
    >
      <ResourceSelectModal
        v-if="modalSettings.type === GAME_MODAL.RESOURCE"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
      <CardSelectModal
        v-else-if="modalSettings.type === GAME_MODAL.CARDS"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        :district-images="cardStore.getDistrictImages"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
      <CharacterSelectModal
        v-else-if="modalSettings.type === GAME_MODAL.CHARACTER"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        :character-images="cardStore.getCharacterImages"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
      <PlayerSelectModal
        v-else-if="modalSettings.type === GAME_MODAL.PLAYER"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
      <DistrictSelectModal
        v-else-if="modalSettings.type === GAME_MODAL.DISTRICT"
        :options="modalSettings.options"
        :ability="modalSettings.ability"
        :district-images="cardStore.getDistrictImages"
        @submit="(target, ability) => modalSettings.onSubmit(target, ability)"
      />
    </Dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { onBeforeRouteLeave, useRouter } from "vue-router";
import {
  ABILITY,
  ABILITY_TARGET,
  ABILITY_TYPE,
  CONDITIONS,
  GAME_MODAL,
  GAME_PHASE,
  RESOURCE,
  RESOURCE_CARDS,
  RESOURCE_GOLD,
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
import DistrictSelectModal from "@/components/game/modals/DistrictSelectModal.vue";
import { useToast } from "primevue/usetoast";

const router = useRouter();
const toast = useToast();

const stateStore = useStateStore();
const websocketStore = useWebsocketStore();
const cardStore = useCardStore();
const gameStore = useGameStore();
const lobbyCode = router.currentRoute.value.params.code;

const modalSettings = ref({
  visible: false,
  type: null,
  header: null,
  onSubmit: null,
  options: {},
  ability: null,
});

const modalChain = ref([]);
const targetBuffer = ref({});

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
        : `${gameStore.getCurrentPlayer?.name} választ karaktert`;
    case GAME_PHASE.RESOURCE:
      return onTurn.value
        ? "Gyűjts nyersanyagot!"
        : `A(z) ${
            gameStore.getGame.characters[
              gameStore.getCurrentPlayer.character - 1
            ]?.name
          } (${gameStore.getCurrentPlayer.name}) gyűjt nyersanyagot`;
    case GAME_PHASE.TURN:
      return onTurn.value
        ? "Te vagy körön!"
        : `A(z) ${
            gameStore.getGame.characters[
              gameStore.getCurrentPlayer.character - 1
            ]?.name
          } (${gameStore.getCurrentPlayer.name}) van körön`;
    default:
      return "";
  }
});

const modalHeader = computed(() => {
  if (modalSettings.value.header) return modalSettings.value.header;
  switch (modalSettings.value.type) {
    case GAME_MODAL.CHARACTER:
      return "Válassz karaktert!";
    case GAME_MODAL.RESOURCE:
      return "Válassz nyersanyagot!";
    case GAME_MODAL.CARDS:
      return `Válassz ${
        modalSettings.value.options.maxSelect > 1 ? "kártyákat" : "kártyát"
      }!`;
    case GAME_MODAL.PLAYER:
      return "Válassz játékost!";
    case GAME_MODAL.DISTRICT:
      return "Válassz kerületet!";
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
        modalChain.value.push({
          type: GAME_MODAL.CHARACTER,
          submit: selectCharacter,
          options: {
            characters: gameStore.getGame.characters,
            unavailable: gameStore.getGame.unavailableCharacters,
            discarded: gameStore.getGame.discardedCharacters,
          },
        });
      } else if (gameStore.getGame.phase === GAME_PHASE.RESOURCE) {
        const isGoldMining = hasCondition(
          gameStore.getCurrentPlayer,
          CONDITIONS.GOLD_MINING
        );
        const hasStarGuidance = hasCondition(
          gameStore.getCurrentPlayer,
          CONDITIONS.STAR_GUIDANCE
        );
        const hasKnowledge = hasCondition(
          gameStore.getCurrentPlayer,
          CONDITIONS.KNOWLEDGE
        );
        const selectCount = hasKnowledge ? 2 : 1;
        if (gameStore.getGame.drawnCards.length === 0) {
          modalChain.value.push({
            type: GAME_MODAL.RESOURCE,
            submit: gatherResources,
            options: {
              gold: isGoldMining ? RESOURCE_GOLD + 1 : RESOURCE_GOLD,
              cards: hasStarGuidance ? RESOURCE_CARDS + 1 : RESOURCE_CARDS,
              cardsToKeep: selectCount,
            },
          });
        } else {
          modalChain.value.push({
            type: GAME_MODAL.CARDS,
            submit: drawCards,
            options: {
              cards: gameStore.getGame.drawnCards,
              minSelect: selectCount,
              maxSelect: selectCount,
            },
          });
        }
      }
      openNextModal();
    }
  }
);

watch(
  () => gameStore.getGame.usingAbility,
  (newValue) => {
    if (newValue) {
      switch (newValue) {
        case ABILITY.SCHOLAR:
          modalSettings.value.ability = gameStore.getCharacter.abilities.find(
            (ability) => ability.enum === ABILITY.SCHOLAR
          );
          modalChain.value.push({
            type: GAME_MODAL.CARDS,
            submit: (target, ability) =>
              useTargetedAbility({ index: target[0] }, ability),
            options: {
              cards: gameStore.getGame.drawnCards,
              minSelect: 1,
              maxSelect: 1,
            },
          });
          break;
      }
      openNextModal();
    }
  }
);

function openNextModal(target) {
  if (target) {
    targetBuffer.value = { ...target, ...targetBuffer.value };
    console.log(targetBuffer.value);
  }
  if (modalChain.value.length > 0) {
    const modal = modalChain.value.shift();
    modalSettings.value.visible = true;
    modalSettings.value.header = modal.header;
    modalSettings.value.type = modal.type;
    modalSettings.value.onSubmit = modal.submit;
    modalSettings.value.options = modal.options;
    if (
      modalSettings.value.ability?.enum === ABILITY.DIPLOMAT &&
      targetBuffer.value.index != null
    ) {
      modalSettings.value.options.maxCost +=
        gameStore.getCurrentPlayer.districts[targetBuffer.value.index].cost;
    }
  } else {
    closeModal();
  }
}

function closeModal() {
  modalSettings.value.visible = false;
  modalSettings.value.type = null;
  modalSettings.value.ability = null;
  modalSettings.value.header = null;
  targetBuffer.value = {};
  modalChain.value = [];
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
      gameStore.getGame.hand = gameStore.getGame.hand.concat(response.data);
      closeModal();
    } else {
      modalChain.value.push({
        type: GAME_MODAL.CARDS,
        submit: drawCards,
        options: {
          cards: response.data,
          minSelect: hasKnowledge ? 2 : 1,
          maxSelect: hasKnowledge ? 2 : 1,
        },
      });
      openNextModal();
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
        modalChain.value.push({
          type: GAME_MODAL.CHARACTER,
          submit: useTargetedAbility,
          options: {
            characters: gameStore.getGame.characters,
            discarded: gameStore.getGame.discardedCharacters,
            untargetable: gameStore.getGame.characters
              .filter(
                (character) =>
                  character.number <= gameStore.getCurrentPlayer.character ||
                  character.number === gameStore.getGame.killedCharacter ||
                  character.number === gameStore.getGame.bewitchedCharacter
              )
              .map((character) => character.number),
          },
        });
        break;
      case ABILITY_TARGET.PLAYER:
        modalChain.value.push({
          type: GAME_MODAL.PLAYER,
          submit: useTargetedAbility,
          options: {
            players: gameStore.getGame.players.filter(
              (player) => player.id !== gameStore.getGame.currentPlayer
            ),
          },
        });
        break;
      case ABILITY_TARGET.PLAYER_AND_RESOURCE:
        modalChain.value.push({
          type: GAME_MODAL.PLAYER,
          submit: (target) => openNextModal({ id: target }),
          options: {
            players: gameStore.getGame.players.filter(
              (player) =>
                player.id !== gameStore.getGame.currentPlayer &&
                !hasCondition(player, CONDITIONS.CROWNED)
            ),
          },
        });
        modalChain.value.push({
          type: GAME_MODAL.RESOURCE,
          submit: useTargetedAbility,
          options: {
            gold: 1,
            cards: 1,
            cardsToKeep: 1,
          },
        });
        break;
      case ABILITY_TARGET.OWN_CARD:
        if (gameStore.getGame.hand > 0) {
          modalChain.value.push({
            type: GAME_MODAL.CARDS,
            submit: useTargetedAbility,
            options: {
              cards: gameStore.getGame.hand,
              minSelect: 1,
              maxSelect: 1,
            },
          });
        } else {
          toast.add({
            severity: "error",
            summary: "Nincs lapod!",
            detail:
              "Ezt a képességet nem használhatod úgy, hogy nincs lap a kezedben!",
            life: 3000,
          });
        }
        break;
      case ABILITY_TARGET.OWN_CARDS:
        if (gameStore.getGame.hand > 0) {
          modalChain.value.push({
            type: GAME_MODAL.CARDS,
            submit: useTargetedAbility,
            options: {
              cards: gameStore.getGame.hand,
              minSelect: 1,
              maxSelect: gameStore.getGame.hand.length,
            },
          });
        } else {
          toast.add({
            severity: "error",
            summary: "Nincs lapod!",
            detail:
              "Ezt a képességet nem használhatod úgy, hogy nincs lap a kezedben!",
            life: 3000,
          });
        }
        break;
      case ABILITY_TARGET.BUILT_DISTRICT:
        modalChain.value.push({
          type: GAME_MODAL.DISTRICT,
          submit: useTargetedAbility,
          options: {
            players: gameStore.getGame.players,
            maxCost: gameStore.getCurrentPlayer.gold + 1,
          },
        });
        break;
      case ABILITY_TARGET.SWAP_DISTRICT:
        if (gameStore.getCurrentPlayer.districts.length > 0) {
          modalChain.value.push({
            header: "Válassz saját kerületet!",
            type: GAME_MODAL.DISTRICT,
            submit: openNextModal,
            options: {
              players: [gameStore.getCurrentPlayer],
              isOwn: true,
            },
          });
          modalChain.value.push({
            header: "Válassz cserélendő kerületet!",
            type: GAME_MODAL.DISTRICT,
            submit: useTargetedAbility,
            options: {
              players: gameStore.getGame.players.filter(
                (player) => player.id !== gameStore.getGame.currentPlayer
              ),
              maxCost: gameStore.getCurrentPlayer.gold,
              unselectableDistricts: gameStore.getCurrentPlayer.districts.map(
                (district) => district.id
              ),
            },
          });
        } else {
          toast.add({
            severity: "error",
            summary: "Nincs kerületed!",
            detail:
              "Ezt a képességet nem használhatod úgy, hogy nincs kerületed építve!",
            life: 3000,
          });
        }
        break;
      case ABILITY_TARGET.OTHERS_BUILT_DISTRICT:
        modalChain.value.push({
          type: GAME_MODAL.DISTRICT,
          submit: useTargetedAbility,
          options: {
            players: gameStore.getGame.players.filter(
              (player) => player.id !== gameStore.getGame.currentPlayer
            ),
            maxCost: Math.min(gameStore.getCurrentPlayer.gold, 3),
            districts: gameStore.getCurrentPlayer.districts.map(
              (district) => district.id
            ),
          },
        });
        break;
      case ABILITY_TARGET.GOLD_OR_CARDS:
        modalChain.value.push({
          type: GAME_MODAL.RESOURCE,
          submit: useTargetedAbility,
          options: {
            gold: 4,
            cards: 4,
            cardsToKeep: 4,
          },
        });
        break;
      case ABILITY_TARGET.SELECTOR:
        await gameStore.useAbility({
          ability: ability.enum,
          code: lobbyCode,
        });
        modalChain.value.push({
          type: GAME_MODAL.CARDS,
          submit: useTargetedAbility,
          options: {
            cards: gameStore.getGame.drawnCards,
            minSelect: 1,
            maxSelect: 1,
          },
        });
        break;
      default:
        console.log(ability);
    }
    openNextModal();
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
    case ABILITY_TARGET.PLAYER_AND_RESOURCE:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target: { resource: target, ...targetBuffer.value },
      });
      break;
    case ABILITY_TARGET.OWN_CARD:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target: {
          index: target[0],
        },
      });
      break;
    case ABILITY_TARGET.OWN_CARDS:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target: {
          indexes: target,
        },
      });
      break;
    case ABILITY_TARGET.BUILT_DISTRICT:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target,
      });
      break;
    case ABILITY_TARGET.SWAP_DISTRICT:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target: {
          secondaryId: target.id,
          secondaryIndex: target.index,
          ...targetBuffer.value,
        },
      });
      break;
    case ABILITY_TARGET.OTHERS_BUILT_DISTRICT:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target,
      });
      break;
    case ABILITY_TARGET.GOLD_OR_CARDS:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target: {
          resource: target,
        },
      });
      break;
    case ABILITY_TARGET.SELECTOR:
      await gameStore.useAbility({
        ability: ability.enum,
        code: lobbyCode,
        target,
      });
      break;
    default:
      console.log(target, ability);
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
