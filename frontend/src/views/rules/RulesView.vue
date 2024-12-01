<template>
  <Card class="container mx-auto my-4" v-if="cardStore.getCards">
    <template #title>
      <h1 class="text-5xl text-center">Játékszabályok</h1>
    </template>
    <template #content>
      <div class="grid grid-cols-3">
        <div class="px-4 mb-4">
          <Fieldset legend="Tartalomjegyzék">
            <div>
              <a href="#C1">1. Bevezető</a>
            </div>
            <div>
              <a href="#C2">2. Az első játék</a>
            </div>
            <div>
              <a href="#C3">3. A játék megkezdése</a>
            </div>
            <div>
              <a href="#C4">4. A játék menete</a>
            </div>
            <div>
              <a href="#C5">5. Kiválasztási fázis</a>
            </div>
            <div>
              <a href="#C6">6. Kör fázis</a>
            </div>
            <div>
              <a href="#C7">7. Karakterképességek</a>
            </div>
            <div>
              <a href="#C8">8. Kerületek</a>
            </div>
            <div>
              <a href="#C9">9. A játék vége</a>
            </div>
            <div>
              <a href="#C10">10. Karakterek</a>
            </div>
            <div>
              <a href="#C11">11. Egyedi kerületek</a>
            </div>
          </Fieldset>
        </div>
      </div>

      <h2 class="text-5xl" id="C1">Bevezető</h2>
      <p>
        A játékban a játékosok azért vetélkednek, hogy belőlük legyen a
        következő Építőmester, amihez a leglenyűgözőbb középkori várost kell
        emelniük. Hogy ezt megtehessék, ahhoz aranyra van szükségük, valamint
        különféle kasztokba tartozó karakterek szolgálatainak megszerzésére.
      </p>
      <div class="grid grid-cols-3 mt-8">
        <div>
          <h2 class="text-5xl mt-4" id="C2">Az első játék</h2>
          <p>
            Az első játékhoz ezeket a karaktereket és egyedi kerületeket érdemes
            használni. Miután megismerkedtetek a játék menetével nyugodtan
            személyre szabhatjátok a karaktereket és kerületeket, hogy izgalmas
            és változatos maradjon a játék.
          </p>
        </div>
        <div class="px-4">
          <Fieldset legend="Karakterek">
            <div class="grid grid-cols-2">
              <div>1. Orgyilkos</div>
              <div>5. Püspök</div>
              <div>2. Tolvaj</div>
              <div>6. Kereskedő</div>
              <div>3. Mágus</div>
              <div>7. Építész</div>
              <div>4. Király</div>
              <div>8. Hadúr</div>
            </div>
          </Fieldset>
        </div>
        <div class="px-4">
          <Fieldset legend="Egyedi kerületek">
            <div class="grid grid-cols-2">
              <div>1. Birodalmi Kincstár</div>
              <div>8. Kőfejtő</div>
              <div>2. Erődítmény</div>
              <div>9. Laboratórium</div>
              <div>3. Gyár</div>
              <div>10. Sárkánykapu</div>
              <div>4. Kísértetváros</div>
              <div>11. Szobor</div>
              <div>5. Kívánságkút</div>
              <div>12. Térképszoba</div>
              <div>6. Kovácsműhely</div>
              <div>13. Tolvajtanya</div>
              <div>7. Könyvtár</div>
              <div>14. Varázstanoda</div>
            </div>
          </Fieldset>
        </div>
      </div>
      <h2 class="text-5xl" id="C3">A játék megkezdése</h2>
      <p>
        A játék kezdetekor a kiválasztott egyedi kerületek összekeverődnek a
        pakliban alapból található 54 kerületkártyával. Ezután minden játékos
        kap 2 aranyat, illetve 4 lapot a pakliból. A beállításoknál kiválasztott
        játékos (alapértelmezetten véletlenszerűen) megkapja a koronát, ő fogja
        kezdeni az első kört.
      </p>
      <h2 class="text-5xl mt-8" id="C4">A játék menete</h2>
      <p>
        A játék menete körökre van osztva. Minden kör a kiválasztási fázissal
        kezdődik, aminek során a játékosok sorban választanak karaktert. Egy
        karaktert csak egy játékos választhat, így minél később kerülsz sorra,
        annál kisebb lesz a választék. Minden karakterkártyának különleges
        képességei vannak, mint amilyen a másik játékos aranyának ellopása vagy
        egy másik játékos kerületének elpusztítása. A kiválasztási fázis után
        következik a kör fázis, amiben a játékosok nyersanyagokat gyűjtenek és
        új kerületeket építenek a városaikban.
      </p>
      <div class="grid grid-cols-2 mt-8">
        <div>
          <h2 class="text-5xl" id="C5">Kiválasztási fázis</h2>
          <p>
            A kiválasztási fázis elején néhány karakterlap véletlenszerűen
            eldobódik és nem lesz választható a körben. Ez lehet látható, így
            minden játékos tudja, hogy kimaradnak, vagy rejtett, amikor csak a
            koronával rendelkező játékos ismeri. Ezeknek a lapoknak a száma a
            játékosok számától függ. Az eldobott karaktereket a képükön egy
            <i class="fa fa-xmark" />
            jelöli. 4-es számú karakter nem lehet láthatóan eldobott.
          </p>
        </div>
        <div class="px-4">
          <Fieldset legend="Kiválasztási fázis 8 karakterrel">
            <div class="grid grid-cols-3">
              <div>4 játékos esetén:</div>
              <div>2 látható eldobott lap</div>
              <div>1 ismeretlen eldobott lap</div>
              <div>5 játékos esetén:</div>
              <div>1 látható eldobott lap</div>
              <div>1 ismeretlen eldobott lap</div>
              <div>6 játékos esetén:</div>
              <div>nincs látható eldobott lap</div>
              <div>1 ismeretlen eldobott lap</div>
              <div>7 játékos esetén:</div>
              <div>nincs látható eldobott lap</div>
              <div
                v-tooltip.top="{
                  value:
                    'Hét játékos esetén az utolsó játékos választhatja a rejtett eldobott karaktert!',
                  escape: false,
                }"
              >
                1* ismeretlen eldobott lap
              </div>
            </div>
          </Fieldset>
        </div>
      </div>
      <p>
        A megmaradt karakterekből először a koronával rendelkező játékos választ
        egyet. Eztuán a következő játékos a többi közül választ egy másikat. Ez
        addig folytatódi, ameddig minden játékosnak lesz választott karaktere.
        Hét játékos esetén az utolsó játékos választhatja a rejtett eldobott
        karaktert.
      </p>
      <h2 class="text-5xl mt-8" id="C6">Kör fázis</h2>
      <p>
        Eltérően a kiválasztási fázistól, a kör fázis során a játékosok nem a
        játékban lévő sorrendjük alapján lépnek, hanem választott karaktereik
        rangjának megfelelően emelkedő számsorrendben. Ez az érték minden
        karakterkártya bal felső sarkában látható. A játék ez alapján
        automatikusan indítja a következő körben következő játékost.
      </p>
      <p>
        A kör során a játékosnak nyersanyagokat
        <span class="font-bold">kell</span> gyűjtenie. Kaphat két aranyat, vagy
        húzhat két kerületkártyát a pakliból. Ha lapot húz, abból kiválaszt
        egyet, amit megtarthat, a másik pedig eldobódik.
      </p>
      <p>
        A nyersanyagok gyűjtése után
        <span class="font-bold">lehetősége van</span> építeni egy kerületet a
        városában. Ha ezt akarja tenni, akkor a lapot a képernyő közepére kell
        húznia. Ha megépítheti a kerületet, akkor bekerül a városába és a játék
        levonja az árát a kincstartalékból. A játékos körönként egy kerületet
        építhet, és nem építhet két egyforma kerületet (aminek ugyanaz a neve),
        ha már van egy a városában.
      </p>
      <p>
        Miután a játékos elvégezte a körét, a képernyő jobb alján lévő
        <span class="font-bold">Kör befejezése</span> gombbal véglegesítheti
        azt. Ekkor a játék automatikusan felfedi a következő játékban lévő
        karaktert és átadja a kört annak a játékosnak. Amennyiben minden játékos
        végzett a körével, egy új kör veszi kezdetét a kiválasztási fázissal
      </p>
      <h2 class="text-5xl mt-8" id="C7">Karakterképességek</h2>
      <p>
        A játékos a karakterének képességeit a körében bármikor használhatja
        (kivéve, ha a leírás felülírja a használat idejét). Ezt a képernyő jobb
        oldalán lévő képességek fülben lévő gombok megnyomásával teheti. Ezután,
        ha célpontot kell választani a képességhez, akkor azt megteheti a
        felugró ablakban. Minden képességet egy körben egyszer lehet használni.
        Bizonyos karaktereknek olyan képességei vannak, amivel nyersanyagokat
        gyűjtenek városaik bizonyos típusú kerületeinek. Ezek a képességek
        eredményezhetnek aranyat vagy lapokat, amint azt minden karakter
        képessége részletesen ismerteti. A karakter keretének a színe megegyezik
        a hozzá tartozó kerületek kereteinek színével. A karakterek listája az
        oldal alján található részletes leírásokkal.
      </p>
      <h2 class="text-5xl mt-8" id="C8">Kerületek</h2>
      <p>
        Öt különböző kerülettípus van, amiket a lenti színek és ikonok jelölnek.
        A kerülettípusokra bizonyos karakterkártyák hivatkoznak, és egyesek
        hatással vannak a játék végi pontszámításra.
      </p>
      <div class="grid grid-cols-5 gap-x-4 text-center">
        <div>
          <Fieldset legend="Nemesi">
            <i
              class="fa fa-crown text-9xl"
              :style="{ color: COLORS[DISTRICT_TYPE.NOBLE].PRIMARY }"
            />
          </Fieldset>
        </div>
        <div>
          <Fieldset legend="Egyházi">
            <i
              class="fa fa-hands-praying text-9xl"
              :style="{ color: COLORS[DISTRICT_TYPE.RELIGIOUS].PRIMARY }"
            />
          </Fieldset>
        </div>
        <div>
          <Fieldset legend="Kereskedelmi">
            <i
              class="fa fa-coins text-9xl"
              :style="{ color: COLORS[DISTRICT_TYPE.TRADE].PRIMARY }"
            />
          </Fieldset>
        </div>
        <div>
          <Fieldset legend="Katonai">
            <i
              class="fa fa-person-military-pointing text-9xl"
              :style="{ color: COLORS[DISTRICT_TYPE.MILITARY].PRIMARY }"
            />
          </Fieldset>
        </div>
        <div>
          <Fieldset legend="Egyedi">
            <i
              class="fa fa-star text-9xl"
              :style="{ color: COLORS[DISTRICT_TYPE.UNIQUE].PRIMARY }"
            />
          </Fieldset>
        </div>
      </div>
      <p>
        Minden
        <span style="font-variant: small-caps">egyedi</span> kerületnek van
        hatása, ami az egérmutató lap nevére helyezésével látható. Ezen hatások
        különféle dolgokat tesznek, pl. több, bizonyos típusú nyersanyagot adnak
        neked vagy pluszpontokat kapsz értük a játék végén.
      </p>
      <div class="grid grid-cols-2 gap-x-4">
        <div>
          <Fieldset legend="Leírások egyszerűsítései">
            <div>
              A lapok leírásának egyszerűsítése érdekében néhány gyakran
              használt szó ikonokkal van helyettesítve. Ezek magyarázatai:
            </div>
            <div>
              <i class="fa fa-coins" />
              <span> : Arany</span>
            </div>
            <div>
              <i class="fa fa-sheet-plastic" />
              <span> : Lap</span>
            </div>
            <div>
              <i class="fa fa-city" />
              <span> : Kerület</span>
            </div>
            <div>
              <i class="fa fa-star" />
              <span> : Pont</span>
            </div>
          </Fieldset>
        </div>
        <div>
          <Fieldset legend="Ne feledd...">
            <ul class="list-disc ml-8">
              <li>
                A játékos kincstartalékában lévő aranyak és a játékos kezében
                lévő lapok száma nyílt információ.
              </li>
              <li>
                Nincs korlátozva, hogy egy játékosnak mennyi arany lehet a
                kincstartalékában.
              </li>
              <li>
                Nincs korlátozva az sem, hogy egy játékosnak hány lap lehet a
                kezében.
              </li>
            </ul>
          </Fieldset>
        </div>
      </div>
      <h2 class="text-5xl mt-8" id="C9">A játék vége</h2>
      <p>
        Amikor egy játékos megépíti a hetedik kerületét, a város elkészült, és a
        játék véget és, miután az aktuális kör befejeződött. Egy városnak lehet
        hétnél több kerülete.
      </p>
      <p>A játék végén a játékosok a következőkért kaphatnak pontot:</p>
      <ul class="list-disc ml-8">
        <li>
          Mindenki annyi pontot kap, amennyi a városában lévő összes kerület
          építési költsége.
        </li>
        <li>
          Ha egy játékosnak mindegyik típusból van legalább egy kerülete, akkor
          3 pontot kap.
        </li>
        <li>Az a játékos, aki elsőnek fejezte be a városát, 4 pontot kap.</li>
        <li>Minden más játékos, aki befejezte a városát, 2 pontot kap.</li>
        <li>
          Az <span style="font-variant: small-caps">egyedi</span> kerületekért
          pluszpontok járnak.
        </li>
      </ul>
      <h2 class="text-5xl my-8" id="C10">Karakterek</h2>
      <div class="grid grid-cols-3 gap-x-8">
        <div
          class="margin-offset"
          v-for="character in sortedCharacters"
          :key="character"
        >
          <GameSettingCharacterCard :character="character" selected is-big />
        </div>
      </div>
      <h2 class="text-5xl my-8" id="C11">Egyedi kerületek</h2>
      <div class="grid grid-cols-3 gap-x-8">
        <div
          class="margin-offset"
          v-for="district in cardStore.getCards.districts?.filter(
            (d) => d.type === DISTRICT_TYPE.UNIQUE
          )"
          :key="district"
        >
          <GameSettingDistrictCard :district="district" :selected="true" />
        </div>
      </div>
    </template>
  </Card>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { COLORS, DISABLED_CHARACTERS, DISTRICT_TYPE } from "@/utils/const";
import { sortCharacters } from "@/utils/utils";
import { useStateStore } from "@/stores/state";
import { useCardStore } from "@/stores/cards";
import Card from "primevue/card";
import Fieldset from "primevue/fieldset";
import GameSettingCharacterCard from "@/components/lobbies/GameSettingCharacterCard.vue";
import GameSettingDistrictCard from "@/components/lobbies/GameSettingDistrictCard.vue";

const stateStore = useStateStore();
const cardStore = useCardStore();

const sortedCharacters = computed(() => {
  let chars =
    cardStore.getCards.characters?.filter(
      (char) => !DISABLED_CHARACTERS.includes(char.id)
    ) ?? [];
  return chars.sort(sortCharacters);
});

onMounted(async () => {
  stateStore.setLoading(true);
  await cardStore.fetchCards();
  stateStore.setLoading(false);
});
</script>
<style scoped>
td {
  border-width: 1px;
}
.margin-offset {
  margin-right: 1.25rem;
}
</style>
