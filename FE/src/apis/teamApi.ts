import type { IApiResponse } from "@/types/common"
import type { IMyTeam, ITeamCard, ITeamDetails } from "@/types/team"

import api from "./index"

const TEAM_BASE_URL = "/teams"
export const teamApi = {
  getTeams: async (): Promise<IApiResponse<ITeamCard[]>> => {
    const response = await api.get<IApiResponse<ITeamCard[]>>(TEAM_BASE_URL)
    return response.data
  },
  getMyTeam: async (): Promise<IApiResponse<IMyTeam>> => {
    const response = await api.get<IApiResponse<IMyTeam>>(TEAM_BASE_URL + "/me")
    return response.data
  },
  getTeamDetails: async (teamId: number): Promise<IApiResponse<ITeamDetails>> => {
    const response = await api.get<IApiResponse<ITeamDetails>>(TEAM_BASE_URL + "/" + teamId)
    return response.data
  },
}
