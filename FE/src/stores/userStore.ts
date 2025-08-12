import { create } from "zustand"

import type { IStudentSignin } from "@/types/student"

interface UserState {
  user: IStudentSignin | null
  setUser: (user: IStudentSignin) => void
  resetUser: () => void
  updateUserTeamId: (teamId?: number | null) => void
  markProfileCreated: () => void
}

export const useUserStore = create<UserState>((set) => ({
  user: null,
  setUser: (user) => set({ user }),
  resetUser: () => set({ user: null }),
  updateUserTeamId: (teamId) =>
    set((state) => ({
      user: state.user ? { ...state.user, teamId: teamId } : null,
    })),
  markProfileCreated: () => set((s) => (s.user ? { user: { ...s.user, isCreatedStudentInfo: true } } : s)),
}))
