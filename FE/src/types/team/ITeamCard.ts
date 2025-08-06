import type { IRecruitment } from "../common/IRecruitment"
import type { ISubcode } from "../common/ISubcode"

import type { ITeamMember } from "./ITeamMember"

export interface ITeamCard {
  teamId: number
  teamName: string
  teamDescription: string
  track: ISubcode
  recruitments: IRecruitment[]
  members: ITeamMember[]
  isRecruitingComplete: boolean
  isFavorite?: boolean
  onClickFavorite: () => void
  onClickCard: () => void
  variant?: "default" | "main"
}
