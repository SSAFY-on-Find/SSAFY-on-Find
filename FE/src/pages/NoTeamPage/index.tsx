// NoTeamPage.jsx
import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { Plus } from "lucide-react"

import { Button } from "@/components/atoms"
import { useAuth } from "@/hooks/useStudent"

export default function NoTeamPage() {
  const navigate = useNavigate()
  const { data: authData } = useAuth()
  const teamId = authData?.teamId
  useEffect(() => {
    if (teamId) {
      navigate("/myteam")
    }
  }, [teamId, navigate])
  return (
    <div className="bg-background flex min-h-[calc(100vh-64px)] items-center justify-center">
      <div className="max-w-md rounded-xl p-8 text-center">
        <h2 className="text-text mb-2 text-2xl font-bold">소속된 팀이 없습니다</h2>
        <p className="text-subtext mb-6">새로운 팀을 만들거나 기존 팀에 합류해보세요!</p>
        <div className="flex justify-center gap-3">
          <Button onClick={() => navigate("/create-team")} Icon={Plus} text="팀 만들기" size={"m"} isIcon={false} />
          <Button onClick={() => navigate("/teamlist")} variant="outline" text="팀 찾기" size={"m"} isIcon={false} />
        </div>
      </div>
    </div>
  )
}
