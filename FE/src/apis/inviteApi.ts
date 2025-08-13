import type { IInvitationRequest } from "@/types/invitation"

import api from "./index"

const INVITE_BASE_URL = "/invitations"
export const inviteApi = {
  invite: async (payload: IInvitationRequest) => {
    const res = await api.post(INVITE_BASE_URL, payload)
    return res
  },
  cancel: async (notificationId: string) => {
    const res = await api.post(`${INVITE_BASE_URL}/${notificationId}/cancel`)
    return res
  },
  accept: async (notificationId: string) => {
    const res = await api.post(`${INVITE_BASE_URL}/${notificationId}/accept`)
    return res
  },
  reject: async (notificationId: string) => {
    const res = await api.post(`${INVITE_BASE_URL}/${notificationId}/reject`)
    return res
  },
}
