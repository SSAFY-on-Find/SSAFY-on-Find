import { useState } from "react"
import { toast } from "react-toastify"
import { useQueryClient } from "@tanstack/react-query"

import { favoriteApi } from "@/apis/favoriteApi"
import type { IStudentCard, IStudentInfo } from "@/types/student"
import type { ITeamCard } from "@/types/team"
import { sortTeamsByFavorite } from "@/utils"

export const useTeamFavoriteToggle = () => {
  const [loadingTeams, setLoadingTeams] = useState<Set<number>>(new Set())
  const queryClient = useQueryClient()

  const toggleFavorite = async (teamId: number) => {
    if (loadingTeams.has(teamId)) return

    setLoadingTeams((prev) => new Set(prev).add(teamId))

    try {
      const response = await favoriteApi.doToggle(teamId)

      if (response.status === "SUCCESS") {
        queryClient.setQueryData(["teams"], (oldData: ITeamCard[]) => {
          // toast.success("팀 좋아요 성공!")
          const updatedData = oldData?.map((team: ITeamCard) =>
            team.teamId === teamId ? { ...team, isFavorite: !team.isFavorite } : team
          )
          return sortTeamsByFavorite(updatedData)
        })
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
