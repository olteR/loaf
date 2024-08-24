import { useToast } from "primevue/usetoast";
import axios from "axios";

export const storeUrls = (urls) => {
  const baseUrl = window.location.origin;
  Object.keys(urls).forEach(function (key) {
    urls[key] = `${baseUrl}/api/${urls[key]}`;
  });
  return urls;
};

export function handleStoreError(error) {
  const toast = useToast();
  toast.add({
    severity: "error",
    summary: "hiba.",
    detail: error,
    life: 3000,
  });
}

export const REQ_TYPE = {
  GET: "get",
  POST: "post",
  PUT: "put",
  PATCH: "patch",
  DELETE: "delete",
};

export async function storeRequest(url, method, payload) {
  try {
    return await axios[method](url, payload);
  } catch (error) {
    handleStoreError(error);
  }
}
