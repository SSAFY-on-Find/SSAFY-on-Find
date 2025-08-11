import type { IApiResponse } from "@/types/common"
import type { IGetTeamNotificationsParams, INotification } from "@/types/notification"

import api from "./index"

const STUDENT_BASE_URL = "/notifications"
export const notificationApi = {
  getTeamNotifications: async ({ teamId, type }: IGetTeamNotificationsParams): Promise<IApiResponse<INotification>> => {
    const response = await api.get<IApiResponse<INotification>>(`${STUDENT_BASE_URL}/${teamId}`, {
      params: {
        type: type,
      },
    })
    return response.data
  },
}
