<template>
  <Card class="container mx-auto my-4">
    <template #title>
      <h1 class="text-9xl text-center">loaf.</h1>
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
      <div class="grid grid-cols-3">
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
      <h2 class="text-5xl" id="C4">A játék menete</h2>
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
      <div class="grid grid-cols-2">
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
    </template>
  </Card>
</template>

<script setup>
import Card from "primevue/card";
import Fieldset from "primevue/fieldset";
</script>
<style scoped>
td {
  border-width: 1px;
}
</style>
