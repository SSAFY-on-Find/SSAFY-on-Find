import { useMutation, useQuery } from "@tanstack/react-query"

import { aiApi } from "@/apis/aiApi"
import { dashboardApi } from "@/apis/dashboardApi"
import type { IAiRecommend, PositionRow, PositionType } from "@/types/dashboard"

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

export const usePositionRatio = () => {
  return useQuery({
    queryKey: ["dashboard-positionRatio"],
    queryFn: async () => {
      const response = await dashboardApi.getPositionRatio()
      if (response.status !== "SUCCESS") {
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
        throw new Error("추천 팀 목록 조회에 실패했습니다.")
      }
      const d = response.data
      return Array.isArray(d) ? { items: d, message: null } : { items: [], message: d.message }
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const useAIRecommendations = () =>
  useMutation<IAiRecommend[], Error, number>({
    mutationFn: (studentId) => aiApi.getRecommendations(studentId),
  })
