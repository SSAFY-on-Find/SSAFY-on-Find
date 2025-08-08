import { useMutation, useQuery } from "@tanstack/react-query"

import { profileApi } from "@/apis/profileApi"
import type { IProfileState } from "@/stores/profileStroe"

export const useProfileCodes = () => {
  return useQuery({
    queryKey: ["profile-codes"],
    queryFn: async () => {
      const res = await profileApi.getCode()
      if (res.status !== "SUCCESS") throw new Error("코드 목록 조회 실패")
      return res.data
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const useCreateProfile = () => {
  return useMutation({
    mutationFn: async (profile: IProfileState) => {
      try {
        const res = await profileApi.createProfile(profile)
        if (res.status !== "SUCCESS") {
          throw new Error("자기소개 등록 실패")
        }
        return res
      } catch (error) {
        if (error instanceof Error) {
          throw new Error(error.message)
        }
        throw new Error("자기소개 등록 중 오류가 발생했습니다.")
      }
    },
  })
}

export const useGetProfile = () => {
  return useQuery({
    queryKey: ["profile-detail"],
    queryFn: async () => {
      const res = await profileApi.getProfile()
      if (res.status !== "SUCCESS") throw new Error("자기소개 조회 실패")
      return res.data
    },
    gcTime: 10 * 60 * 1000,
  })
}

export const useEditProfile = () => {
  return useMutation({
    mutationFn: async (profile: IProfileState) => {
      try {
        const res = await profileApi.editProfile(profile)
        if (res.status !== "SUCCESS") {
          throw new Error("자기소개 수정 실패")
        }
        return res
      } catch (error) {
        if (error instanceof Error) {
          throw new Error(error.message)
        }
        throw new Error("자기소개 수정 중 오류가 발생했습니다.")
      }
    },
  })
}
