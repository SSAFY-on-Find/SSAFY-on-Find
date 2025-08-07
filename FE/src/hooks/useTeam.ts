import { toast } from "react-toastify"
import { useMutation, useQuery } from "@tanstack/react-query"

import { teamApi } from "@/apis/teamApi"
import type { ITeamCreate } from "@/types/team"
import { sortTeamsByFavorite } from "@/utils"

export const useTeams = () => {
  return useQuery({
    queryKey: ["teams"],
    queryFn: async () => {
      try {
        const response = await teamApi.getTeams()
        if (response.status !== "SUCCESS") {
          throw new Error("팀 목록 조회에 실패하였습니다.")
        }
        return response.data
      } catch (error) {
        if (error instanceof Error) {
          throw new Error(error.message)
        }
        throw new Error("팀 목록을 불러오는 중 오류가 발생했습니다.")
      }
    },
    select: (data) => sortTeamsByFavorite(data),
    gcTime: 10 * 60 * 1000,
  })
}

export const useMyTeam = () => {
  return useQuery({
    queryKey: ["myTeam"],
    queryFn: async () => {
      try {
        const response = await teamApi.getMyTeam()
        if (response.status !== "SUCCESS") {
          throw new Error("내 팀 정보 조회에 실패했습니다.")
        }
        return response.data
      } catch (error) {
        if (error instanceof Error) {
          throw new Error(error.message)
        }
        throw new Error("내 팀 정보를 불러오는 중 오류가 발생했습니다.")
      }
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const useTeamDetails = (teamId: number) => {
  return useQuery({
    queryKey: ["TeamDetails", teamId],
    queryFn: async () => {
      try {
        const response = await teamApi.getTeamDetails(teamId)
        if (response.status !== "SUCCESS") {
          throw new Error("팀 정보 조회에 실패했습니다.")
        }
        return response.data
      } catch (error) {
        if (error instanceof Error) {
          throw new Error(error.message)
        }
        throw new Error("팀 정보를 불러오는데 오류가 발생했습니다.")
      }
    },
    gcTime: 10 * 60 * 1000,
    enabled: !!teamId,
  })
}

export const useCreateTeam = () => {
  return useMutation({
    mutationFn: async (createTeamDto: ITeamCreate) => {
      const response = await teamApi.createTeam(createTeamDto)
      return response.data
    },
    onSuccess: () => {
      toast.success("팀 생성 성공")
    },
    onError: (error) => {
      toast.error("팀 생성 실패!")
      console.log("팀 생성 실패 오류: ", error)
    },
  })
}
export const useTeamWarmup = () => {
  return useQuery({
    queryKey: ["team-warmup"],
    queryFn: async () => {
      try {
        const response = await teamApi.getWarmup()
        if (response.status !== "SUCCESS") {
          throw new Error("팀 생성 웜업에 실패했습니다.")
        }
        return response.data
      } catch (error) {
        if (error instanceof Error) {
          throw new Error(error.message)
        }
        throw new Error("팀 생성 웜업에 오류가 발생했습니다.")
      }
    },
    gcTime: 10 * 60 * 1000,
  })
}
