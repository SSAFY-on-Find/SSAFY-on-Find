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

const TeamChat: React.FC<TeamChatProps> = ({ roomId, studentId, members, initialMessages }) => {
  const { connect, disconnect, sendMessage, error } = useChat(roomId)
  const { messages, isConnected, clearMessages, setMessages } = useChatStore()
  const [newMessage, setNewMessage] = useState("")
  const chatWindowRef = useRef<HTMLDivElement>(null)

  //  1: 웹소켓 연결/해제 관리. roomId가 바뀔 때만 실행됩니다.
  useEffect(() => {
    if (roomId) {
      connect()
    }
    return () => {
      disconnect()
      clearMessages()
    }
  }, [roomId, connect, disconnect, clearMessages])

  //  2: 이전 메시지 초기화. initialMessages가 처음 들어왔을 때 실행됩니다.
  useEffect(() => {
    if (initialMessages) {
      setMessages(initialMessages)
    }
  }, [initialMessages, setMessages])

  //  3: 메시지 목록이 변경되면 스크롤을 맨 아래로 이동
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
