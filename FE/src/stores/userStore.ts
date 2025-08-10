import { create } from "zustand"

import type { IStudentSignin } from "@/types/student"

interface UserState {
  user: IStudentSignin | null
  // isLoading: boolean
  setUser: (user: IStudentSignin) => void
  resetUser: () => void
  updateUserTeamId: (teamId?: number | null) => void
  // initializeAuth: () => Promise<void>
}

// const getCookie = (name: string): string | null => {
//   const value = `; ${document.cookie}`
//   const parts = value.split(`; ${name}=`)
//   if (parts.length === 2) return parts.pop()?.split(';').shift() || null
//   return null
// }

export const useUserStore = create<UserState>((set) => ({
  user: null,
  // isLoading: true,
  setUser: (user) => set({ user }),
  resetUser: () => set({ user: null }),
  updateUserTeamId: (teamId) =>
    set((state) => ({
      user: state.user ? { ...state.user, teamId: teamId } : null,
    })),

  // initializeAuth: async () => {
  //   console.log('🔍 initializeAuth 시작')
  //   set({ isLoading: true })
  //   try {
  //     const sessionId = getCookie('JSESSIONID')
  //     console.log('🍪 JSESSIONID:', sessionId)
  //     if (sessionId) {
  //       console.log('📡 API 호출 시작')
  //       const response = await fetch('/api/auth/me', {
  //         method: 'GET',
  //         credentials: 'include'
  //       })

  //       console.log('📡 응답 상태:', response.status)
  //       if (response.ok) {
  //         const userData = await response.json()
  //         console.log('✅ 사용자 데이터:', userData)
  //         set({ user: userData, isLoading: false })
  //       } else {
  //         console.log('❌ API 응답 실패')
  //         set({ user: null, isLoading: false })
  //       }
  //     } else {
  //       console.log('❌ 세션 쿠키 없음')
  //       set({ user: null, isLoading: false })
  //     }
  //   } catch (error) {
  //     console.error('Auth initialization failed:', error)
  //     set({ user: null, isLoading: false })
  //   }
  // }
}))
