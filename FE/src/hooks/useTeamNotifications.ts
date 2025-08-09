import { useEffect } from "react"

import { useNotificationStore } from "@/stores/notificationStore"

/**
 * @description 팀 알림 데이터를 관리하고 가져오는 커스텀 훅
 * @param teamId 데이터를 조회할 팀 ID
 * @param type 알림 타입 ("send" 또는 "receive")
 * @returns 알림 목록, 읽지 않은 알림 수, 로딩 상태, 에러 정보 및 데이터 fetch 함수
 */
export const useTeamNotifications = (teamId: number, type: string) => {
  // Zustand 스토어에서 상태와 액션을 가져옵니다.
  const { notifications, unReadCount, isLoading, error, fetchTeamNotifications } = useNotificationStore()

  // teamId 또는 type이 변경될 때마다 알림 데이터를 다시 불러옵니다.
  useEffect(() => {
    // teamId가 유효한 경우에만 API를 호출합니다.
    if (teamId) {
      fetchTeamNotifications({ teamId, type })
    }
    // 의존성 배열에 fetchTeamNotifications를 추가하여 안정성을 높입니다.
  }, [teamId, type, fetchTeamNotifications])

  // 컴포넌트에서 사용할 상태와 액션을 반환합니다.
  return { notifications, unReadCount, isLoading, error, refetch: fetchTeamNotifications }
}
