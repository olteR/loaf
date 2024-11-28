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
      :has-factory="hasDistrict(gameStore.getCurrentPlayer, DISTRICTS.FACTORY)"
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
      :closable="
        !!modalSettings.ability &&
        modalSettings.ability !== ABILITY.WITCH &&
        ![ABILITY.WIZARD, ABILITY.SPY, ABILITY.SEER, ABILITY.PAY_OFF].includes(
          gameStore.getGame.usingAbility
        )
      "
      v-model:visible="modalSettings.visible"
      modal
      :header="modalHeader"
      @hide="closeModal"
    >
      <ChoiceSelectModal
        v-if="modalSettings.type === GAME_MODAL.CHOICE"
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
      <SliderModal
        v-else-if="modalSettings.type === GAME_MODAL.SLIDER"
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
import { useToast } from "primevue/usetoast";
import { hasCondition, hasDistrict } from "@/utils/utils";
import {
  ABILITY,
  ABILITY_TYPE,
  CONDITIONS,
  DISTRICT_TYPE,
  DISTRICTS,
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
import CharacterList from "@/components/game/characters/CharacterList.vue";
import CharacterSelectModal from "@/components/game/modals/CharacterSelectModal.vue";
import ChoiceSelectModal from "@/components/game/modals/ChoiceSelectModal.vue";
import DistrictSelectModal from "@/components/game/modals/DistrictSelectModal.vue";
import MemberList from "@/components/game/members/MemberList.vue";
import PlayerHand from "@/components/game/hand/PlayerHand.vue";
import PlayerSelectModal from "@/components/game/modals/PlayerSelectModal.vue";

import Button from "primevue/button";
import Dialog from "primevue/dialog";
import SliderModal from "@/components/game/modals/SliderModal.vue";

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
  let characterAbilities =
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
  if (
    gameStore.getCharacter?.abilities.find(
      (ability) => ability.enum === ABILITY.WITCH
    ) &&
    gameStore.getGame.bewitchedCharacter
  ) {
    characterAbilities = characterAbilities.concat(
      gameStore.getGame.characters
        .find(
          (character) =>
            character.number === gameStore.getGame.bewitchedCharacter
        )
        .abilities.map((ability) => {
          ability.sourceName = "Megbabonázás";
          return ability;
        })
    );
  }
  return characterAbilities.concat(districtAbilities).concat(handAbilities);
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await gameStore.fetchGame(lobbyCode);
  if (gameStore.getGame.phase === GAME_PHASE.NOT_STARTED) {
    await router.push(`/lobby/${lobbyCode}`);
  } else if (gameStore.getGame.phase === GAME_PHASE.ENDED) {
    await router.push(`/game-results/${lobbyCode}`);
  }
  websocketStore.subscribeToGame();
  stateStore.setLoading(false);
  if (!modalSettings.value.visible && modalChain.value.length > 0) {
    openNextModal();
  }
});

onBeforeRouteLeave(() => {
  websocketStore.unsubscribe();
});

watch(
  () => gameStore.getCurrentPlayer,
  (newValue) => {
    if (newValue.userId === stateStore.getUser.id) {
      if (gameStore.getGame.phase === GAME_PHASE.SELECTION) {
        addCharacterSelectModal();
      } else if (gameStore.getGame.phase === GAME_PHASE.RESOURCE) {
        addResourceSelectModal();
      }
      openNextModal();
    }
  }
);

watch(
  () => gameStore.getGame.phase,
  (newValue) => {
    if (gameStore.getCurrentPlayer.userId === stateStore.getUser.id) {
      if (newValue === GAME_PHASE.SELECTION) {
        addCharacterSelectModal();
      } else if (newValue === GAME_PHASE.RESOURCE) {
        addResourceSelectModal();
      }
      openNextModal();
    }
  }
);

