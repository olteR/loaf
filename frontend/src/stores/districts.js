import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { useToast } from "primevue/usetoast";
import axios from "axios";

export const useDistrictStore = defineStore("district", () => {
  const baseUrl = window.location.origin;
  const toast = useToast();

  const urls = {
    districts: `${baseUrl}/api/game/districts`,
  };

  const districts = ref([]);

  const getDistricts = computed(() => districts.value);

  async function fetchDistricts() {
    try {
      const response = await axios.get(urls.districts);
      districts.value = response.data;
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
    getDistricts,
    fetchDistricts,
  };
});
