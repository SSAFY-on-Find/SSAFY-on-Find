import type { IApiResponse } from "@/types/common"
import type { ITeamRatio, PositionRatioRecord } from "@/types/dashboard"
import type { IProfileSummary } from "@/types/profile/IProfileSummary"
import type { ITeamCard } from "@/types/team"

import api from "./index"

const DASHBOARD_BASE_URL = "/dashboard"
type TeamMessage = { message: string }
type TeamRecommendApiData = ITeamCard[] | TeamMessage
export const dashboardApi = {
  getMySummary: async (): Promise<IApiResponse<IProfileSummary>> => {
    const response = await api.get<IApiResponse<IProfileSummary>>("/me/summary")
    return response.data
  },
  getTeamRatio: async (): Promise<IApiResponse<ITeamRatio>> => {
    const response = await api.get<IApiResponse<ITeamRatio>>(DASHBOARD_BASE_URL + "/team-ratio")
    return response.data
  },
  getPositionRatio: async (): Promise<IApiResponse<PositionRatioRecord>> => {
    const response = await api.get<IApiResponse<PositionRatioRecord>>(DASHBOARD_BASE_URL + "/position-ratio")
    return response.data
  },
  getRecommandTeam: async (): Promise<IApiResponse<TeamRecommendApiData>> => {
    const response = await api.get<IApiResponse<TeamRecommendApiData>>(DASHBOARD_BASE_URL + "/team-recommend")
    return response.data
  },
}
