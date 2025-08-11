// src/components/templates/ChatView.tsx

import { useQuery } from "@tanstack/react-query"
import { X } from "lucide-react" // 닫기 아이콘

import { getChatMessages } from "@/apis/chatRoom" // 채팅 기록 불러오는 API
import { useMyDirectChatRooms } from "@/hooks/useDM"
import TeamChat from "@/pages/MyTeamPage/organisms/TeamChat"
import { useChatViewStore } from "@/stores/useChatViewStore"
import { useUserStore } from "@/stores/userStore"

function ChatView() {
  // 1. 전역 스토어에서 상태와 액션 가져오기
  const { activeRoomId, activeRoomType, closeChat } = useChatViewStore()
  const currentUser = useUserStore((state) => state.user)

  // 2. 채팅방 목록과 이전 메시지 데이터 가져오기
  const { data: chatRooms } = useMyDirectChatRooms()
  const { data: initialMessages, isLoading: isMessagesLoading } = useQuery({
    queryKey: ["chatMessages", activeRoomId],
    queryFn: () => getChatMessages(activeRoomId!),
    enabled: !!activeRoomId, // activeRoomId가 있을 때만 쿼리 실행
  })

  // 3. 렌더링에 필요한 데이터가 준비되었는지 확인
  if (activeRoomType !== "direct" || !activeRoomId || !currentUser || !chatRooms) {
    return null
  }

  const currentChatRoom = chatRooms.find((room) => room.chatRoomId === activeRoomId)

  if (!currentChatRoom) {
    closeChat() // 목록에 없는 채팅방이면 닫기
    return null
  }

  // 4. TeamChat에 전달할 members 배열 구성
  const members = [
    {
      studentId: Number(currentUser.studentId),
      name: currentUser.name,
      profileImageUrl: undefined,
      major: currentUser.major,
      position: undefined,
    },
    {
      studentId: currentChatRoom.targetStudentId,
      name: currentChatRoom.targetUsername,
      profileImageUrl: currentChatRoom.targetProfileImageUrl,
      major: currentChatRoom.major,
      position: currentChatRoom.position,
    },
  ]

  return (
    <aside className="fixed top-[64px] right-0 z-10 flex h-[calc(100vh-64px)] w-[430px] flex-col border-l bg-white shadow-lg">
      {/* 채팅창 헤더 */}
      <header className="flex items-center justify-between border-b p-4">
        <h3 className="text-lg font-bold">{currentChatRoom.targetUsername}</h3>
        <button onClick={closeChat} className="rounded-full p-1 hover:bg-gray-100">
          <X className="h-5 w-5 text-gray-600" />
        </button>
      </header>

      {/* 로딩 중일 때 표시할 UI */}
      {isMessagesLoading ? (
        <div className="flex flex-1 items-center justify-center">
          <p>대화 기록을 불러오는 중...</p>
        </div>
      ) : (
        // 로딩 완료 후 TeamChat 컴포넌트 렌더링
        <TeamChat
          roomId={activeRoomId}
          studentId={Number(currentUser.studentId)}
          members={members}
          initialMessages={initialMessages}
        />
      )}
    </aside>
  )
}

export default ChatView
