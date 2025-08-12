import type { AxiosRequestConfig } from "axios"

import type { IAiRecommend } from "@/types/dashboard"

import api from "./index"

const AI_BASE_URL = "https://i13a704.p.ssafy.io/fastapi/v1/recommendations"

export const aiApi = {
  getRecommendations(studentId: number, config?: AxiosRequestConfig) {
    return api.post<IAiRecommend[]>(`${AI_BASE_URL}/${studentId}`, null, config).then((res) => res.data)
  },
}
