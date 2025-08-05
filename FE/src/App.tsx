import { Route, Routes } from "react-router-dom"

import { Header, SideBar } from "@/layout"
import Dashboard from "@/pages/DashboardPage"
import MyProfile from "@/pages/MyProfilePage"
import MyTeam from "@/pages/MyTeamPage"
import StudentList from "@/pages/StudentListPage"
import TeamList from "@/pages/TeamListPage"

import ComponentTestPage from "./components/ComponentTestPage"

import "@/index.css"

function App() {
  return (
    <div className="App">
      <Header />
      <div className="mt-[64px]">
        <SideBar />
        <main className="border-main ml-[230px] border">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/myteam" element={<MyTeam />} />
            <Route path="/teamlist" element={<TeamList />} />
            <Route path="/studentlist" element={<StudentList />} />
            <Route path="/myprofile" element={<MyProfile />} />
            <Route path="/component-test" element={<ComponentTestPage />} />
          </Routes>
        </main>
      </div>
    </div>
  )
}

export default App
