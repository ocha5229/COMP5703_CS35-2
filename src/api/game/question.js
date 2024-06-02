import request from "@/utils/request";

export function getTemplateById(gameId) {
  return request({
    url: "/question/game/" + gameId,
    method: "get",
  });
}
