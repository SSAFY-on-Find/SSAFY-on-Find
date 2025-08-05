import type { IRecruitment } from "../common/IRecruitment"
import type { ISubcode } from "../common/ISubcode"

import type { ITeamMember } from "./ITeamCard"

export interface ITeamDetails {
  teamName: string
  teamDescription: string
  teamTrack: ISubcode
  teamCount: number
  position: IRecruitment[]
  members: ITeamMember[]
}
