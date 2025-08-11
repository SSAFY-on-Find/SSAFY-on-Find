import { useCallback, useEffect, useRef, useState } from "react"
import { Client, type IMessage, type StompSubscription } from "@stomp/stompjs"

import { useChatStore } from "@/stores/chatStore"
import type { ChatMessage } from "@/types/chat/chat"
import type { IChatOptions } from "@/types/chat/IChatOption"

// .env 파일에서 웹소켓 URL을 가져오고, 없을 경우 기본값을 사용합니다.
const WEBSOCKET_URL = import.meta.env.VITE_APP_BASE_URL + "/ws-stomp" || "ws://localhost:8080/api/v1/ws-stomp"

export const useChat = ({ roomId, studentId, chatType }: IChatOptions) => {
  const stompClientRef = useRef<Client | null>(null)
  const subscriptionRef = useRef<StompSubscription | null>(null)

  const { addMessage, setConnected, clearMessages } = useChatStore()
  const [error, setError] = useState<string | null>(null)
  const subscribePath = chatType === "team" ? `/topic/chatroom/${roomId}` : `/queue/chatroom/${roomId}`
  const publishPath = chatType === "team" ? "/pub/team/message" : "/pub/direct/message"

  // roomId가 변경되거나 훅이 처음 마운트될 때 클라이언트를 설정하고 정리합니다.
  useEffect(() => {
    if (!roomId) return

    const client = new Client({
      brokerURL: WEBSOCKET_URL,
      connectHeaders: {
        studentId: String(studentId),
        roomId: String(roomId),
      },

      // 연결 성공 시 콜백
      onConnect: () => {
        setConnected(true)
        setError(null)

        if (subscriptionRef.current) {
          subscriptionRef.current.unsubscribe()
        }

        subscriptionRef.current = client.subscribe(subscribePath, (message: IMessage) => {
          const receivedMessage = JSON.parse(message.body) as ChatMessage
          addMessage(receivedMessage)
        })
      },
      onDisconnect: () => {
        setConnected(false)
      },
      // 에러 처리 콜백
      onStompError: (frame) => {
        setError(`Broker reported error: ${frame.headers["message"]}. Description: ${frame.body}`)
        setConnected(false)
      },
      onWebSocketError: () => {
        setError("WebSocket 연결에 실패했습니다.")
        setConnected(false)
      },
      reconnectDelay: 5000, // 5초마다 재연결 시도
    })

    // 생성된 클라이언트를 ref에 저장합니다.
    stompClientRef.current = client

    // 컴포넌트가 언마운트되거나 roomId가 변경될 때 실행될 정리(cleanup) 함수입니다.
    // 연결을 비활성화하고 리소스를 정리합니다.
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
    clearMessages()
    stompClientRef.current.activate()
  }, [clearMessages])

  const disconnect = useCallback(() => {
    if (stompClientRef.current?.active) {
      stompClientRef.current.deactivate()
    }
  }, [])

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
