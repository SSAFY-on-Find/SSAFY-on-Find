import "@/index.css";
import { Routes, Route } from "react-router-dom"
import Dashboard from "@/pages/DashboardPage"
import MyTeam from "@/pages/MyTeamPage"
import { Header } from "@/layout"

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
  );
}

export default App;
