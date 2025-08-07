import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { Send } from "lucide-react"

import { Button, InputBox } from "@/components/atoms"
import { MessageBox } from "@/components/molecules"
import { useMyTeam } from "@/hooks/useTeam"
import { useUserStore } from "@/stores/userStore"

export default function MyTeamPage() {
  const navigate = useNavigate()

  const user = useUserStore()
  const { data: myTeamData, isLoading: myTeamDataLoading } = useMyTeam()

  useEffect(() => {
    if (user.user && user.user.teamId === null) {
      navigate("/create-team")
    }
  }, [user, navigate])
  if (myTeamDataLoading) return
  return (
    <div className="bg-background min-h-screen p-8">
      <div className="flex justify-between border-1 p-1">
        <div className="w-[650px] border-1 p-1">
          <div className="flex border-1 p-1">내팀정보</div>
          <div className="flex border-1 p-1">대기목록</div>
        </div>
        <div className="border-line flex w-[430px] flex-col rounded-lg border-1 bg-white">
          <div className="flex p-6">
            <p className="text-text text-2xl font-semibold">팀 채팅</p>
          </div>
          <div className="flex h-[500px] flex-col gap-4 overflow-y-auto px-6">
            <MessageBox time="오후 07:01" who="me" content="내채팅내용~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" />
            <MessageBox
              time="오후 07:02"
              name="김싸피"
              who="other"
              content="상대채팅내용~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
            />
            <MessageBox
              time="오후 07:02"
              name="박싸피"
              who="other"
              content="상대채팅내용~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
            />
            <MessageBox
              time="오후 07:01"
              who="me"
              content="내채팅내용 내채팅내용내채팅내용 내채팅내용내채팅내용 내채팅내용내채팅내용 내채팅내용내채팅내용 내채팅내용"
            />
            <MessageBox time="오후 07:01" who="system" content="이싸피님이 팀원으로 합류하였습니다." />
            <MessageBox
              time="오후 07:02"
              name="이싸피"
              who="other"
              content="상대채팅내용~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
            />
            <MessageBox time="오후 07:01" who="me" content="내채팅내용~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" />
          </div>
          <div className="flex items-center justify-center gap-2 p-3">
            <InputBox text={""} size={"s"} placeholder={"메시지를 입력하세요..."} onChange={() => ""} />
            <div className="flex h-10 w-10 items-center justify-center">
              <Button
                isIcon={true}
                Icon={Send}
                onClick={function (): void {
                  throw new Error("Function not implemented.")
                }}
                size={"l"}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
