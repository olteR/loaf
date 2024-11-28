import { defineStore } from "pinia";
import { REQ_TYPE, useRequestStore } from "@/stores/request";
import { useToast } from "primevue/usetoast";
import { computed, ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const requestStore = useRequestStore();
  const toast = useToast();

  const urls = {
    register: "auth/register",
    statistics: "statistics",
  };

  const statistics = ref();
  const getStatistics = computed(() => statistics.value);

  async function registerUser(user) {
    await requestStore.request(urls.register, REQ_TYPE.POST, user);
    toast.add({
      severity: "success",
      summary: "Siker",
      detail: "Sikeres regisztráció!",
      life: 3000,
    });
  }

  async function fetchStatistics() {
    const response = await requestStore.request(urls.statistics, REQ_TYPE.GET);
    statistics.value = response.data;
  }

  return {
    getStatistics: getStatistics,
    registerUser,
    fetchStatistics,
  };
});
