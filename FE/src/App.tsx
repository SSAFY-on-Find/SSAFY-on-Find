import "@/index.css";
import Dashboard from "@/pages/DashboardPage";
import MyTeam from "@/pages/MyTeamPage";
import { Routes, Route } from "react-router-dom";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/myteam" element={<MyTeam />} />
    </Routes>
  );
}

export default App;
