import React, { useEffect, useRef, useState } from "react"
import { Send } from "lucide-react"

import { Button, InputBox } from "@/components/atoms"
import { MessageBox } from "@/components/molecules"
import { useChat } from "@/hooks/useChat"
import { useChatStore } from "@/stores/chatStore"

// 팀 멤버의 타입 정의
interface TeamMember {
  studentId: number
  name: string
  profileImageUrl?: string
  // 필요하다면 프로필 이미지 등 다른 속성 추가 가능
}

interface TeamChatProps {
  roomId: number
  studentId: number
  members: TeamMember[] // 팀원 목록을 props로 받도록 추가
}

const TeamChat: React.FC<TeamChatProps> = ({ roomId, studentId, members }) => {
  const { connect, disconnect, sendMessage, error } = useChat(roomId)
  const { messages, isConnected, clearMessages } = useChatStore()
  const [newMessage, setNewMessage] = useState("")
  const chatWindowRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (roomId) connect()
    return () => {
      disconnect()
      clearMessages()
    }
  }, [roomId, connect, disconnect, clearMessages])

  useEffect(() => {
    if (chatWindowRef.current) {
      chatWindowRef.current.scrollTop = chatWindowRef.current.scrollHeight
    }
  }, [messages])

  // 메시지 전송 로직
  const handleSendMessage = () => {
    if (newMessage.trim() && isConnected) {
      sendMessage({
        roomId: roomId,
        studentId: studentId,
        content: newMessage,
      })
      setNewMessage("")
    }
  }

  return (
    <div className="flex h-full flex-col">
      <header className="flex items-center justify-between p-4">
        <h1 className="text-xl font-bold">팀 채팅</h1>
      </header>

      {error && <div className="bg-red-100 p-2 text-center text-red-600">{error}</div>}

      <main ref={chatWindowRef} className="flex-1 overflow-y-auto bg-white p-4">
        <div className="flex flex-col gap-4">
          {messages.map((msg, index) => {
            // members 배열에서 studentId를 기준으로 보낸 사람의 정보를 찾습니다.
            const sender = members.find((member) => member.studentId === msg.studentId)
            // 보낸 사람의 이름. 정보가 없으면 ID를 표시합니다.
            const senderName = sender ? sender.name : `사용자 ${msg.studentId}`
            const senderProfile = sender ? sender.profileImageUrl : ``

            return (
              <MessageBox
                who={msg.studentId === studentId ? "me" : "other"}
                content={msg.content}
                name={senderName} // 찾은 이름을 props로 전달
                profile={senderProfile}
                time={new Date().toLocaleTimeString("ko-KR", {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              />
            )
          })}
        </div>
      </main>

      <footer className="bg-white p-4">
        <div className="flex items-center gap-2">
          <div className="flex-1">
            <InputBox
              text={newMessage}
              size={"s"}
              placeholder={"메시지를 입력하세요..."}
              onChange={setNewMessage}
              isDisabled={!isConnected}
              onKeyDown={(e) => {
                if (e.key === "Enter") handleSendMessage()
              }}
            />
          </div>
          <div className="flex h-10 w-10 items-center">
            <Button size={"l"} isIcon={true} Icon={Send} onClick={handleSendMessage} />
          </div>
        </div>
      </footer>
    </div>
  )
}

export default TeamChat
