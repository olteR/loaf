import { createRouter, createWebHistory } from "vue-router";
import { useStateStore } from "@/stores/state";
import { BREADCRUMB } from "@/utils/const";

import LoginView from "../views/base/LoginView.vue";
import RegisterView from "@/views/base/RegisterView.vue";
import ProfileView from "../views/base/ProfileView.vue";
import PageNotFound from "@/views/base/PageNotFound.vue";
import MyGamesView from "@/views/lobbies/MyGamesView.vue";
import LobbiesView from "../views/lobbies/LobbiesView.vue";
import LobbyView from "@/views/lobbies/LobbyView.vue";
import GameView from "@/views/game/GameView.vue";
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
      path: "/register",
      name: "register",
      component: RegisterView,
      props: { breadcrumbs: [] },
    },
    {
      path: "/profile",
      name: "profile",
      component: ProfileView,
      props: { breadcrumbs: [{ name: "profile" }] },
    },
    {
      path: "/my-games",
      name: "my-games",
      component: MyGamesView,
      props: { breadcrumbs: [BREADCRUMB.MY_GAMES] },
    },
    {
      path: "/lobbies",
      name: "lobbies",
      component: LobbiesView,
      props: { breadcrumbs: [BREADCRUMB.LOBBIES] },
    },
    {
      path: "/lobby/:code?",
      name: "lobby",
      component: LobbyView,
      props: { breadcrumbs: [BREADCRUMB.LOBBIES] },
    },
    {
      path: "/game/:code?",
      name: "game",
      component: GameView,
      props: { breadcrumbs: [] },
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
