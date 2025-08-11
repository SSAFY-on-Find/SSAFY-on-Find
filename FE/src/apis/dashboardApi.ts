import type { IApiResponse } from "@/types/common"
import type { IProfileSummary } from "@/types/profile/IProfileSummary"

import api from "./index"

// const STUDENT_BASE_URL = "/students"
export const dashboardApi = {
  getMySummary: async (): Promise<IApiResponse<IProfileSummary>> => {
    const response = await api.get<IApiResponse<IProfileSummary>>("/me/summary")
    return response.data
  },
}
