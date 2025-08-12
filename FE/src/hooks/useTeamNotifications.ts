import { useEffect } from "react"

import { useNotificationStore } from "@/stores/notificationStore"

export const useTeamNotifications = (teamId: number, type: string) => {
  const { notifications, unReadCount, isLoading, error, fetchTeamNotifications } = useNotificationStore()

  useEffect(() => {
    if (teamId) {
      fetchTeamNotifications({ teamId, type })
    }
  }, [teamId, type])

  return { notifications, unReadCount, isLoading, error, refetch: fetchTeamNotifications }
}
