import type { ISubcode } from "../common/ISubcode"

import type { IStudent } from "./IStudent"

export interface IStudentCard {
  student: IStudent
  position: ISubcode
  track: ISubcode
  goal: ISubcode
  profileImageUrl: string
  isFavorite: boolean
  teamName: string
}
