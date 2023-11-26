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

  const getCards = computed(() => cards.value);

  async function fetchCards() {
    try {
      const response = await axios.get(urls.cards);
      cards.value = response.data;
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
    fetchCards,
  };
});
