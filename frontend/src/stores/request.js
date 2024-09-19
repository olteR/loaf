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

  const urls = (urls) => {
    const baseUrl = window.location.origin;
    Object.keys(urls).forEach(function (key) {
      urls[key] = `${baseUrl}/api/${urls[key]}`;
    });
    return urls;
  };

  async function request(url, method, payload) {
    try {
      return await axios[method](url, payload);
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
    urls,
    request,
  };
});
