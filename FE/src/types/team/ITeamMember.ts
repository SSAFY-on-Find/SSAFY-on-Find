import type { ISubcode } from "../common/ISubcode"

export interface ITeamMember {
  studentId: number
  name: string
  major: string
  profileImageUrl: string
  position: ISubcode
}
