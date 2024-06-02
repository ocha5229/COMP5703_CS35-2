import router from "./router";
import store from "./store";
import { Message } from "element-ui";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import { getToken } from "@/utils/auth";
import { isRelogin } from "@/utils/request";

NProgress.configure({ showSpinner: false });

const whiteList = ["/login", "/register"];

router.beforeEach((to, from, next) => {
  NProgress.start();
  if (getToken()) {
    to.meta.title && store.dispatch("settings/setTitle", to.meta.title);
    /* has token*/
    if (to.path === "/login") {
      next({ path: "/" });
      NProgress.done();
    } else if (whiteList.indexOf(to.path) !== -1) {
      next();
    } else {
      if (store.getters.roles.length === 0) {
        isRelogin.show = true;
        // user_info
        store
          .dispatch("GetInfo")
          .then(() => {
            isRelogin.show = false;
            store.dispatch("GenerateRoutes").then((accessRoutes) => {
              // roles

              router.addRoutes(accessRoutes); //
              next({ ...to, replace: true }); // hack addRoutes
            });
          })
          .catch((err) => {
            store.dispatch("LogOut").then(() => {
              Message.error(err);
              next({ path: "/" });
            });
          });
      } else {
        next();
      }
    }
  } else {
    // token
    if (whiteList.indexOf(to.path) !== -1) {
      // ，
      next();
    } else {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`); //
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  NProgress.done();
});
