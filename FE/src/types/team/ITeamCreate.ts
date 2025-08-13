export interface ITeamCreate {
  description: string
  track: string
  positions?: string[]
}

export interface ITeamCreateResponse {
  teamId: number
}
