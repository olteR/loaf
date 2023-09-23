import { createRouter, createWebHistory } from "vue-router";
import { useStateStore } from "@/stores/state";
import { BREADCRUMB } from "@/utils/const";

import LoginView from "../views/LoginView.vue";
import ProfileView from "../views/ProfileView.vue";
import PageNotFound from "@/views/PageNotFound.vue";
import LobbiesView from "../views/lobbies/LobbiesView.vue";
import StatisticsView from "@/views/statistics/StatisticsView.vue";
import RulesView from "@/views/rules/RulesView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "login",
      component: LoginView,
      props: { breadcrumbs: [] },
    },
    {
      path: "/profile",
      name: "profile",
      component: ProfileView,
      props: { breadcrumbs: [{ name: "profile" }] },
    },
    {
      path: "/lobbies",
      name: "lobbies",
      component: LobbiesView,
      props: { breadcrumbs: [BREADCRUMB.LOBBIES] },
    },
    {
      path: "/statistics",
      name: "statistics",
      component: StatisticsView,
      props: { breadcrumbs: [BREADCRUMB.STATISTICS] },
    },
    {
      path: "/rules",
      name: "rules",
      component: RulesView,
      props: { breadcrumbs: [BREADCRUMB.RULES] },
    },
    {
      path: "/",
      redirect: () => {
        return "lobbies";
      },
    },
    {
      path: "/:pathMatch(.*)*",
      name: "404",
      component: PageNotFound,
    },
  ],
});
router.beforeEach((to) => {
  const stateStore = useStateStore();
  const breadcrumbs = [].concat(to.matched[0].props.default.breadcrumbs);
  stateStore.setBreadcrumbs(breadcrumbs);
});
export default router;
