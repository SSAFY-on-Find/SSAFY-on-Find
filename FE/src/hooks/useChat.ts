import { useCallback, useEffect, useRef, useState } from "react"
import { Client, type IMessage, type StompSubscription } from "@stomp/stompjs"

import { useChatStore } from "@/stores/chatStore"
import type { ChatMessage } from "@/types/chat/chat"

// .env 파일에서 웹소켓 URL을 가져오고, 없을 경우 기본값을 사용합니다.
const WEBSOCKET_URL = import.meta.env.VITE_APP_BASE_URL + "/ws-stomp" || "ws://localhost:8080/api/v1/ws-stomp"

export const useChat = (roomId: number | null) => {
  // Stomp 클라이언트와 구독 객체를 ref로 관리하여 리렌더링 시에도 유지되도록 합니다.
  const stompClientRef = useRef<Client | null>(null)
  const subscriptionRef = useRef<StompSubscription | null>(null)

  const { addMessage, setConnected, clearMessages } = useChatStore()
  const [error, setError] = useState<string | null>(null)

  // roomId가 변경되거나 훅이 처음 마운트될 때 클라이언트를 설정하고 정리합니다.
  useEffect(() => {
    // roomId가 없으면 아무 작업도 수행하지 않습니다.
    if (!roomId) return

    // 새 roomId에 대한 새 클라이언트 인스턴스를 생성합니다.
    const client = new Client({
      brokerURL: WEBSOCKET_URL,
      // 연결 성공 시 콜백
      onConnect: () => {
        setConnected(true)
        setError(null)

        // 기존 구독이 있다면 해지하여 중복 구독을 방지합니다.
        if (subscriptionRef.current) {
          subscriptionRef.current.unsubscribe()
        }

        // 새로운 채팅방을 구독하고, 구독 객체를 ref에 저장합니다.
        subscriptionRef.current = client.subscribe(`/topic/chatroom/${roomId}`, (message: IMessage) => {
          const receivedMessage = JSON.parse(message.body) as ChatMessage
          addMessage(receivedMessage)
        })
      },
      // 연결 해제 시 콜백
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
    // roomId가 변경될 때마다 이 useEffect는 다시 실행되어 새 연결을 설정합니다.
  }, [roomId, addMessage, setConnected])

  /**
   * @description 설정된 WebSocket 서버에 연결을 활성화합니다.
   */
  const connect = useCallback(() => {
    // 클라이언트가 초기화되지 않았거나 이미 활성 상태이면 아무것도 하지 않습니다.
    if (!stompClientRef.current || stompClientRef.current.active) {
      return
    }
    // 연결 시도 전에 기존 메시지를 비웁니다.
    clearMessages()
    stompClientRef.current.activate()
  }, [clearMessages])

  /**
   * @description WebSocket 연결을 종료합니다.
   */
  const disconnect = useCallback(() => {
    if (stompClientRef.current?.active) {
      stompClientRef.current.deactivate()
    }
  }, [])

  /**
   * @description 메시지를 서버로 전송(발행)합니다.
   */
  const sendMessage = useCallback((message: Omit<ChatMessage, "publishedAt">) => {
    if (stompClientRef.current?.active && message.content) {
      stompClientRef.current.publish({
        destination: "/pub/team/message",
        body: JSON.stringify(message),
      })
    } else {
      setError("연결되지 않았거나 메시지가 비어있습니다.")
    }
  }, [])

  return { connect, disconnect, sendMessage, error }
}
