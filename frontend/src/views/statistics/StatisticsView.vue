<template>
  <div v-if="userStore.getStatistics">
    <Card class="container mx-auto my-4">
      <template #title>
        <h1 class="text-5xl text-center">Statisztikák</h1>
      </template>
      <template #content>
        <div class="grid grid-cols-5 gap-x-8">
          <div>
            <h3 class="text-2xl text-center font-bold mb-4">
              Általános statisztikák
            </h3>
            <div>
              Lejátszott játékok: {{ userStore.getStatistics?.gamesPlayed }}
            </div>
            <div>Megnyert játékok: {{ userStore.getStatistics?.gamesWon }}</div>
            <div>
              Győzelmi ráta:
              {{
                caclulateRate(
                  userStore.getStatistics?.gamesWon,
                  userStore.getStatistics?.gamesPlayed
                )
              }}%
            </div>
            <div>
              Átlagos helyezés:
              {{ userStore.getStatistics?.averagePlacement }}
            </div>
            <div>
              Legtöbbet választott karakter:
              {{
                `${mostPickedNumber.maxIndexes.join(", ")} (${
                  mostPickedNumber.max
                }x)`
              }}
            </div>
            <div>
              Legtöbbet választott rang:
              {{
                `${mostPickedCharacter.maxIndexes.map(
                  (ind) => cardStore.getCards.characters[ind - 1].name
                )} (${mostPickedCharacter.max}x)`
              }}
            </div>
          </div>
          <div>
            <h3 class="font-bold text-2xl text-center mb-4">
              Győzelmek aránya
            </h3>
            <Chart
              type="pie"
              :data="{
                labels: ['Megnyert játékok', 'Nem megnyert játékok'],
                datasets: [
                  {
                    data: [
                      userStore.getStatistics?.gamesWon,
                      userStore.getStatistics?.gamesPlayed -
                        userStore.getStatistics?.gamesWon,
                    ],
                    color: '#ffffff',
                    borderColor: '#222222',
                    backgroundColor: ['#C5E1A5', '#EF9A9A'],
                  },
                ],
              }"
              :options="{
                plugins: {
                  legend: {
                    display: false,
                  },
                  tooltip: {
                    callbacks: {
                      label: (item) => {
                        return ` ${item.parsed} (${caclulateRate(
                          item.parsed,
                          userStore.getStatistics?.gamesPlayed
                        )}%)`;
                      },
                    },
                  },
                },
              }"
            ></Chart>
          </div>
          <div>
            <h3 class="font-bold text-2xl text-center mb-4">
              Arany és lapok aránya
            </h3>
            <Chart
              type="pie"
              :data="{
                labels: ['Arany választva', 'Lapok választva'],
                datasets: [
                  {
                    data: [
                      userStore.getStatistics?.goldChosen,
                      userStore.getStatistics?.cardsChosen,
                    ],
                    color: '#ffffff',
                    borderColor: '#222222',
                    backgroundColor: ['#FFF59D', '#CE93D8'],
                  },
                ],
              }"
              :options="{
                plugins: {
                  legend: {
                    display: false,
                  },
                  tooltip: {
                    callbacks: {
                      label: (item) => {
                        return ` ${item.parsed} (${caclulateRate(
                          item.parsed,
                          userStore.getStatistics?.goldChosen +
                            userStore.getStatistics?.cardsChosen
                        )}%)`;
                      },
                    },
                  },
                },
              }"
            ></Chart>
          </div>
          <div>
            <h3 class="font-bold text-2xl text-center mb-4">
              Választott rangok aránya
            </h3>
            <Chart
              type="pie"
              :data="{
                labels: userStore.getStatistics?.numberPicks.map(
                  (pick) => pick.character
                ),
                datasets: [
                  {
                    data: userStore.getStatistics?.numberPicks.map(
                      (pick) => pick.picks
                    ),
                    color: '#ffffff',
                    borderColor: '#222222',
                    backgroundColor: [
                      '#9FA8DA',
                      '#F48FB1',
                      '#FFE0B2',
                      '#FFF59D',
                      '#90CAF9',
                      '#A5D6A7',
                      '#CE93D8',
                      '#EF9A9A',
                    ],
                  },
                ],
              }"
              :options="{
                plugins: {
                  legend: {
                    display: false,
                  },
                  tooltip: {
                    callbacks: {
                      label: (item) => {
                        return ` ${item.parsed} (${caclulateRate(
                          item.parsed,
                          userStore.getStatistics?.numberPicks.reduce(
                            (partialSum, a) => partialSum + a.picks,
                            0
                          )
                        )}%)`;
                      },
                    },
                  },
                },
              }"
            ></Chart>
          </div>
          <div>
            <h3 class="font-bold text-2xl text-center mb-4">
              Választott karakterek aránya
            </h3>
            <Chart
              type="pie"
              :data="{
                labels: characterPicksFiltered.map(
                  (pick) =>
                    cardStore.getCards.characters[pick.character - 1].name
                ),
                datasets: [
                  {
                    data: characterPicksFiltered.map((pick) => pick.picks),
                    color: '#ffffff',
                    borderColor: '#222222',
                    backgroundColor: [
                      '#9FA8DA',
                      '#F48FB1',
                      '#FFE0B2',
                      '#FFF59D',
                      '#90CAF9',
                      '#A5D6A7',
                      '#CE93D8',
                      '#EF9A9A',
                    ],
                  },
                ],
              }"
              :options="{
                plugins: {
                  legend: {
                    display: false,
                  },
                  tooltip: {
                    callbacks: {
                      label: (item) => {
                        return ` ${item.parsed} (${caclulateRate(
                          item.parsed,
                          characterPicksFiltered.reduce(
                            (partialSum, a) => partialSum + a.picks,
                            0
                          )
                        )}%)`;
                      },
                    },
                  },
                },
              }"
            ></Chart>
          </div>
        </div>
      </template>
    </Card>
    <Card class="container mx-auto my-4"
      ><template #title>
        <h1 class="text-5xl text-center">Múltbeli játékok</h1>
      </template>
      <template #content>
        <Panel
          v-for="game in userStore.getStatistics?.previousGames"
          :key="game.code"
          class="my-6"
          style="outline: solid thick"
          :class="{
            'outline-amber-400': game.placement === 1,
            'outline-gray-400': game.placement === 2,
            'outline-amber-800': game.placement === 3,
          }"
        >
          <template #header>
            <div :class="`text-2xl font-bold`">
              <span>
                {{ game.name }}
              </span>
              <i
                v-if="game.placement < 4"
                class="ml-2 fa fa-trophy"
                :class="{
                  'text-amber-400': game.placement === 1,
                  'text-gray-400': game.placement === 2,
                  'text-amber-800': game.placement === 3,
                }"
              />
            </div>
          </template>
          <div class="inline-flex w-full justify-between">
            <div>
              <div>{{ `Helyezés: ${game.placement}.` }}</div>
              <div>
                <span>{{ `Pontszám: ${game.points}` }}</span>
                <i class="ml-1 fa fa-star" />
              </div>
              <div>
                Játékosok:
                <Chip v-for="player in game.players" :key="player" class="mr-1">
                  {{ player }}
                </Chip>
              </div>
            </div>
            <div class="self-end">
              <Button @click="router.push(`/game-results/${game.code}`)"
                >Részletek</Button
              >
            </div>
          </div>
        </Panel>
      </template>
    </Card>
  </div>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { DISABLED_CHARACTERS } from "@/utils/const";
