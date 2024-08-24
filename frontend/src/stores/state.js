import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { useToast } from "primevue/usetoast";
import axios from "axios";
import router from "@/router";
import jwtDecode from "jwt-decode";
import { REQ_TYPE, storeRequest, storeUrls } from "@/stores/storeUtils";

export const useStateStore = defineStore("state", () => {
  const toast = useToast();

  const urls = storeUrls({
    login: `auth/login`,
  });

  const user = ref();
  const jwt = ref(
    localStorage.getItem("loaf_jwt") ? localStorage.getItem("loaf_jwt") : null
  );
  const breadcrumbs = ref([]);
  const loading = ref(false);

  const getUser = computed(() =>
    user.value
      ? user.value
      : jwt.value
      ? {
          id: jwtDecode(jwt.value).uid,
          name: jwtDecode(jwt.value).sub,
        }
      : null
  );
  const getJwt = computed(() => jwt.value);
  const isLoggedIn = computed(() =>
    jwt.value ? Date.now() < jwtDecode(jwt.value).exp * 1000 : false
  );
  const getBreadcrumbs = computed(() => breadcrumbs.value);
  const getLoading = computed(() => loading.value);

  async function loginUser(login) {
    const response = await storeRequest(urls.login, REQ_TYPE.POST, login);
    await handleLoginResponse(response);
    toast.add({
      severity: "success",
      summary: "siker.",
      detail: "sikeres bejelentkezés.",
      life: 3000,
    });
  }

  async function logoutUser() {
    delete axios.defaults.headers.common["Authorization"];
    user.value = null;
    jwt.value = null;
    localStorage.removeItem("loaf_jwt");
    await router.push("/login");
  }

  async function handleLoginResponse(response) {
    localStorage.setItem("loaf_jwt", response.data.token);
    jwt.value = response.data.token;
    axios.defaults.headers.common["Authorization"] =
      "Bearer " + response.data.token;
    user.value = response.data;
    await router.push("/");
  }

  function setBreadcrumbs(items) {
    breadcrumbs.value = items;
  }

  function setLoading(val) {
    loading.value = val;
  }

  return {
    getUser,
    getJwt,
    isLoggedIn,
    getBreadcrumbs,
    getLoading,
    loginUser,
    logoutUser,
    setBreadcrumbs,
    setLoading,
  };
});
