export type TeamName = "isTeam" | "notTeam"
export type PositionType = "프론트" | "풀스택" | "임베디드" | "인프라" | "백엔드" | "모바일" | "AI"

export interface IPositionRatioNode {
  name: TeamName
  count: number
}

export interface IPositionRatio {
  totalCount: number
  teamType: IPositionRatioNode[]
}

export type PositionRatioRecord = Record<PositionType, IPositionRatio>

export interface PositionRow {
  position: PositionType
  totalCount: number
  teamType: IPositionRatioNode[]
}
