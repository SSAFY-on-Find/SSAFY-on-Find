import { Route, Routes, useLocation } from "react-router-dom"
import { ToastContainer } from "react-toastify"

import { Header, SideBar } from "@/layout"
import Dashboard from "@/pages/DashboardPage"
import Login from "@/pages/LoginPage"
import MyProfile from "@/pages/MyProfilePage"
import MyTeam from "@/pages/MyTeamPage"
import CreateProfile from "@/pages/ProfileCreatePage"
import StudentList from "@/pages/StudentListPage"
import TeamCreatePage from "@/pages/TeamCreatePage"
import TeamList from "@/pages/TeamListPage"
import { RoutePolicy } from "@/router"

import ComponentTestPage from "./components/ComponentTestPage"

import "@/index.css"

function App() {
  const location = useLocation()
  const hideLayout = location.pathname === "/login"

  return (
    <RoutePolicy>
      <div className="App">
        {!hideLayout && <Header />}
        <div className={!hideLayout ? "mt-[64px]" : ""}>
          {!hideLayout && <SideBar />}
          <main className={!hideLayout ? "border-main ml-[230px] border" : ""}>
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/login" element={<Login />} />
              <Route path="/myteam" element={<MyTeam />} />
              <Route path="/teamlist" element={<TeamList />} />
              <Route path="/create-team" element={<TeamCreatePage />} />
              <Route path="/studentlist" element={<StudentList />} />
              <Route path="/myprofile" element={<MyProfile />} />
              <Route path="/create-profile" element={<CreateProfile />} />
              <Route path="/component-test" element={<ComponentTestPage />} />
            </Routes>
          </main>
        </div>
        <ToastContainer position="bottom-right" autoClose={3000} theme="light" />
      </div>
    </RoutePolicy>
  )
}

export default App
