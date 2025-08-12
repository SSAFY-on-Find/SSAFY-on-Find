import { useEffect } from "react"
import { Navigate, Route, Routes, useLocation } from "react-router-dom"
import { ToastContainer } from "react-toastify"

import Loading from "@/components/templates/Loading"
import { useAuth } from "@/hooks/useStudent"
import { useNotificationStream } from "@/hooks/useSubscribe"
import { Header, SideBar } from "@/layout"
import CreateProfile from "@/pages/CreateProfilePage"
import Dashboard from "@/pages/DashboardPage"
import EditProfile from "@/pages/EditProfilePage"
import Login from "@/pages/LoginPage"
import MyProfile from "@/pages/MyProfilePage"
import MyTeam from "@/pages/MyTeamPage"
import StudentDetail from "@/pages/StudentDetailPage"
import StudentList from "@/pages/StudentListPage"
import TeamCreatePage from "@/pages/TeamCreatePage"
import TeamList from "@/pages/TeamListPage"
import { useUserStore } from "@/stores/userStore"

import ChatView from "./components/templates/ChatView"
import TeamEditPage from "./pages/TeamUpdatePage"
import { useChatViewStore } from "./stores/useChatViewStore"
import ScrollToTop from "./utils/ScrollToTop"

import "@/index.css"

function App() {
  const location = useLocation()
  const hideLayout = location.pathname === "/login"

  // 1) 세션 확인
  const { data: authUser, isLoading, isError } = useAuth()
  const setUser = useUserStore((s) => s.setUser)
  const resetUser = useUserStore((s) => s.resetUser)
  // 사이드바와 1대1 채팅을 위한 라우트
  const { activeRoomId, activeRoomType } = useChatViewStore()
  useEffect(() => {
    if (authUser) setUser(authUser)
    if (isError) resetUser()
  }, [authUser, isError, setUser, resetUser])

  const sseEnabled = !!authUser && authUser.isCreatedStudentInfo
  useNotificationStream(sseEnabled)

  // 2) 로딩 화면
  if (isLoading) {
    return <Loading text="세션 확인 중..." fullScreen />
  }

  // 3) 비로그인: 로그인 라우트만 노출
  if (!authUser) {
    return (
      <>
        <Routes>
          <Route path="/login" element={<Login />} />
          {/* 그 외는 전부 로그인으로 */}
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
        <ToastContainer position="bottom-right" autoClose={3000} theme="light" />
      </>
    )
  }

  // 4) 로그인 O + 프로필 미작성: /create-profile만 허용
  if (!authUser.isCreatedStudentInfo) {
    return (
      <>
        {!hideLayout && <Header />}
        <div className={!hideLayout ? "mt-[64px]" : ""}>
          {!hideLayout && <SideBar />}
          <main className={!hideLayout ? "ml-[260px]" : ""}>
            <Routes>
              <Route path="/create-profile" element={<CreateProfile />} />
              {/* 다른 경로는 전부 create-profile로 돌리기 */}
              <Route path="*" element={<Navigate to="/create-profile" replace />} />
            </Routes>
          </main>
        </div>
        <ToastContainer position="bottom-right" autoClose={3000} theme="light" />
      </>
    )
  }

  // 5) 로그인 O + 프로필 작성 완료: 전체 앱 라우트
  return (
    <>
      <ScrollToTop />
      {!hideLayout && <Header />}
      <div className={!hideLayout ? "mt-[64px]" : ""}>
        {!hideLayout && <SideBar />}
        <main className={!hideLayout ? "ml-[260px]" : ""}>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/myteam" element={<MyTeam />} />
            <Route path="/teamlist" element={<TeamList />} />
            <Route path="/create-team" element={<TeamCreatePage />} />
            <Route path="/edit-team" element={<TeamEditPage />} />
            <Route path="/studentlist" element={<StudentList />} />
            <Route path="/studentlist/:studentId" element={<StudentDetail />} />
            <Route path="/myprofile" element={<MyProfile />} />
            <Route path="/create-profile" element={<CreateProfile />} />
            <Route path="/edit-profile" element={<EditProfile />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>
        {activeRoomId && activeRoomType === "direct" && <ChatView />}
      </div>
      <ToastContainer position="bottom-right" autoClose={3000} theme="light" />
    </>
  )
}

export default App
