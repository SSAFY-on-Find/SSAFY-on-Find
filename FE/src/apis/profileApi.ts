import type { IProfileState } from "@/stores/profileStore"
import type { IApiResponse } from "@/types/common"
import type { IProfileCode } from "@/types/profile"

import api from "./index"

const PROFILE_BASE_URL = "/me"
export const profileApi = {
  getCode: async (): Promise<IApiResponse<IProfileCode>> => {
    const response = await api.get<IApiResponse<IProfileCode>>(PROFILE_BASE_URL + "/warm-up")
    return response.data
  },
  createProfile: async (profile: IProfileState): Promise<IApiResponse<void>> => {
    const formData = new FormData()

    const data = {
      position: profile.position?.subcode ?? "",
      track: profile.track?.subcode ?? "",
      techStack: profile.techStack.map((item) => item.subcode),
      goal: profile.goal?.subcode ?? "",
      mbti: profile.mbti?.subcode ?? "",
      strength: profile.strength,
      description: profile.description ?? "",
    }

    formData.append("requestDto", new Blob([JSON.stringify(data)], { type: "application/json" }))

    if (profile.profileImageFile) {
      formData.append("profile", profile.profileImageFile)
    }
    if (profile.portfolioFile) {
      formData.append("portfolio", profile.portfolioFile)
    }

    const response = await api.post<IApiResponse<void>>(PROFILE_BASE_URL, formData)
    return response.data
  },
  getProfile: async (): Promise<IApiResponse<IProfileState>> => {
    const response = await api.get<IApiResponse<IProfileState>>(PROFILE_BASE_URL + "/details")
    return response.data
  },
  editProfile: async (profile: IProfileState): Promise<IApiResponse<void>> => {
    const formData = new FormData()

    const data = {
      position: profile.position?.subcode ?? "",
      track: profile.track?.subcode ?? "",
      techStack: profile.techStack.map((item) => item.subcode),
      goal: profile.goal?.subcode ?? "",
      mbti: profile.mbti?.subcode ?? "",
      strength: profile.strength,
      description: profile.description ?? "",
    }

    formData.append("requestDto", new Blob([JSON.stringify(data)], { type: "application/json" }))

    if (profile.profileImageFile) {
      formData.append("profile", profile.profileImageFile)
    }
    if (profile.portfolioFile) {
      formData.append("portfolio", profile.portfolioFile)
    }

    const response = await api.patch<IApiResponse<void>>(PROFILE_BASE_URL, formData)
    return response.data
  },
}
