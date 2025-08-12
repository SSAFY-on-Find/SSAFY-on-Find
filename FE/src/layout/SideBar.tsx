import { useState } from "react"
import { ChevronDown, ChevronUp, Info, Plus } from "lucide-react"

import { Tooltip } from "@/components/atoms"
import { ChatList, Nav } from "@/components/molecules"
import { StudentSearchModal } from "@/components/templates"
import { useCreateDirectChatRoom, useMyDirectChatRooms } from "@/hooks/useDM"
import { useStudentList } from "@/hooks/useStudent"
import { useChatViewStore } from "@/stores/useChatViewStore"

function SideBar() {
  const [open, setOpen] = useState(true)
  const [studentSearchModal, setStudentSearchModal] = useState(false)

  const { data: chatRooms, isLoading: isChatListLoading } = useMyDirectChatRooms()
  const { data: students, isLoading: isStudentListLoading } = useStudentList()

  const openChat = useChatViewStore((state) => state.openChat)
  const { mutate: createChat } = useCreateDirectChatRoom()

  const handleStartNewChat = (targetStudentId: number) => {
    createChat(
      { targetStudentId },
      {
        onSuccess: (roomId) => {
          openChat({ roomId, roomType: "direct" })
        },
        onError: (error) => {
          console.log("1대1채팅방 생성 에러 : ", error)
        },
      }
    )
  }

  return (
    <>
      <aside className="bg-background fixed top-[64px] left-0 flex h-[calc(100vh-64px)] w-[260px] flex-col px-2">
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

          <div className="min-h-0 flex-1 overflow-x-hidden overflow-y-auto transition-all">
            {open && (
              <>
                {isChatListLoading && <div className="p-4 text-center text-sm">채팅 목록을 불러오는 중...</div>}
                {chatRooms && (
                  <ChatList rooms={chatRooms} onRoomClick={(roomId) => openChat({ roomId, roomType: "direct" })} />
                )}
              </>
            )}
          </div>
        </div>
      </aside>

      {!isStudentListLoading && students && (
        <StudentSearchModal
          isOpen={studentSearchModal}
          onClose={() => setStudentSearchModal(false)}
          students={students}
          onStudentClick={(studentId) => {
            handleStartNewChat(Number(studentId))
            setStudentSearchModal(false)
          }}
        />
      )}
    </>
  )
}

export default SideBar
