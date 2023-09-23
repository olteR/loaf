<template>
  <Menubar :model="menuItems">
    <template #start>
      <div>
        <SiteBreadcrumb/>
      </div>
    </template>
    <template #item="{ item }">
      <Button class="menubutton" @click="router.push(item.to)"
        ><div
          class="menulabel"
          :class="{ active: router.currentRoute.value.name === item.label }"
        >
          <i :class="item.icon"></i>
          {{ item.label }}
        </div></Button
      >
    </template>
    <template #end>
      <div class="tab-menu-profile">
        bejelentkezett felhasználó:
        <router-link to="/profile">
          {{ stateStore.getUser ? stateStore.getUser.name : "" }}.
          <i class="fa fa-user"></i
        ></router-link>
      </div>
    </template>
  </Menubar>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStateStore } from "@/stores/state";
import SiteBreadcrumb from "@/components/general/SiteBreadcrumb.vue";
import Menubar from "primevue/menubar";
import Button from "primevue/button";

const router = useRouter();
const stateStore = useStateStore();
const menuItems = ref([
  {
    label: "lobbik",
    to: "/lobbies",
    icon: "fa fa-people-group",
  },
  {
    label: "statisztikák",
    to: "/statistics",
    icon: "fa fa-list-ol",
  },
  {
    label: "szabályok",
    to: "/rules",
    icon: "fa fa-circle-info",
  },
]);
</script>

<style>
.p-tabmenu {
  background-color: #1e1e1e;
  position: fixed;
  top: 0;
  z-index: 99;
  padding-top: 0.2rem;
}
.menubutton,
.menubutton > button {
  background-color: transparent !important;
  background-image: none !important;
  color: rgba(255, 255, 255, 0.87) !important;
}
.menulabel {
  border-bottom: 2px solid transparent;
}
.active {
  border-bottom: 2px solid #ce93d8;
  transition: border-bottom-color 0.5s ease;
}
.p-menubar .p-menubar-root-list {
  margin: auto;
}
.p-menubar-start {
  width: 30vw;
}
.p-menubar-end {
  width: 30vw;
  text-align: right;
  margin-left: 0 !important;
}
</style>
