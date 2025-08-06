import { useState } from "react"
import { ChevronDown, ChevronUp, Info, Plus } from "lucide-react"

import { Tooltip } from "@/components/atoms"
import { ChatList, Nav } from "@/components/molecules"
import { StudentSearchModal } from "@/components/templates"

// 교육생 검색 모달 테스트용 MockData 입니다
const students = [
  { id: "std001", name: "김싸피", major: "비전공", position: "임베디드", hasTeam: true },
  { id: "std002", name: "이싸", major: "전공", position: "풀스텍", hasTeam: false },
  { id: "std003", name: "박싸피", major: "비전공", position: "백엔드", hasTeam: true },
  { id: "std004", name: "조싸피", major: "전공", position: "모바일", hasTeam: true },
  { id: "std005", name: "이싸피", major: "비전공", position: "임베디드", hasTeam: false },
  { id: "std006", name: "김박싸피", major: "비전공", position: "임베디드", hasTeam: true },
  { id: "std007", name: "이싸", major: "전공", position: "풀스텍", hasTeam: false },
  { id: "std008", name: "박싸피", major: "비전공", position: "백엔드", hasTeam: true },
  { id: "std009", name: "조싸피", major: "전공", position: "모바일", hasTeam: true },
  { id: "std010", name: "이싸피", major: "비전공", position: "임베디드", hasTeam: false },
  { id: "std011", name: "김싸피", major: "비전공", position: "임베디드", hasTeam: true },
  { id: "std012", name: "이싸", major: "전공", position: "풀스텍", hasTeam: false },
  { id: "std013", name: "박싸피", major: "비전공", position: "백엔드", hasTeam: true },
  { id: "std014", name: "조싸피", major: "전공", position: "모바일", hasTeam: true },
  { id: "std015", name: "이싸피", major: "비전공", position: "임베디드", hasTeam: false },
]

function SideBar() {
  const [open, setOpen] = useState(true)
  const [studentSearchModal, setStudentSearchModal] = useState(false)

  const handleChatWithUser = (userId: string) => {
    console.log(userId, "과 채팅하기") // 1대1 채팅 로직 넣기
  }

  return (
    <>
      <aside className="bg-background fixed top-[64px] left-0 flex h-[calc(100vh-64px)] w-[230px] flex-col">
        <Nav />
        <div className="flex min-h-0 flex-1 flex-col">
          <div className="mt-5 flex flex-row justify-between">
            <div className="flex flex-row">
              <button
                className="text-subtext flex cursor-pointer flex-row items-center justify-start gap-2 px-2 outline-none select-none"
                onClick={() => setOpen((prev) => !prev)}
                type="button"
              >
                {open ? <ChevronDown /> : <ChevronUp />}
                <div className="text-sm font-medium">채팅 목록</div>
              </button>
              <Tooltip
                content={
                  <>
                    <span className="text-main">● </span>팀 가입
                    <br />
                    <span className="text-main">○ </span>미가입
                  </>
                }
                side="right"
              >
                <Info className="text-subtext w-4" />
              </Tooltip>
            </div>
            <Plus
              className="hover:bg-main/10 text-subtext mr-2 w-5 cursor-pointer rounded-full"
              onClick={() => setStudentSearchModal(true)}
            />
          </div>
          <div className="min-h-0 flex-1 overflow-x-hidden overflow-y-auto transition-all">{open && <ChatList />}</div>
        </div>
      </aside>
      <StudentSearchModal
        isOpen={studentSearchModal}
        onClose={() => setStudentSearchModal(false)}
        students={students}
        onStudentClick={(userId) => {
          handleChatWithUser(userId)
          setStudentSearchModal(false)
        }}
      />
    </>
  )
}

export default SideBar
