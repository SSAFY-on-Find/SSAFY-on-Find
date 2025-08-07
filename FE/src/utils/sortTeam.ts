import type { ITeamCard } from "@/types/team"

export const sortTeamsByFavorite = (teams: ITeamCard[]) => {
  return teams?.sort((a, b) => {
    if (a.isFavorite && !b.isFavorite) return -1
    if (!a.isFavorite && b.isFavorite) return 1
    return 0
  })
}
