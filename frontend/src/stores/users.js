import { defineStore } from "pinia";
import { REQ_TYPE, storeRequest, storeUrls } from "@/stores/storeUtils";

export const useUserStore = defineStore("user", () => {
  const urls = storeUrls({
    register: "auth/register",
  });

  async function registerUser(user) {
    await storeRequest(urls.register, REQ_TYPE.POST, user);
  }

  return {
    registerUser,
  };
});
