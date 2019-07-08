import Vue from "vue";
import Router from "vue-router";

import Projects from "./components/projects/Projects";
import Tasks from "./components/administrative/tasks/Tasks";
import Settings from "./components/administrative/settings/Settings";
import About from "./components/About.vue";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/projects",
      name: "projects",
      component: Projects
    },
    {
      path: "/admin/tasks",
      name: "tasks",
      component: Tasks
    },
    {
      path: "/admin/settings",
      name: "settings",
      component: Settings
    },
    {
      path: "/about",
      name: "about",
      component: About
      // // route level code-splitting
      // // this generates a separate chunk (about.[hash].js) for this route
      // // which is lazy-loaded when the route is visited.
      // component: () =>
      //   import(/* webpackChunkName: "about" */ "./views/About.vue")
    }
  ]
});