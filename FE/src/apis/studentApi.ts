import type { IApiResponse } from "@/types/common"

import api from "./index"

const STUDENT_BASE_URL = "/students"
export const studentApi = {
  login: async (studentId: string): Promise<IApiResponse<void>> => {
    const response = await api.post<IApiResponse<void>>(STUDENT_BASE_URL + "/sign-in", { studentId })
    return response.data
  },
}
