import { create } from "zustand"

import type { IStudentSignin } from "@/types/student"

interface UserState {
  user: IStudentSignin | null
  setUser: (user: IStudentSignin) => void
  resetUser: () => void
}

export const useUserStore = create<UserState>((set) => ({
  user: null,
  setUser: (user) => set({ user }),
  resetUser: () => set({ user: null }),
}))
