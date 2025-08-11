import { create } from "zustand"

import { notificationApi } from "@/apis/notificationApi"
import type { IGetTeamNotificationsParams, INotificationStatusResponseDto } from "@/types/notification"

// 스토어가 가질 상태(State) 정의
interface NotificationState {
  notifications: INotificationStatusResponseDto[]
  unReadCount: number
  isLoading: boolean
  error: string | null
}

// 스토어가 수행할 행동(Actions) 정의
interface NotificationActions {
  fetchTeamNotifications: (params: IGetTeamNotificationsParams) => Promise<void>
}

// 초기 상태
const initialState: NotificationState = {
  notifications: [],
  unReadCount: 0,
  isLoading: false,
  error: null,
}

export const useNotificationStore = create<NotificationState & NotificationActions>((set) => ({
  ...initialState,

  // 특정 팀의 알림을 가져오는 비동기 액션
  fetchTeamNotifications: async (params) => {
    // API 요청 시작 전, 로딩 상태 활성화 및 에러 초기화
    set({ isLoading: true, error: null })
    try {
      const response = await notificationApi.getTeamNotifications(params)
      // API 요청 성공 시, 상태 업데이트
      set({
        notifications: response.data.notificationStatusList,
        unReadCount: response.data.unReadCount,
        isLoading: false,
      })
    } catch (err) {
      // API 요청 실패 시, 에러 상태 업데이트
      const errorMessage = err instanceof Error ? err.message : "알림을 불러오는 데 실패했습니다."
      set({ error: errorMessage, isLoading: false })
    }
  },
}))