watch(
  () => gameStore.getGame.usingAbility,
  (newValue) => {
    if (newValue && newValue !== ABILITY.SEER) {
      modalSettings.value.ability = newValue;
      switch (newValue) {
        case ABILITY.WITCH:
          modalChain.value.push({
            header: "Válassz karaktert, akit megbabonázol!",
            type: GAME_MODAL.CHARACTER,
            submit: (target) => useTargetedAbility({ index: target }),
            options: {
              characters: gameStore.getGame.characters,
              discarded: gameStore.getGame.discardedCharacters,
              untargetable: gameStore.getGame.characters
                .filter(
                  (character) =>
                    character.number <= gameStore.getCurrentPlayer.character
                )
                .map((character) => character.number),
              selectCount: 1,
            },
          });
          break;
        case ABILITY.SPY:
          modalChain.value.push({
            header: `${
              gameStore.getGame.players.find(
                (player) => player.id === gameStore.getGame.abilityTarget
              ).name
            } kezében lévő lapok`,
            type: GAME_MODAL.CARDS,
            submit: async () =>
              gameStore.useAbility({
                ability: ABILITY.SPY,
                code: lobbyCode,
              }),
            options: {
              cards: gameStore.getGame.drawnCards,
              minSelect: 0,
              maxSelect: 0,
            },
          });
          break;
        case ABILITY.WIZARD:
          modalChain.value.push({
            type: GAME_MODAL.CARDS,
            submit: (target, ability) => {
              if (
                gameStore.getGame.drawnCards[target].cost <=
                gameStore.getCurrentPlayer.gold
              ) {
                modalChain.value.push({
                  type: GAME_MODAL.CHOICE,
                  submit: (t, a) =>
                    useTargetedAbility({ index: target[0], choice: t }, a),
                  options: {
                    choices: [
                      {
                        icon: "hammer",
                        tooltip:
                          "Kifizeted a kerület árát és beépíted a városodba",
                        severity: "success",
                        value: true,
                      },
                      {
                        icon: "hand",
                        tooltip: "Kezedbe veszed a lapot",
                        severity: "danger",
                        value: false,
                      },
                    ],
                  },
                });
                openNextModal({ index: target });
              } else {
                useTargetedAbility({ index: target[0] }, ability);
              }
            },
            options: {
              cards: gameStore.getGame.drawnCards,
              minSelect: 1,
              maxSelect: 1,
            },
          });
          break;
        case ABILITY.PAY_OFF:
          modalChain.value.push({
            type: GAME_MODAL.CHOICE,
            submit: (target, ability) =>
              useTargetedAbility({ choice: target }, ability),
            options: {
              choices: [
                {
                  icon: "coins",
                  tooltip: `Lefizeted a Zsarolót ${Math.floor(
                    gameStore.getCurrentPlayer.gold / 2
                  )} aranyért`,
                  severity: "success",
                  value: true,
                },
                {
                  icon: "x",
                  tooltip:
                    "Nem fizeted le a zsarolót és odaadod neki az összes aranyad, ha valóban téged fenyegetett",
                  severity: "danger",
                  value: false,
                },
              ],
            },
          });
          break;
        case ABILITY.SCHOLAR:
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
    }
  }
);

watch(
  () => gameStore.getGame.abilityTarget,
  (newValue) => {
    if (gameStore.getGame.usingAbility === ABILITY.SEER) {
      modalSettings.value.ability = ABILITY.SEER;
      modalChain.value.push({
        header: `Válaszd ki, hogy ${
          gameStore.getGame.players.find((player) => player.id === newValue)
            .name
        } melyik lapot kapja!`,
        type: GAME_MODAL.CARDS,
        submit: (target, ability) => {
          useTargetedAbility({ id: newValue, index: target[0] }, ability);
        },
        options: {
          cards: gameStore.getGame.hand,
          minSelect: 1,
          maxSelect: 1,
        },
      });
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
      modalSettings.value.ability === ABILITY.DIPLOMAT &&
      targetBuffer.value.index != null
    ) {
      modalSettings.value.options.maxCost +=
        gameStore.getCurrentPlayer.districts[targetBuffer.value.index].cost;
    } else if (
      modalSettings.value.ability === ABILITY.MAGISTRATE &&
      targetBuffer.value.indexes != null
    ) {
      modalSettings.value.options.untargetable = gameStore.getGame.characters
        .filter(
          (character) => !targetBuffer.value.indexes.includes(character.number)
        )
        .map((character) => character.number);
    } else if (
      modalSettings.value.ability === ABILITY.BLACKMAILER &&
      targetBuffer.value.indexes != null
    ) {
      modalSettings.value.options.untargetable = gameStore.getGame.characters
        .filter(
          (character) => !targetBuffer.value.indexes.includes(character.number)
        )
        .map((character) => character.number);
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

function addCharacterSelectModal() {
  modalChain.value.push({
    type: GAME_MODAL.CHARACTER,
    submit: (target) => selectCharacter(target[0]),
    options: {
      characters: gameStore.getGame.characters,
      unavailable: gameStore.getGame.unavailableCharacters,
      discarded: gameStore.getGame.discardedCharacters,
      selectCount: 1,
    },
  });
}

function addResourceSelectModal() {
  const goldAmount = hasDistrict(
    gameStore.getCurrentPlayer,
    DISTRICTS.GOLD_MINE
  )
    ? RESOURCE_GOLD + 1
    : RESOURCE_GOLD;
  const cardAmount = hasDistrict(
    gameStore.getCurrentPlayer,
    DISTRICTS.OBSERVATORY
  )
    ? RESOURCE_CARDS + 1
    : RESOURCE_CARDS;
  const keepAmount = hasDistrict(gameStore.getCurrentPlayer, DISTRICTS.LIBRARY)
    ? 2
    : 1;
  if (gameStore.getGame.drawnCards.length === 0) {
    modalChain.value.push({
      header: "Válassz nyersanyagot!",
      type: GAME_MODAL.CHOICE,
      submit: gatherResources,
      options: {
        choices: [
          {
            icon: "coins",
            tooltip: `Kapsz ${goldAmount} aranyat`,
            value: RESOURCE.GOLD,
          },
          {
            icon: "sheet-plastic",
            tooltip: `Húzol ${cardAmount} lapot${
              cardAmount === keepAmount
                ? ""
                : " és megtartasz " + keepAmount + " darabot"
            }`,
            value: RESOURCE.CARDS,
          },
        ],
      },
    });
  } else {
    modalChain.value.push({
      type: GAME_MODAL.CARDS,
      submit: drawCards,
      options: {
        cards: gameStore.getGame.drawnCards,
        minSelect: keepAmount,
        maxSelect: keepAmount,
      },
    });
  }
}

async function selectCharacter(number) {
  await gameStore.selectCharacter(lobbyCode, number);
  closeModal();
}

async function gatherResources(resource) {
  const response = await gameStore.gatherResources(lobbyCode, resource);
  if (resource === RESOURCE.CARDS) {
    const hasLibrary = hasDistrict(
      gameStore.getCurrentPlayer,
      DISTRICTS.LIBRARY
    );
    if (
      hasLibrary &&
      !hasDistrict(gameStore.getCurrentPlayer, DISTRICTS.OBSERVATORY)
    ) {
      gameStore.getGame.hand = gameStore.getGame.hand.concat(response.data);
    } else {
      modalChain.value.push({
        type: GAME_MODAL.CARDS,
        submit: drawCards,
        options: {
          cards: response.data,
          minSelect: hasLibrary ? 2 : 1,
          maxSelect: hasLibrary ? 2 : 1,
        },
      });
    }
  }
  openNextModal();
}

async function drawCards(cards) {
  await gameStore.drawCards(lobbyCode, cards);
  closeModal();
}

async function buildDistrict(index) {
  await gameStore.buildDistrict(lobbyCode, index);
}

async function useAbility(ability) {
  modalSettings.value.ability = ability;
  switch (ability) {
    case ABILITY.ASSASSIN:
    case ABILITY.THIEF:
    case ABILITY.WITCH:
      modalChain.value.push({
        type: GAME_MODAL.CHARACTER,
        submit: (target) =>
          useTargetedAbility(
            {
              index: target[0],
            },
            ability
          ),
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
          selectCount: 1,
        },
      });
      break;
    case ABILITY.MAGICIAN_PLAYER:
      modalChain.value.push({
        type: GAME_MODAL.PLAYER,
        submit: (target) =>
          useTargetedAbility(
            {
              id: target,
            },
            ability
          ),
        options: {
          players: gameStore.getGame.players.filter(
            (player) => player.id !== gameStore.getGame.currentPlayer
          ),
        },
      });
      break;
    case ABILITY.MAGICIAN_DECK:
      if (gameStore.getGame.hand.length > 0) {
        modalChain.value.push({
          type: GAME_MODAL.CARDS,
          submit: (target) =>
            useTargetedAbility(
              {
                indexes: target,
              },
              ability
            ),
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
    case ABILITY.WARLORD:
      modalChain.value.push({
        type: GAME_MODAL.DISTRICT,
        submit: useTargetedAbility,
        options: {
          players: gameStore.getGame.players,
          maxCost: gameStore.getCurrentPlayer.gold + 1,
        },
      });
      break;
    case ABILITY.SPY:
      modalChain.value.push({
        header: "Válassz típust!",
        type: GAME_MODAL.CHOICE,
        submit: (target) => openNextModal({ type: target }),
        options: {
          choices: [
            {
              icon: "crown",
              tooltip: "Nemesi",
              severity: "warning",
              value: DISTRICT_TYPE.NOBLE,
            },
            {
              icon: "hands-praying",
              tooltip: "Vallási",
              severity: "info",
              value: DISTRICT_TYPE.RELIGIOUS,
            },
            {
              icon: "coins",
              tooltip: "Kereskedelmi",
              severity: "success",
              value: DISTRICT_TYPE.TRADE,
            },
            {
              icon: "person-military-pointing",
              tooltip: "Katonai",
              severity: "danger",
              value: DISTRICT_TYPE.MILITARY,
            },
            {
              icon: "star",
              tooltip: "Egyedi",
              severity: "help",
              value: DISTRICT_TYPE.UNIQUE,
            },
          ],
        },
      });
      modalChain.value.push({
        type: GAME_MODAL.PLAYER,
        submit: (target) =>
          useTargetedAbility(
            {
              id: target,
              ...targetBuffer.value,
            },
            ability
          ),
        options: {
          players: gameStore.getGame.players.filter(
            (player) =>
              player.id !== gameStore.getGame.currentPlayer &&
              player.handSize > 0
          ),
        },
      });
      break;
    case ABILITY.WIZARD:
      modalChain.value.push({
        type: GAME_MODAL.PLAYER,
        submit: async (target) =>
          await gameStore.useAbility({
            ability: ability,
            code: lobbyCode,
            target: { id: target },
          }),
        options: {
          players: gameStore.getGame.players.filter(
            (player) =>
              player.id !== gameStore.getGame.currentPlayer &&
              player.handSize > 0
          ),
        },
      });
      break;
    case ABILITY.EMPEROR:
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
        type: GAME_MODAL.CHOICE,
        submit: (target) =>
          useTargetedAbility(
            { resource: target, ...targetBuffer.value },
            ability
          ),
        options: {
          choices: [
            {
              icon: "coins",
              tooltip: "Elveszel 1 aranyat",
              value: RESOURCE.GOLD,
            },
            {
              icon: "sheet-plastic",
              tooltip: "Elveszel 1 lapot",
              value: RESOURCE.CARDS,
            },
          ],
        },
      });
      break;
    case ABILITY.ABBOT:
      {
        let richestPlayers = [];
        let mostGold = -1;
        gameStore.getGame.players.forEach((player) => {
          if (player.gold > mostGold) {
            mostGold = player.gold;
            richestPlayers = [player];
          } else if (player.gold === mostGold) {
            richestPlayers.push(player);
          }
        });
        if (
          richestPlayers
            .map((player) => player.id)
            .includes(gameStore.getGame.currentPlayer)
        ) {
          toast.add({
            severity: "error",
            summary: "Nem használhatod ezt a képességet!",
            detail: "Te vagy a leggazdagabb játékos!",
            life: 3000,
          });
        } else if (richestPlayers.length === 1) {
          await useTargetedAbility({ id: richestPlayers[0].id }, ability);
        } else {
          modalChain.value.push({
            type: GAME_MODAL.PLAYER,
            submit: (target) => useTargetedAbility({ id: target }, ability),
            options: {
              players: richestPlayers,
            },
          });
        }
      }
      break;
    case ABILITY.RELIGIOUS_GOLD_OR_CARDS:
      {
        const resourceCount = gameStore.getCurrentPlayer.districts.filter(
          (district) =>
            district.type === DISTRICT_TYPE.RELIGIOUS ||
            district.abilities
              .map((ability) => ability.enum)
              .includes(DISTRICTS.SCHOOL_OF_MAGIC)
        ).length;
        if (resourceCount > 0) {
          modalChain.value.push({
            type: GAME_MODAL.SLIDER,
            submit: (target) =>
              useTargetedAbility(
                {
                  index: target.gold,
                  secondaryIndex: target.cards,
                },
                ability
              ),
            options: {
              resourceCount,
            },
          });
        } else {
          toast.add({
            severity: "error",
            summary: "Nem használhatod ezt a képességet!",
            detail: "Nincs vallási kerületed!",
            life: 3000,
          });
        }
      }
      break;
    case ABILITY.NAVIGATOR:
      modalChain.value.push({
        type: GAME_MODAL.CHOICE,
        submit: (target) =>
          useTargetedAbility(
            {
              resource: target,
            },
            ability
          ),
        options: {
          choices: [
            {
              icon: "coins",
              tooltip: "Kapsz 4 aranyat",
              value: RESOURCE.GOLD,
            },
            {
              icon: "sheet-plastic",
              tooltip: "Húzol 4 lapot",
              value: RESOURCE.CARDS,
            },
          ],
        },
      });
      break;
    case ABILITY.DIPLOMAT:
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
          submit: (target) =>
            useTargetedAbility(
              {
                secondaryId: target.id,
                secondaryIndex: target.index,
                ...targetBuffer.value,
              },
              ability
            ),
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
    case ABILITY.MAGISTRATE:
      modalChain.value.push({
        header: "Válaszd ki a karaktereket, amiknek parancsot raksz!",
        type: GAME_MODAL.CHARACTER,
        submit: (target) => openNextModal({ indexes: target }),
        options: {
          characters: gameStore.getGame.characters,
          discarded: gameStore.getGame.discardedCharacters,
          untargetable: gameStore.getGame.characters
            .filter(
              (character) =>
                character.number <= gameStore.getCurrentPlayer.character
            )
            .map((character) => character.number),
          selectCount: 3,
        },
      });
      modalChain.value.push({
        header: "Válaszd ki melyik karakteré legyen az aláírt parancs!",
        type: GAME_MODAL.CHARACTER,
        submit: (target) =>
          useTargetedAbility(
            {
              index: target[0],
              ...targetBuffer.value,
            },
            ability
          ),
        options: {
          characters: gameStore.getGame.characters,
          discarded: gameStore.getGame.discardedCharacters,
          selectCount: 1,
        },
      });
      break;
    case ABILITY.BLACKMAILER:
      modalChain.value.push({
        header: "Válaszd ki a karaktereket, amikre megfenyegetést raksz!",
        type: GAME_MODAL.CHARACTER,
        submit: (target) => openNextModal({ indexes: target }),
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
          selectCount: 2,
        },
      });
      modalChain.value.push({
        header: "Válaszd ki melyik karakteré legyen a valódi fenyegetés!",
        type: GAME_MODAL.CHARACTER,
        submit: (target) =>
          useTargetedAbility(
            {
              index: target[0],
              ...targetBuffer.value,
            },
            ability
          ),
        options: {
          characters: gameStore.getGame.characters,
          discarded: gameStore.getGame.discardedCharacters,
          selectCount: 1,
        },
      });
      break;
    case ABILITY.PAY_OFF:
      break;
    case ABILITY.CARDINAL:
      {
        const currentPlayer = gameStore.getCurrentPlayer;
        const otherPlayers = gameStore.getGame.players.filter(
          (player) => player.id !== currentPlayer.id
        );
        const mostGold = Math.min(
          Math.max(...otherPlayers.map((player) => player.gold)),
          gameStore.getGame.hand.length - 1
        );
        const filteredDistricts = gameStore.getGame.hand
          .map((district, i) => {
            district.originalIndex = i;
            return district;
          })
          .filter(
            (district) =>
              district.cost <= currentPlayer.gold + mostGold &&
              district.cost > currentPlayer.gold
          );
        if (filteredDistricts.length > 0) {
          modalChain.value.push({
            header: "Válassz kerületet, amit megépítenél!",
            type: GAME_MODAL.CARDS,
            submit: (target) => {
              const goldNeeded =
                filteredDistricts[target[0]].cost - currentPlayer.gold;
              modalChain.value.push({
                type: GAME_MODAL.PLAYER,
                submit: (t) => openNextModal({ id: t }),
                options: {
                  players: otherPlayers.filter(
                    (player) => player.gold >= goldNeeded
                  ),
                },
              });
              modalChain.value.push({
                header: `Válassz ${goldNeeded} kártyát, amit odaadsz!`,
                type: GAME_MODAL.CARDS,
                submit: (t) =>
                  useTargetedAbility(
                    { indexes: t, ...targetBuffer.value },
                    ability
                  ),
                options: {
                  cards: gameStore.getGame.hand.filter(
                    (_, i) => i !== filteredDistricts[target[0]].originalIndex
                  ),
                  minSelect: goldNeeded,
                  maxSelect: goldNeeded,
                },
              });
              openNextModal({
                index: filteredDistricts[target[0]].originalIndex,
              });
            },
            options: {
              cards: filteredDistricts,
              minSelect: 1,
              maxSelect: 1,
            },
          });
        } else {
          toast.add({
            severity: "error",
            summary: "Nem használhatod ezt a képességet!",
            detail:
              "Nincs ilyen kerület a kezedben vagy a többi játékosnak nincs elég aranya!",
            life: 3000,
          });
        }
      }
      break;
    case ABILITY.SCHOLAR:
      await gameStore.useAbility({
        ability: ability,
        code: lobbyCode,
      });
      modalChain.value.push({
        type: GAME_MODAL.CARDS,
        submit: (target) => useTargetedAbility({ index: target }),
        options: {
          cards: gameStore.getGame.drawnCards,
          minSelect: 1,
          maxSelect: 1,
        },
      });
      break;
    case ABILITY.MARSHAL:
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
    case DISTRICTS.FRAMEWORK:
    case DISTRICTS.LABORATORY:
      if (gameStore.getGame.hand.length > 0) {
        modalChain.value.push({
          type: GAME_MODAL.CARDS,
          submit: (target) =>
            useTargetedAbility(
              {
                index: target[0],
              },
              ability
            ),
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
    case DISTRICTS.ARMORY:
      modalChain.value.push({
        type: GAME_MODAL.DISTRICT,
        submit: useTargetedAbility,
        options: {
          players: gameStore.getGame.players.filter(
            (player) => player.id !== gameStore.getGame.currentPlayer
          ),
        },
      });
      break;
    case DISTRICTS.NECROPOLIS:
      modalChain.value.push({
        type: GAME_MODAL.CARDS,
        submit: (target) => useTargetedAbility({ index: target }),
        options: {
          cards: gameStore.getGame.districts,
          minSelect: 1,
          maxSelect: 1,
        },
      });
      break;
    case DISTRICTS.THIEVES_DEN:
      {
        if (
          gameStore.getGame.hand.length + gameStore.getCurrentPlayer.gold >
          6
        ) {
          const filteredDistricts = gameStore.getGame.hand
            .map((district, i) => {
              district.originalIndex = i;
              return district;
            })
            .filter(
              (district) =>
                !district.abilities
                  .map((ability) => ability.enum)
                  .includes(DISTRICTS.THIEVES_DEN)
            );
          modalChain.value.push({
            type: GAME_MODAL.CARDS,
            submit: (target) =>
              useTargetedAbility(
                {
                  indexes: target.map(
                    (index) => filteredDistricts[index].originalIndex
                  ),
                },
                ability
              ),
            options: {
              cards: filteredDistricts,
              minSelect: Math.max(6 - gameStore.getGame.gold, 1),
              maxSelect: 6,
            },
          });
        } else {
          toast.add({
            severity: "error",
            summary: "Nincs elég nyersanyagod!",
            detail: "Nincs elég aranyad és lapod a Tolvajtanya megépítéséhez!",
            life: 3000,
          });
        }
      }

      break;
    default:
      await gameStore.useAbility({
        ability: ability,
        code: lobbyCode,
      });
      modalSettings.value.ability = null;
  }
  openNextModal();
}

async function useTargetedAbility(target, ability) {
  await gameStore.useAbility({
    ability: ability,
    code: lobbyCode,
    target,
  });
  openNextModal();
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
