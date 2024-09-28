import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { REQ_TYPE, useRequestStore } from "@/stores/request";

export const useCardStore = defineStore("card", () => {
  const requestStore = useRequestStore();
  const urls = {
    cards: "game/cards",
  };

  const cards = ref([]);
  const characterImages = ref([]);
  const districtImages = ref([]);

  const getCards = computed(() => cards.value);
  const getCharacterImages = computed(() => characterImages.value);
  const getDistrictImages = computed(() => districtImages.value);

  async function fetchCards() {
    if (cards.value.length === 0) {
      const response = await requestStore.request(urls.cards, REQ_TYPE.GET);
      cards.value = response.data;
      cards.value.characters.forEach((character) => {
        let img = new Image();
        img.src = `${window.location.origin}/src/assets/characters/${character.id}.jpg`;
        characterImages.value.push(img);
      });
      cards.value.districts.forEach((district) => {
        let img = new Image();
        img.src = `${window.location.origin}/src/assets/districts/${district.id}.jpg`;
        districtImages.value.push(img);
      });
    }
  }

  return {
    getCards: getCards,
    getCharacterImages: getCharacterImages,
    getDistrictImages: getDistrictImages,
    fetchCards,
  };
});
