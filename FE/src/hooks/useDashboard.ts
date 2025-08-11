import { useQuery } from "@tanstack/react-query"

import { dashboardApi } from "@/apis/dashboardApi"

export const useSummaryInfo = () => {
  return useQuery({
    queryKey: ["summaryInfo"],
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
