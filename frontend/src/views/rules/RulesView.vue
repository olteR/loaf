<template>
  <Card class="container mx-auto my-4">
    <template #title>
      <h1 class="text-9xl text-center">loaf.</h1>
    </template>
    <template #content>
      <h2 class="text-5xl">Bevezető</h2>
      <p>
        A játékban a játékosok azért vetélkednek, hogy belőlük legyen a
        következő Építőmester, amihez a leglenyűgözőbb középkori várost kell
        emelniük. Hogy ezt megtehessék, ahhoz aranyra van szükségük, valamint
        különféle kasztokba tartozó karakterek szolgálatainak megszerzésére.
      </p>
      <div class="grid grid-cols-3">
        <div>
          <h2 class="text-5xl mt-4">Az első játék</h2>
          <p>
            Az első játékhoz ezeket a karaktereket és egyedi kerületeket érdemes
            használni.
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
