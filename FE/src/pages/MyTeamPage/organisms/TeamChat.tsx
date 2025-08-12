import React, { useEffect, useRef, useState } from "react"
import { Send } from "lucide-react"

import { Button, InputBox } from "@/components/atoms"
import { MessageBox } from "@/components/molecules"
import { useChat } from "@/hooks/useChat"
import { useChatStore } from "@/stores/chatStore"
import type { ChatMessage } from "@/types/chat/chat"
import { formatMessageTime } from "@/utils"

interface TeamMember {
  studentId: number
  name: string
  profileImageUrl?: string
}

interface TeamChatProps {
  roomId: number
  studentId: number
  members: TeamMember[]
  initialMessages?: ChatMessage[]
}

// ✅ 빈 배열을 컴포넌트 외부에 상수로 선언하여 항상 동일한 참조를 갖도록 합니다.
const EMPTY_MESSAGES: ChatMessage[] = []

const TeamChat: React.FC<TeamChatProps> = ({ roomId, studentId, members, initialMessages }) => {
  const { connect, disconnect, sendMessage, error } = useChat({ roomId, studentId, chatType: "team" })

  // ✅ 스토어에서 메시지 배열을 선택합니다.
  // 해당 방의 메시지가 없으면(undefined), 위에서 선언한 EMPTY_MESSAGES를 사용합니다.
  // 이렇게 하면 새로운 배열이 생성되지 않아 무한 루프가 발생하지 않습니다.
  const messages = useChatStore((state) => state.messagesByRoom[String(roomId)] || EMPTY_MESSAGES)

  const { isConnected, clearMessages, setMessages } = useChatStore()
  const [newMessage, setNewMessage] = useState("")
  const chatWindowRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (roomId) {
      connect()
    }
    return () => {
      disconnect()
      clearMessages(String(roomId))
    }
  }, [roomId, connect, disconnect, clearMessages])

  useEffect(() => {
    // initialMessages가 변경될 때만 메시지를 설정하도록 합니다.
    if (initialMessages) {
      setMessages(String(roomId), initialMessages)
    }
  }, [initialMessages, setMessages, roomId])

  useEffect(() => {
    if (chatWindowRef.current) {
      chatWindowRef.current.scrollTop = chatWindowRef.current.scrollHeight
    }
  }, [messages])

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
          {messages.map((msg) => {
            const sender = members.find((member) => member.studentId === msg.studentId)
            const senderName = sender ? sender.name : `사용자 ${msg.studentId}`
            const senderProfile = sender ? sender.profileImageUrl : ``

            return (
              <MessageBox
                key={`${msg.studentId}-${msg.publishedAt}`} // 고유한 key를 위해 publishedAt 사용
                who={msg.studentId === studentId ? "me" : "other"}
                content={msg.content}
                name={senderName}
                profile={senderProfile}
                time={formatMessageTime(msg.publishedAt)}
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
                if (e.key === "Enter" && !e.nativeEvent.isComposing) {
                  e.preventDefault()
                  handleSendMessage()
                }
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
