import { defineStore } from "pinia";
import { REQ_TYPE, useRequestStore } from "@/stores/request";
import { useToast } from "primevue/usetoast";

export const useUserStore = defineStore("user", () => {
  const requestStore = useRequestStore();
  const toast = useToast();

  const urls = {
    register: "auth/register",
  };

  async function registerUser(user) {
    await requestStore.request(urls.register, REQ_TYPE.POST, user);
    toast.add({
      severity: "success",
      summary: "Siker",
      detail: "Sikeres regisztráció!",
      life: 3000,
    });
  }

  return {
    registerUser,
  };
});
