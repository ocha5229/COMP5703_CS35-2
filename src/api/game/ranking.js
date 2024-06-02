import request from "@/utils/request";

export function rankingList(query) {
  return request({
    url: "/question/integral/list",
    method: "get",
    params: query,
  });
}

