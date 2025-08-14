import { useState } from "react"
import { toast } from "react-toastify"
import { useQueryClient } from "@tanstack/react-query"

import { favoriteApi } from "@/apis/favoriteApi"
import type { IStudentCard, IStudentInfo } from "@/types/student"
import type { ITeamCard } from "@/types/team"

type DashboardRecommendCache = ITeamCard[] | { items: ITeamCard[]; message: string | null } | undefined

export const useTeamFavoriteToggle = () => {
  const [loadingTeams, setLoadingTeams] = useState<Set<number>>(new Set())
  const queryClient = useQueryClient()

  const toggleFavorite = async (teamId: number) => {
    if (loadingTeams.has(teamId)) return
    setLoadingTeams((prev) => new Set(prev).add(teamId))

    const pickNextFavorite = (): boolean | undefined => {
      // teams 리스트에서 먼저 시도
      const teams = queryClient.getQueryData<ITeamCard[] | undefined>(["teams"])
      const fromTeams = teams?.find((t) => t.teamId === teamId)?.isFavorite
      if (typeof fromTeams === "boolean") return !fromTeams

      // 대시보드 추천에서 시도 (배열/객체 모두)
      const dash = queryClient.getQueryData<DashboardRecommendCache>(["dashboard", "recommendTeam"])
      if (Array.isArray(dash)) {
        const hit = dash.find((t) => t.teamId === teamId)?.isFavorite
        if (typeof hit === "boolean") return !hit
      } else if (dash && Array.isArray(dash.items)) {
        const hit = dash.items.find((t) => t.teamId === teamId)?.isFavorite
        if (typeof hit === "boolean") return !hit
      }

      return undefined
    }

    try {
      const response = await favoriteApi.doToggle(teamId)

      if (response.status === "SUCCESS") {
        // 1) 팀 목록 캐시 즉시 반영
        queryClient.setQueryData<ITeamCard[] | undefined>(["teams"], (old) =>
          old?.map((t) => (t.teamId === teamId ? { ...t, isFavorite: !t.isFavorite } : t))
        )

        // 2) 대시보드 추천 팀 캐시 즉시 반영
        queryClient.setQueryData<DashboardRecommendCache>(["dashboard", "recommendTeam"], (old) => {
          if (Array.isArray(old)) {
            return old.map((t) => (t.teamId === teamId ? { ...t, isFavorite: !t.isFavorite } : t))
          }
          if (old && Array.isArray(old.items)) {
            return {
              ...old,
              items: old.items.map((t) => (t.teamId === teamId ? { ...t, isFavorite: !t.isFavorite } : t)),
            }
          }
          return old
        })

        const next = pickNextFavorite()
        if (typeof next === "boolean") {
          toast.success(next ? "즐겨찾기에 해제했어요." : "즐겨찾기를 추가했어요.")
        } else {
          toast.success("즐겨찾기 상태가 변경되었습니다.")
        }
      } else {
        toast.error("좋아요 처리에 실패했습니다.")
      }
    } catch (error) {
      toast.error("네트워크 오류가 발생했습니다.")
    } finally {
      setLoadingTeams((prev) => {
        const newSet = new Set(prev)
        newSet.delete(teamId)
        return newSet
      })
    }
  }

  return {
    toggleFavorite,
    isLoading: (teamId: number) => loadingTeams.has(teamId),
  }
}

export const useStudentFavoriteToggle = () => {
  const [loadingStudents, setLoadingStudents] = useState<Set<number>>(new Set())
  const queryClient = useQueryClient()

  const toggleFavorite = async (targetStudentId: number) => {
    // 중복 클릭 방지
    if (loadingStudents.has(targetStudentId)) return

    setLoadingStudents((prev) => new Set(prev).add(targetStudentId))

    try {
      const res = await favoriteApi.studentToggle(targetStudentId)

      if (res.status === "SUCCESS") {
        // 리스트 캐시 업데이트: ["StudentList"]는 IStudentCard[] 배열이라고 가정
        queryClient.setQueryData<IStudentCard[] | undefined>(["StudentList"], (old) =>
          old?.map((s) => (s.student.studentId === targetStudentId ? { ...s, isFavorite: !s.isFavorite } : s))
        )

        // 상세 캐시 업데이트: ["StudentInfo", id]가 있다면 동기화
        queryClient.setQueryData<IStudentInfo | undefined>(["StudentInfo", targetStudentId], (old) =>
          old ? { ...old, isFavorite: !old.isFavorite } : old
        )

        // 필요 시 토스트
        toast.success(res.data.isFavorite ? "즐겨찾기에 추가했어요." : "즐겨찾기를 해제했어요.")
      } else {
        toast.error("즐겨찾기 처리에 실패했습니다.")
        // 실패 시 확실하게 동기화
        queryClient.invalidateQueries({ queryKey: ["StudentList"] })
        queryClient.invalidateQueries({ queryKey: ["StudentInfo", targetStudentId] })
      }
    } catch (e) {
      toast.error("네트워크 오류가 발생했습니다.")
    } finally {
      setLoadingStudents((prev) => {
        const next = new Set(prev)
        next.delete(targetStudentId)
        return next
      })
    }
  }

  return {
    toggleFavorite,
    isLoading: (id: number) => loadingStudents.has(id),
  }
}
