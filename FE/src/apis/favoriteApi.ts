import type { IApiResponse } from "@/types/common"
import type { ITeamFavorite } from "@/types/team"

import api from "./index"

const FAVORITE_BASE_URL = "/favorites"
export const favoriteApi = {
  doToggle: async (teamId: number): Promise<IApiResponse<ITeamFavorite[]>> => {
    const response = await api.post<IApiResponse<ITeamFavorite[]>>(FAVORITE_BASE_URL + "/toggle", { teamId })
    return response.data
  },
}
