import { Route,Routes } from "react-router-dom"

import Dashboard from "@/pages/DashboardPage"
import MyTeam from "@/pages/MyTeamPage"

import "@/index.css"

const test1 = ["자바", "비전공"]
const test2 = [
	["프론트", "POS001"],
	["백엔드", "POS001"],
	["풀스택", "POS001"],
	["임베디드", "POS001"],
	["모바일", "POS001"],
	["AI", "POS001"],
]
function App() {
	return (
		<Routes>
			<Route path="/" element={<Dashboard />} />
			<Route path="/myteam" element={<MyTeam />} />
		</Routes>
	)
}

export default App
