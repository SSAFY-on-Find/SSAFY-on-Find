import { useQuery } from "@tanstack/react-query"

import { profileApi } from "@/apis/profileApi"

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
