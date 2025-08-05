import { create } from "zustand"

interface ITeamState {
  favoriteTeams: number[]
  isDetailModalOpen: boolean
  selectedTeamId: number | null

  toggleFavorite: (teamId: number) => void
  openDetailModal: (teamId: number) => void
  closeDetailModal: () => void
}

export const useTeamStore = create<ITeamState>((set) => ({
  favoriteTeams: [],
  isDetailModalOpen: false,
  selectedTeamId: null,

  toggleFavorite: (teamId: number) =>
    set((state) => ({
      favoriteTeams: state.favoriteTeams.includes(teamId)
        ? state.favoriteTeams.filter((id) => id !== teamId)
        : [...state.favoriteTeams, teamId],
    })),

  openDetailModal: (teamId: number) =>
    set({
      isDetailModalOpen: true,
      selectedTeamId: teamId,
    }),

  closeDetailModal: () =>
    set({
      isDetailModalOpen: false,
      selectedTeamId: null,
    }),
}))