import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import { useUserStore } from "@/stores/users";
import Button from "primevue/button";
import Card from "primevue/card";
import Chart from "primevue/chart";
import Chip from "primevue/chip";
import Panel from "primevue/panel";

const router = useRouter();
const stateStore = useStateStore();
const cardStore = useCardStore();
const userStore = useUserStore();

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  await userStore.fetchStatistics();
  stateStore.setLoading(false);
});

const characterPicksFiltered = computed(() => {
  return userStore.getStatistics?.characterPicks.filter(
    (pick) => !DISABLED_CHARACTERS.includes(pick.character)
  );
});

const mostPickedNumber = computed(() => {
  let max = 0;
  let maxIndexes = [];
  userStore.getStatistics?.numberPicks.forEach((pick) => {
    if (pick.picks > max) {
      max = pick.picks;
      maxIndexes = [pick.character];
    } else if (pick.picks === max) {
      maxIndexes.push(pick.character);
    }
  });
  return { max, maxIndexes };
});

const mostPickedCharacter = computed(() => {
  let max = 0;
  let maxIndexes = [];
  characterPicksFiltered.value.forEach((pick) => {
    if (pick.picks > max) {
      max = pick.picks;
      maxIndexes = [pick.character];
    } else if (pick.picks === max) {
      maxIndexes.push(pick.character);
    }
  });
  return { max, maxIndexes };
});

function caclulateRate(a, b) {
  if (a && b) {
    return percentage(a / b);
  }
  return 0;
}

function percentage(number) {
  return Math.round(number * 10000) / 100;
}
</script>
