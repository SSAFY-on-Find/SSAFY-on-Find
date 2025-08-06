import type { IApiResponse } from "@/types/common"
import type { IStudentSignin } from "@/types/student"

import api from "./index"

const STUDENT_BASE_URL = "/students"
export const studentApi = {
  login: async (studentId: string): Promise<IApiResponse<IStudentSignin>> => {
    const response = await api.post<IApiResponse<IStudentSignin>>(STUDENT_BASE_URL + "/sign-in", { studentId })
    return response.data
  },
}
