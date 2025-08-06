import { create } from "zustand"

interface ITeamState {
  isDetailModalOpen: boolean
  selectedTeamId: number | null

  openDetailModal: (teamId: number) => void
  closeDetailModal: () => void
}

export const useTeamStore = create<ITeamState>((set) => ({
  isDetailModalOpen: false,
  selectedTeamId: null,

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
