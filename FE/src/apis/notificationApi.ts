import type { IApiResponse } from "@/types/common"
import type { INotification } from "@/types/notification"

import api from "./index"

type NotificationPayload = { notification: INotification[] }

const NOTIFICATION_BASE_URL = "/notifications"
export const notificationApi = {
  getTeamNotifications: async ({ teamId, type }: { teamId: number; type: string }) => {
    const { data } = await api.get<IApiResponse<NotificationPayload>>(`${NOTIFICATION_BASE_URL}/${teamId}`, {
      params: { type },
    })
    return data
  },
  getNotifications: async ({ type }: { type: string }) => {
    const { data } = await api.get<IApiResponse<NotificationPayload>>(`${NOTIFICATION_BASE_URL}`, { params: { type } })
    return data
  },
}
