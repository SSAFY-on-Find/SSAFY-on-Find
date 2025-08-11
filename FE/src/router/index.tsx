import { Navigate, useLocation } from "react-router-dom"

import { useUserStore } from "@/stores/userStore"

export function RoutePolicy({ children }: { children: React.ReactNode }) {
  const { user } = useUserStore()
  const location = useLocation()

  // if (isLoading) {
  //   return <div>로딩 중...</div>
  // }

  // 1. 로그인 안했으면 로그인 페이지로 강제 이동
  if (!user && location.pathname !== "/login") {
    return <Navigate to="/login" replace />
  }

  // 2. 로그인했고, isCreatedStudentInfo === false면 자기소개 생성 페이지로 이동 (예: /create-profile)
  if (user && !user.isCreatedStudentInfo && location.pathname !== "/create-profile") {
    return <Navigate to="/create-profile" replace />
  }

  // 3. 모두 통과 시 자식 라우트 렌더
  return <>{children}</>
}
