// NoTeamPage.jsx
import { useNavigate } from "react-router-dom"
import { Plus, Users } from "lucide-react"

import { Button } from "@/components/atoms"

export default function NoTeamPage() {
  const navigate = useNavigate()

  return (
    <div className="bg-background flex min-h-screen items-center justify-center">
      <div className="border-line max-w-md rounded-xl border bg-white p-8 text-center">
        <Users className="mx-auto mb-4 h-16 w-16 text-gray-400" />
        <h2 className="text-text mb-2 text-2xl font-bold">소속된 팀이 없습니다</h2>
        <p className="text-subtext mb-6">새로운 팀을 만들거나 기존 팀에 합류해보세요!</p>
        <div className="flex justify-center gap-3">
          <Button onClick={() => navigate("/create-team")} Icon={Plus} text="팀 만들기" size={"s"} isIcon={false} />
          <Button onClick={() => navigate("/teamlist")} variant="outline" text="팀 찾기" size={"s"} isIcon={false} />
        </div>
      </div>
    </div>
  )
}
