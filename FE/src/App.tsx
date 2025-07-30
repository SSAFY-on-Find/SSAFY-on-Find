import { Route, Routes } from "react-router-dom"

import { Header } from "@/layout"
import Dashboard from "@/pages/DashboardPage"
import MyTeam from "@/pages/MyTeamPage"

import "@/index.css"

function App() {
	return (
		<div className="App">
			<Header />
			<main className="mt-[64px]">
				<Routes>
					<Route path="/" element={<Dashboard />} />
					<Route path="/myteam" element={<MyTeam />} />
				</Routes>
			</main>
		</div>
	)
}

export default App
