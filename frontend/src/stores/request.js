import { defineStore } from "pinia";
import { useToast } from "primevue/usetoast";
import axios from "axios";

export const REQ_TYPE = {
  GET: "get",
  POST: "post",
  PUT: "put",
  PATCH: "patch",
  DELETE: "delete",
};

export const useRequestStore = defineStore("request", () => {
  const toast = useToast();

  async function request(url, method, payload) {
    try {
      return await axios[method](`/api/${url}`, payload);
    } catch (error) {
      toast.add({
        severity: "error",
        summary: "Hiba",
        detail: error,
        life: 3000,
      });
    }
  }

  return {
    request,
  };
});
