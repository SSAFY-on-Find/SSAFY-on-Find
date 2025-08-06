import type { IRecruitment } from "../common/IRecruitment"
import type { ISubcode } from "../common/ISubcode"

import type { ITeamMember } from "./ITeamMember"

export interface ITeamDetails {
  teamName: string
  teamDescription: string
  teamTrack: ISubcode
  majorCount: number
  nonMajorCount: number
  teamCount: number
  positions: IRecruitment[]
  members: ITeamMember[]
  isFavorite: boolean
}
