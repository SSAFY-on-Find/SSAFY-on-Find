import { create } from "zustand"

interface INotificationState {
  unread: number
  isBlinking: boolean
  bump: (delta?: number) => void
  reset: () => void
}

export const useNotificationStore = create<INotificationState>((set) => ({
  unread: 0,
  isBlinking: false,
  bump: (delta = 1) =>
    set((s) => ({
      unread: s.unread + delta,
      isBlinking: true,
    })),
  reset: () => set({ unread: 0, isBlinking: false }),
}))
