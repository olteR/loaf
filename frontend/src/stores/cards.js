import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { useToast } from "primevue/usetoast";
import axios from "axios";

export const useCardStore = defineStore("card", () => {
  const baseUrl = window.location.origin;
  const toast = useToast();

  const urls = {
    cards: `${baseUrl}/api/game/cards`,
  };

  const cards = ref([]);
  const characterImages = ref([]);
  const districtImages = ref([]);

  const getCards = computed(() => cards.value);
  const getCharacterImages = computed(() => characterImages.value);
  const getDistrictImages = computed(() => districtImages.value);

  async function fetchCards() {
    try {
      if (cards.value.length === 0) {
        const response = await axios.get(urls.cards);
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
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "hiba.",
        detail: error,
        life: 3000,
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
