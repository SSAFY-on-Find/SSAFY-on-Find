import { NavLink } from "react-router-dom"
import { ChartColumnIncreasing, Notebook, Settings, User, Users } from "lucide-react"

interface INavProps {
  navIcon: React.ElementType
  navTitle: string
  to: string
}

function NavBtn({ navIcon: Icon, navTitle, to }: INavProps) {
  return (
    <NavLink
      to={to}
      className={({ isActive }) =>
        `flex items-center gap-2 rounded-md px-4 py-2 font-medium transition-colors ${
          isActive ? "bg-main text-background" : "text-subtext hover:bg-main/10"
        }`
      }
    >
      <Icon className="h-5 w-5" />
      <div className="text-base">{navTitle}</div>
    </NavLink>
  )
}

function Nav() {
  return (
    <nav className="flex flex-col gap-2 px-2 py-4">
      <NavBtn navIcon={ChartColumnIncreasing} navTitle="통계 대시보드" to="/" />
      <NavBtn navIcon={Notebook} navTitle="내 팀 관리" to="/myteam" />
      <NavBtn navIcon={Users} navTitle="팀 목록" to="/teamlist" />
      <NavBtn navIcon={User} navTitle="교육생 목록" to="/studentlist" />
      <NavBtn navIcon={Settings} navTitle="마이 페이지" to="/myprofile" />
    </nav>
  )
}

export default Nav
