import { useState } from "react"
import { toast } from "react-toastify"
import { useQueryClient } from "@tanstack/react-query"

import { favoriteApi } from "@/apis/favoriteApi"
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
          toast.success("팀 좋아요 성공!")
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
