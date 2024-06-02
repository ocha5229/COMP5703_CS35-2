import request from "@/utils/request";

export function gameTypeList(query) {
  return request({
    url: "/question/type/list",
    method: "get",
    params: query,
  });
}


export function addType(data) {
  return request({
    url: "/question/type",
    method: "post",
    data: data,
  });
}

export function getType(id) {
  return request({
    url: "/question/type/" + id,
    method: "get",
  });
}

export function addOptions(data) {
  return request({
    url: "/question/option",
    method: "post",
    data: data,
  });
}

export function updateType(data) {
  return request({
    url: "/question/type",
    method: "put",
    data: data,
  });
}

export function deleteType(id) {
  return request({
    url: "/question/type/" + id,
    method: "delete",
  });
}
