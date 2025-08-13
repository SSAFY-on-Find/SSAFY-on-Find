import { useQuery } from "@tanstack/react-query"

import { notificationApi } from "@/apis/notificationApi"
import type { INotification } from "@/types/notification"

export const useNotification = (type: string) =>
  useQuery<INotification[]>({
    queryKey: ["my-notification", type],
    queryFn: async () => {
      const res = await notificationApi.getNotifications({ type })
      if (res.status !== "SUCCESS") throw new Error("알림 목록 조회 실패")
      return res.data.notification
    },
    staleTime: 60_000,
    gcTime: 10 * 60_000,
    refetchOnWindowFocus: false,
  })

export const useTeamNotification = (teamId: number | null | undefined, type: string) =>
  useQuery<INotification[]>({
    queryKey: ["team-notification", teamId, type],
    queryFn: async () => {
      if (typeof teamId !== "number") return []
      const res = await notificationApi.getTeamNotifications({ teamId, type })
      if (res.status !== "SUCCESS") throw new Error("팀 알림 목록 조회 실패")
      return res.data.notification
    },
    enabled: typeof teamId === "number" && !!type,
    staleTime: 60_000,
    gcTime: 10 * 60_000,
    refetchOnWindowFocus: false,
  })
