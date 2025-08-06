import type { ITeamDetails } from "./ITeamDetails"
import type { ITeamRule } from "./ITeamRule"

export interface IMyTeam {
  teamInfo: ITeamDetails
  majorCount: number
  nonMajorCount: number
  ruleStatuses: ITeamRule[]
}
