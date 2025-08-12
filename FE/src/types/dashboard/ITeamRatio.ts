type MajorType = "전공" | "비전공" | "전체"

interface ITeamRatioNode {
  type: MajorType
  totalStudentCount: number
  teamMemberCount: number
}

export interface ITeamRatio {
  all: ITeamRatioNode
  major: ITeamRatioNode
  nonMajor: ITeamRatioNode
}
