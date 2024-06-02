import request from "@/utils/request";

export function allTypeList(query) {
  return request({
    url: "/question/type/allList",
    method: "get",
    params: query,
  });
}

export function getScore(query) {
  return request({
    url: "/question/answer/cancel",
    method: "get",
    params: query,
  });
}
;
export function subjectList(query) {
  return request({
    url: "/question/list",
    method: "get",
    params: query,
  });
}

export function addSubject(data) {
  return request({
    url: "/question",
    method: "post",
    data: data,
  });
}

export function addOptions(data) {
  return request({
    url: "/question/option",
    method: "post",
    data: data,
  });
}

export function getSubject(id) {
  return request({
    url: "/question/" + id,
    method: "get",
  });
}

export function deleteSubject(id) {
  return request({
    url: "/question/game/" + id,
    method: "delete",
  });
}

export function createGame(data) {
  return request({
    url: "/question/createGame",
    method: "post",
    data: data,
  });
}

export function gameList(query) {
  return request({
    url: "/question/game/list",
    method: "get",
    params: query,
  });
}

export function getNextQuestion(data) {
  return request({
    url: "/question/answer",
    method: "post",
    data: data,
  });
}
