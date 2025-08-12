import type { ISubcode } from "@/types/common"
import type { IStudent } from "@/types/student"
import type { ITeamInfo } from "@/types/team"

export interface IProfileSummary {
  student: IStudent
  position: ISubcode
  track: ISubcode
  profileImageUrl: string
  team: ITeamInfo
}
