import type { IApiResponse } from "@/types/common"
import type { IStudentSignin } from "@/types/student"

import api from "./index"

const AUTH_BASE_URL = "/me/auth"
export const authApi = {
  getAuth: async (): Promise<IApiResponse<IStudentSignin>> => {
    const response = await api.get<IApiResponse<IStudentSignin>>(AUTH_BASE_URL, {
      headers: { "Cache-Control": "no-store" },
    })
    return response.data
  },
}
