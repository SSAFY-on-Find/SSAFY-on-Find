import type { IApiResponse } from "@/types/common"
import type { ITeamFavorite } from "@/types/team"

import api from "./index"

const FAVORITE_BASE_URL = "/favorites"
export const favoriteApi = {
  doToggle: async (teamId: number): Promise<IApiResponse<ITeamFavorite[]>> => {
    const response = await api.post<IApiResponse<ITeamFavorite[]>>(FAVORITE_BASE_URL + "/teams/toggle", { teamId })
    return response.data
  },
  studentToggle: async (targetStudentId: number): Promise<IApiResponse<{ isFavorite: boolean }>> => {
    const response = await api.post<IApiResponse<{ isFavorite: boolean }>>(FAVORITE_BASE_URL + "/students/toggle", {
      targetStudentId,
    })
    return response.data
  },
}
