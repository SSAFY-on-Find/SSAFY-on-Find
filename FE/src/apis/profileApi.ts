import type { IApiResponse } from "@/types/common"
import type { IProfileCode } from "@/types/profile"

import api from "./index"

const PROFILE_BASE_URL = "/me"
export const profileApi = {
  getCode: async (): Promise<IApiResponse<IProfileCode>> => {
    const response = await api.get<IApiResponse<IProfileCode>>(PROFILE_BASE_URL + "/warm-up")
    return response.data
  },
}
