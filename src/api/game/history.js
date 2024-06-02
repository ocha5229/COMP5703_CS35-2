import request from "@/utils/request";

export function historyList(query) {
  return request({
    url: "/question/historyList",
    method: "get",
    params: query,
  });
}

export function historyDeatail(query) {
  return request({
    url: "/question/history/info",
    method: "get",
    params: query,
  });
}

export function updateEvaluate(data) {
  return request({
    url: "/question/history/evaluate",
    method: "post",
    data: data,
  });
}


export function getEvaluate(id) {
  return request({
    url: "/question/history/" + id,
    method: "get",

  });
}
