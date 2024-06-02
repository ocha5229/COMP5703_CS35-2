import request from "@/utils/request";

export function disscutionList(query) {
  return request({
    url: "/question/game/comment",
    method: "get",
    params: query,
  });
}

export function addDisscution(data) {
  return request({
    url: "/question/game/comment",
    method: "post",
    data: data,
  });
}

