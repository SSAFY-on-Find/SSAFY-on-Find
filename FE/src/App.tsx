import { Route, Routes } from "react-router-dom"

import { Header, SideBar } from "@/layout"
import Dashboard from "@/pages/DashboardPage"
import MyProfile from "@/pages/MyProfilePage"
import MyTeam from "@/pages/MyTeamPage"
import StudentList from "@/pages/StudentListPage"
import TeamList from "@/pages/TeamListPage"

import "@/index.css"

function App() {
	return (
		<div className="App">
			<Header />
			<div className="mt-[64px]">
				<SideBar />
				<main className="ml-[200px] border border-main">
					<Routes>
						<Route path="/" element={<Dashboard />} />
						<Route path="/myteam" element={<MyTeam />} />
						<Route path="/teamlist" element={<TeamList />} />
						<Route path="/studentlist" element={<StudentList />} />
						<Route path="/myprofile" element={<MyProfile />} />
					</Routes>
				</main>
			</div>
		</div>
	)
}

export default App
