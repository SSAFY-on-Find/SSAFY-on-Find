import { useEffect } from "react"
import { useNavigate } from "react-router-dom"

import { useUserStore } from "@/stores/userStore"

export default function MyTeamPage() {
  const user = useUserStore()
  const navigate = useNavigate()
  useEffect(() => {
    if (user.user && user.user.teamId === null) {
      navigate("/create-team")
    }
  }, [user, navigate])
  return (
    <div className="bg-background min-h-screen">
      <p className="text-text p-6">내 팀 페이지</p>
    </div>
  )
}
