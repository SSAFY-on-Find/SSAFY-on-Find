import type { IFile, ISubcode } from "@/types/common"
import type { IStudent } from "@/types/student"
import type { ITeamInfo } from "@/types/team"

export interface IStudentInfo {
  student: IStudent
  position: ISubcode
  track: ISubcode
  goal: ISubcode
  mbti: ISubcode
  techStack: ISubcode[]
  strength: string[]
  description: string
  profileImageUrl: string
  portfolio: IFile
  isFavorite: boolean
  teamInfo: ITeamInfo
}
