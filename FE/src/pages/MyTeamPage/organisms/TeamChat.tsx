import React, { useEffect, useRef, useState } from "react"

import { useChat } from "@/hooks/useChat"
import { useChatStore } from "@/stores/chatStore"
import type { ChatMessage } from "@/types/chat/chat"

interface TeamChatProps {
  roomId: number
  studentId: number
}

const TeamChat: React.FC<TeamChatProps> = ({ roomId, studentId }) => {
  // 1. Props로 받은 roomId로 useChat 훅을 초기화합니다.
  const { connect, disconnect, sendMessage, error } = useChat(roomId)
  const { messages, isConnected, clearMessages } = useChatStore()
  const [newMessage, setNewMessage] = useState("")
  const chatWindowRef = useRef<HTMLDivElement>(null)

  // 2. roomId가 유효하면 자동으로 웹소켓에 연결하고, 컴포넌트 언마운트 시 연결을 해제합니다.
  useEffect(() => {
    if (roomId) {
      connect()
    }
    return () => {
      disconnect()
      clearMessages() // 컴포넌트가 사라질 때 메시지 목록을 비웁니다.
    }
  }, [roomId, connect, disconnect, clearMessages])

  // 새로운 메시지가 추가될 때마다 스크롤을 맨 아래로 이동
  useEffect(() => {
    if (chatWindowRef.current) {
      chatWindowRef.current.scrollTop = chatWindowRef.current.scrollHeight
    }
  }, [messages])

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault()
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
    // 3. 기존 ChatRoomPage.tsx의 UI 구조를 거의 그대로 사용합니다.
    //    전체 페이지 레이아웃(h-screen 등) 대신 flex-col과 높이 100%로 채우도록 수정합니다.
    <div className="flex h-full flex-col">
      <header className="flex items-center justify-between border-b p-4">
        <h1 className="text-xl font-bold">팀 채팅</h1>
        <span className={`text-sm font-medium ${isConnected ? "text-green-600" : "text-red-600"}`}>
          {isConnected ? "● 연결됨" : "● 연결 끊김"}
        </span>
      </header>

      {error && <div className="bg-red-100 p-2 text-center text-red-600">{error}</div>}

      <main ref={chatWindowRef} className="flex-1 overflow-y-auto bg-gray-50 p-4">
        <div className="flex flex-col gap-4">
          {messages.map((msg: ChatMessage, index) => (
            <div key={index} className={`flex ${msg.studentId === studentId ? "justify-end" : "justify-start"}`}>
              <div
                className={`max-w-xs rounded-lg p-3 lg:max-w-md ${
                  msg.studentId === studentId ? "bg-blue-500 text-white" : "bg-white text-black"
                }`}
              >
                {msg.studentId !== studentId && (
                  <p className="pb-1 text-xs font-semibold opacity-70">사용자 {msg.studentId}</p>
                )}
                <p>{msg.content}</p>
              </div>
            </div>
          ))}
        </div>
      </main>

      <footer className="border-t bg-white p-4">
        <form onSubmit={handleSendMessage} className="flex gap-2">
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            placeholder="메시지를 입력하세요..."
            className="flex-1 rounded-md border px-3 py-2 focus:outline-none"
            disabled={!isConnected}
          />
          <button
            type="submit"
            className="rounded-md bg-blue-500 px-4 py-2 font-semibold text-white hover:bg-blue-600 disabled:bg-gray-400"
            disabled={!isConnected || !newMessage.trim()}
          >
            전송
          </button>
        </form>
      </footer>
    </div>
  )
}

export default TeamChat
