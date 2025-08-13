import { toast } from "react-toastify"
import { useQuery } from "@tanstack/react-query"

import { aiApi } from "@/apis/aiApi"
import { dashboardApi } from "@/apis/dashboardApi"
import type { IAiRecommend, PositionRow, PositionType } from "@/types/dashboard"

export const useSummaryInfo = () => {
  return useQuery({
    queryKey: ["dashboard-summaryInfo"],
    queryFn: async () => {
      const response = await dashboardApi.getMySummary()
      if (response.status !== "SUCCESS") {
        toast.error("프로필 요약 정보 조회에 실패했습니다.")
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
        toast.error("팀빌딩 현황 조회에 실패했습니다.")
        throw new Error("팀빌딩 현황 조회에 실패했습니다.")
      }
      return response.data
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const usePositionRatio = () => {
  return useQuery({
    queryKey: ["dashboard-positionRatio"],
    queryFn: async () => {
      const response = await dashboardApi.getPositionRatio()
      if (response.status !== "SUCCESS") {
        toast.error("포지션별 팀 빌딩 현황 조회에 실패했습니다.")
        throw new Error("포지션별 팀 빌딩 현황 조회에 실패했습니다.")
      }
      // Record -> Row[]
      const rows: PositionRow[] = Object.entries(response.data).map(([position, v]) => ({
        position: position as PositionType,
        totalCount: v.totalCount,
        teamType: v.teamType,
      }))
      return rows
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const useRecommendTeam = () => {
  return useQuery({
    queryKey: ["dashboard-recommendTeam"],
    queryFn: async () => {
      const response = await dashboardApi.getRecommendTeam()
      if (response.status !== "SUCCESS") {
        toast.error("추천 팀 목록 조회에 실패했습니다.")
        throw new Error("추천 팀 목록 조회에 실패했습니다.")
      }
      const d = response.data
      return Array.isArray(d) ? { items: d, message: null } : { items: [], message: d.message }
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const useAIRecommendations = (studentId: number | undefined) => {
  return useQuery<IAiRecommend[], Error>({
    // 1. queryKey: 이 쿼리의 고유 식별자입니다. studentId마다 캐시가 따로 관리됩니다.
    queryKey: ["aiRecommendations", studentId],

    // 2. queryFn: 실제 데이터 fetching 함수입니다.
    queryFn: () => aiApi.getRecommendations(studentId!),

    // 3. enabled: studentId가 있을 때만 쿼리가 실행되도록 합니다.
    enabled: !!studentId,

    // 4. staleTime: 데이터를 얼마 동안 'fresh'한 상태로 간주할지 설정합니다.
    // Infinity로 설정하면 한 번 가져온 데이터는 수동으로 무효화(invalidate)하기 전까지 다시 가져오지 않습니다.
    staleTime: Infinity,

    // 5. gcTime(cacheTime): 캐시에서 데이터가 얼마나 오래 머무를지 설정합니다. 60분!
    gcTime: 1000 * 60 * 60,
  })
}
