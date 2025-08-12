import { useCallback, useEffect, useRef, useState } from "react"
import { Client, type IMessage, type StompSubscription } from "@stomp/stompjs"

import { updateLastReadAt } from "@/apis/chatRoom"
import { useChatStore } from "@/stores/chatStore"
import type { ChatMessage } from "@/types/chat/chat"
import type { IChatOptions } from "@/types/chat/IChatOption"

const WEBSOCKET_URL = import.meta.env.VITE_APP_BASE_URL + "/ws-stomp" || "ws://localhost:8080/api/v1/ws-stomp"

export const useChat = ({ roomId, studentId, chatType }: IChatOptions) => {
  const stompClientRef = useRef<Client | null>(null)
  const subscriptionRef = useRef<StompSubscription | null>(null)

  // ❗️ 수정: clearMessages는 이제 컴포넌트에서 직접 호출하므로 여기서 제거합니다.
  const { addMessage, setConnected, clearMessages } = useChatStore()
  const [error, setError] = useState<string | null>(null)
  const subscribePath = chatType === "team" ? `/topic/chatroom/${roomId}` : `/queue/chatroom/${roomId}`
  const publishPath = chatType === "team" ? "/pub/team/message" : "/pub/direct/message"

  useEffect(() => {
    if (!roomId) return

    const client = new Client({
      brokerURL: WEBSOCKET_URL,
      connectHeaders: {
        studentId: String(studentId),
        roomId: String(roomId),
      },
      onConnect: () => {
        setConnected(true)
        setError(null)

        if (subscriptionRef.current) {
          subscriptionRef.current.unsubscribe()
        }

        subscriptionRef.current = client.subscribe(subscribePath, (message: IMessage) => {
          const receivedMessage = JSON.parse(message.body) as ChatMessage
          // ❗️ 수정: addMessage 호출 시 roomId를 전달하여 올바른 방에 메시지를 추가합니다.
          addMessage(String(roomId), receivedMessage)
        })
      },
      onDisconnect: () => {
        setConnected(false)
      },
      onStompError: (frame) => {
        setError(`Broker reported error: ${frame.headers["message"]}. Description: ${frame.body}`)
        setConnected(false)
      },
      onWebSocketError: () => {
        setError("WebSocket 연결에 실패했습니다.")
        setConnected(false)
      },
      reconnectDelay: 5000,
    })

    stompClientRef.current = client

    return () => {
      subscriptionRef.current?.unsubscribe()
      client.deactivate()
      stompClientRef.current = null
      subscriptionRef.current = null
    }
  }, [roomId, studentId, addMessage, setConnected, subscribePath])

  const connect = useCallback(() => {
    if (!stompClientRef.current || stompClientRef.current.active) {
      return
    }
    stompClientRef.current.activate()
  }, [])

  const disconnect = useCallback(() => {
    if (stompClientRef.current?.active && roomId) {
      updateLastReadAt(roomId)
      stompClientRef.current.deactivate()
    }
  }, [roomId, clearMessages])

  const sendMessage = useCallback(
    (message: Omit<ChatMessage, "publishedAt">) => {
      if (stompClientRef.current?.active && message.content) {
        stompClientRef.current.publish({
          destination: publishPath,
          body: JSON.stringify(message),
        })
      } else {
        setError("연결되지 않았거나 메시지가 비어있습니다.")
      }
    },
    [publishPath]
  )

  return { connect, disconnect, sendMessage, error }
}
