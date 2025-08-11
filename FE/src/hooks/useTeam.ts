import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"

import { teamApi } from "@/apis/teamApi"
import { useUserStore } from "@/stores/userStore"
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
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { updateUserTeamId } = useUserStore.getState()

  return useMutation({
    mutationFn: async (createTeamDto: ITeamCreate) => {
      const response = await teamApi.createTeam(createTeamDto)
      return response.data
    },
    onSuccess: (data) => {
      toast.success("팀 생성 성공")
      queryClient.invalidateQueries({ queryKey: ["myTeam"] })
      queryClient.invalidateQueries({ queryKey: ["teams"] })
      if (data && data.teamId) {
        updateUserTeamId(data.teamId)
      }

      navigate("/myteam")
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

export const useUpdateTeam = () => {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { updateUserTeamId } = useUserStore.getState()

  return useMutation({
    mutationFn: async ({ teamId, updateTeamDto }: { teamId: number; updateTeamDto: ITeamCreate }) => {
      const response = await teamApi.updateTeam(teamId, updateTeamDto)
      return response.data
    },
    onSuccess: (data) => {
      toast.success("팀 수정 성공")
      queryClient.invalidateQueries({ queryKey: ["myTeam"] })
      queryClient.invalidateQueries({ queryKey: ["teams"] })
      if (data && data.teamId) {
        updateUserTeamId(data.teamId)
      }

      navigate("/myteam")
    },
    onError: (error) => {
      toast.error("팀 수정 실패!")
      console.log("팀 수정 실패 오류: ", error)
    },
  })
}
export const useLeaveTeam = () => {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { updateUserTeamId } = useUserStore.getState()

  return useMutation({
    mutationFn: teamApi.leaveTeam,
    onSuccess: (response) => {
      toast.success(response.data.message || "팀에서 성공적으로 탈퇴했습니다.")
      queryClient.setQueryData(["myTeam"], null)
      queryClient.invalidateQueries({ queryKey: ["myTeam"] })
      queryClient.invalidateQueries({ queryKey: ["teams"] })
      updateUserTeamId(null)
      navigate("/teamlist")
    },
    onError: (error) => {
      if (error) {
        toast.error(error.message || "팀 탈퇴에 실패했습니다.")
      } else {
        toast.error("팀 탈퇴 중 오류가 발생했습니다.")
      }
      console.error("팀 탈퇴 실패 오류: ", error)
    },
  })
}
