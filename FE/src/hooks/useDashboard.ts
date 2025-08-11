import { useQuery } from "@tanstack/react-query"

import { dashboardApi } from "@/apis/dashboardApi"

export const useSummaryInfo = () => {
  return useQuery({
    queryKey: ["dashboard-summaryInfo"],
    queryFn: async () => {
      const response = await dashboardApi.getMySummary()
      if (response.status !== "SUCCESS") {
        throw new Error("프로필 요약 정보 조회에 실패했습니다.")
      }
      return response.data
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const useTeamRatio = () => {
  return useQuery({
    queryKey: ["dashboard-teamRatio"],
    queryFn: async () => {
      const response = await dashboardApi.getTeamRatio()
      if (response.status !== "SUCCESS") {
        throw new Error("팀빌딩 현황 조회에 실패했습니다.")
      }
      return response.data
    },
    gcTime: 10 * 60 * 1000,
  })
}
