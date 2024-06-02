import { Message, MessageBox, Notification, Loading } from 'element-ui'

let loadingInstance;

export default {
  //
  msg(content) {
    Message.info(content)
  },
  //
  msgError(content) {
    Message.error(content)
  },
  //
  msgSuccess(content) {
    Message.success(content)
  },
  //
  msgWarning(content) {
    Message.warning(content)
  },
  //
  alert(content) {
    MessageBox.alert(content, "")
  },
  //
  alertError(content) {
    MessageBox.alert(content, "", { type: 'error' })
  },
  //
  alertSuccess(content) {
    MessageBox.alert(content, "", { type: 'success' })
  },
  //
  alertWarning(content) {
    MessageBox.alert(content, "", { type: 'warning' })
  },
  //
  notify(content) {
    Notification.info(content)
  },
  //
  notifyError(content) {
    Notification.error(content);
  },
  //
  notifySuccess(content) {
    Notification.success(content)
  },
  //
  notifyWarning(content) {
    Notification.warning(content)
  },
  //
  confirm(content) {
    return MessageBox.confirm(content, "", {
      confirmButtonText: '',
      cancelButtonText: '',
      type: "warning",
    })
  },
  //
  prompt(content) {
    return MessageBox.prompt(content, "", {
      confirmButtonText: '',
      cancelButtonText: '',
      type: "warning",
    })
  },
  //
  loading(content) {
    loadingInstance = Loading.service({
      lock: true,
      text: content,
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    })
  },
  //
  closeLoading() {
    loadingInstance.close();
  }
}
