import type { IApiResponse } from "@/types/common"
import type { IInvitationRequest } from "@/types/invitation"

import api from "./index"

const INVITE_BASE_URL = "/invitations"
export const inviteApi = {
  invite: async (payload: IInvitationRequest): Promise<IApiResponse<void>> => {
    const response = await api.post<IApiResponse<void>>(INVITE_BASE_URL, payload)
    return response.data
  },
  cancel: async (notificationId: string): Promise<IApiResponse<void>> => {
    const { data } = await api.post<IApiResponse<void>>(`${INVITE_BASE_URL}/${notificationId}/cancel`)
    return data
  },
  accept: async (notificationId: string): Promise<IApiResponse<void>> => {
    const { data } = await api.post<IApiResponse<void>>(`${INVITE_BASE_URL}/${notificationId}/accept`)
    return data
  },
  reject: async (notificationId: string): Promise<IApiResponse<void>> => {
    const { data } = await api.post<IApiResponse<void>>(`${INVITE_BASE_URL}/${notificationId}/reject`)
    return data
  },
}
