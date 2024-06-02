import Vue from "vue";

import Cookies from "js-cookie";

import Element from "element-ui";
import "./assets/styles/element-variables.scss";
import locale from "element-ui/lib/locale/lang/en";

import "@/assets/styles/index.scss"; // global css
import "@/assets/styles/base.scss"; //  css
import App from "./App";
import store from "./store";
import router from "./router";
import directive from "./directive"; // directive
import plugins from "./plugins"; // plugins
import { download } from "@/utils/request";

import "./assets/icons"; // icon
import "./permission"; // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import {
  parseTime,
  resetForm,
  addDateRange,
  selectDictLabel,
  selectDictLabels,
  handleTree,
} from "@/utils/cs35";
// Paging component
import Pagination from "@/components/Pagination";
// Custom table tool components
import RightToolbar from "@/components/RightToolbar";
// Rich text component
import Editor from "@/components/Editor";
// File upload component
import FileUpload from "@/components/FileUpload";
// Image upload module
import ImageUpload from "@/components/ImageUpload";
// Image preview component
import ImagePreview from "@/components/ImagePreview";
// Dictionary tag component
import DictTag from "@/components/DictTag";
// Header tag assembly
import VueMeta from "vue-meta";
// Dictionary data element
import DictData from "@/components/DictData";

// Global method mounting
Vue.prototype.getDicts = getDicts;
Vue.prototype.getConfigKey = getConfigKey;
Vue.prototype.parseTime = parseTime;
Vue.prototype.resetForm = resetForm;
Vue.prototype.addDateRange = addDateRange;
Vue.prototype.selectDictLabel = selectDictLabel;
Vue.prototype.selectDictLabels = selectDictLabels;
Vue.prototype.download = download;
Vue.prototype.handleTree = handleTree;

//
Vue.component("DictTag", DictTag);
Vue.component("Pagination", Pagination);
Vue.component("RightToolbar", RightToolbar);
Vue.component("Editor", Editor);
Vue.component("FileUpload", FileUpload);
Vue.component("ImageUpload", ImageUpload);
Vue.component("ImagePreview", ImagePreview);

Vue.use(directive);
Vue.use(plugins);
Vue.use(VueMeta);
DictData.install();
Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
};

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
};

Vue.prototype.msgWarning = function (msg) {
  this.$message({ showClose: true, message: msg, type: "warning" });
};

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
};

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get("size") || "medium",
  locale, // set element-ui default size
});

Vue.config.productionTip = false;

new Vue({
  el: "#app",
  router,
  store,
  render: (h) => h(App),
});
