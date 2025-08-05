import type { IRecruitment } from "../common/IRecruitment"
import type { ISubcode } from "../common/ISubcode"

export interface ITeamCard {
  teamId: number
  teamName: string
  description: string
  track: ISubcode
  memberProfileImages: string[]
  recruitments: IRecruitment[]
  isRecruitingComplete: boolean
}
