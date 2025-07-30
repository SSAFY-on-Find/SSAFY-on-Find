import MyTeam from "@/pages/MyTeamPage"

import Dashboard from "@/pages/DashboardPage"

import "@/index.css"
import { Route, Routes } from "react-router-dom"

function App() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/myteam" element={<MyTeam />} />
    </Routes>
  )
}

export default App
