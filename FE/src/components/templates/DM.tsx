import React, { useEffect, useRef, useState } from "react"
import { Send } from "lucide-react"

import { Button, InputBox } from "@/components/atoms"
import { MessageBox } from "@/components/molecules"
import { useChat } from "@/hooks/useChat"
import { useChatStore } from "@/stores/chatStore"
import type { ChatMessage } from "@/types/chat/chat"
import { formatMessageTime } from "@/utils"

interface ChatMember {
  studentId: number
  name: string
  profileImageUrl?: string
}

interface ChatMemberProps {
  roomId: number
  studentId: number
  members: ChatMember[]
  initialMessages?: ChatMessage[]
}

const DM: React.FC<ChatMemberProps> = ({ roomId, studentId, members, initialMessages }) => {
  const { connect, disconnect, sendMessage, error } = useChat({ roomId, studentId, chatType: "direct" })

  // ❗️ 수정: 스토어에서 현재 roomId에 해당하는 메시지만 가져옵니다.
  const messages = useChatStore((state) => state.messagesByRoom[String(roomId)] || [])
  const { setMessages } = useChatStore()

  const [newMessage, setNewMessage] = useState("")
  const chatWindowRef = useRef<HTMLDivElement>(null)

  // 1. 웹소켓 연결/해제 관리
  useEffect(() => {
    if (roomId) {
      connect()
    }
    return () => {
      disconnect()
    }
  }, [roomId, connect, disconnect])

  // 2. 이전 메시지 초기화
  useEffect(() => {
    if (initialMessages) {
      // ❗️ 수정: 현재 방에 대한 메시지를 설정합니다.
      setMessages(String(roomId), initialMessages)
    }
  }, [initialMessages, setMessages, roomId])

  // 3. 새 메시지 수신 시 스크롤 이동
  useEffect(() => {
    if (chatWindowRef.current) {
      chatWindowRef.current.scrollTop = chatWindowRef.current.scrollHeight
    }
  }, [messages])

  const handleSendMessage = () => {
    if (newMessage.trim()) {
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
      {error && <div className="bg-red-100 p-2 text-center text-red-600">{error}</div>}

      <main ref={chatWindowRef} className="min-h-0 flex-1 overflow-y-auto bg-white p-4">
        <div className="flex flex-col gap-4">
          {messages.map((msg) => {
            const sender = members.find((member) => member.studentId === msg.studentId) || members[1]
            if (!sender) return null
            const senderName = sender.name
            const senderProfile = sender.profileImageUrl
            return (
              <MessageBox
                key={`${msg.studentId}-${msg.publishedAt}`}
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

export default DM
