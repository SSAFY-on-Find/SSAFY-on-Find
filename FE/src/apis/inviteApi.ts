import type { IApiResponse } from "@/types/common"
import type { IInvitationRequest } from "@/types/invitation"

import api from "./index"

const INVITE_BASE_URL = "/invitations"
export const inviteApi = {
  invite: async (payload: IInvitationRequest): Promise<IApiResponse<void>> => {
    const response = await api.post<IApiResponse<void>>(INVITE_BASE_URL, payload)
    return response.data
  },
}
