import { Route, Routes } from "react-router-dom"

import Dashboard from "@/pages/DashboardPage"
import MyTeam from "@/pages/MyTeamPage"

import "@/index.css"

function App() {
	return (
		<Routes>
			<Route path="/" element={<Dashboard />} />
			<Route path="/myteam" element={<MyTeam />} />
		</Routes>
	)
}

export default App
