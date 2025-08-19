import { create } from "zustand"

interface INotificationState {
  unread: number
  isBlinking: boolean
  isOpen: boolean
  bump: (delta?: number) => void
  reset: () => void
  setOpen: (open: boolean) => void
}

export const useNotificationStore = create<INotificationState>((set, get) => ({
  unread: 0,
  isBlinking: false,
  isOpen: false,

  // 알림 도착
  bump: (delta = 1) => {
    const { isOpen, unread } = get()
    set({
      unread: unread + delta,
      isBlinking: isOpen ? false : true, // 열려있으면 깜빡임 OFF
    })
  },

  // 읽음 처리(열 때 호출)
  reset: () => set({ unread: 0, isBlinking: false }),

  // 열림/닫힘 상태 동기화
  setOpen: (open) =>
    set((s) => ({
      isOpen: open,
      // 열리면 깜빡임 끔, 닫히면 유지
      isBlinking: open ? false : s.isBlinking,
    })),
}))
