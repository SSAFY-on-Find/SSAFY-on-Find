export default interface ITeamDetail {
  id: string
  name: string
  description: string
  track: string
  position: string[]
  maxMembers: number
  currentMembers: number
  members: Array<{
    id: string
    name: string
    position: string
    major: string
  }>
}
