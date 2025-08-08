// src/hooks/useChat.ts
import { useCallback, useEffect, useRef, useState } from "react" // useCallback 임포트
import { Client, type IMessage } from "@stomp/stompjs"

import { useChatStore } from "@/stores/chatStore"
import type { ChatMessage } from "@/types/chat/chat"

const WEBSOCKET_URL = "ws://localhost:8080/api/v1/ws-stomp"

export const useChat = (roomId: number | null) => {
  const stompClient = useRef<Client | null>(null)
  const { addMessage, setConnected, clearMessages } = useChatStore()
  const [error, setError] = useState<string | null>(null)

  // 컴포넌트 언마운트 시 연결 해제를 위한 useEffect
  // 이 useEffect는 useChat 훅을 사용하는 컴포넌트가 사라질 때 최종적으로 연결을 정리합니다.
  useEffect(() => {
    return () => {
      if (stompClient.current?.connected) {
        stompClient.current.deactivate()
      }
    }
  }, []) // 최초 1회만 실행되도록 빈 배열 전달

  /**
   * @description WebSocket 서버에 연결합니다.
   */
  const connect = useCallback(() => {
    if (!roomId) {
      setError("채팅방 ID가 유효하지 않습니다.")
      return
    }

    clearMessages()

    const client = new Client({
      brokerURL: WEBSOCKET_URL,
      onConnect: () => {
        setConnected(true)
        setError(null)

        client.subscribe(`/topic/chatroom/${roomId}`, (message: IMessage) => {
          const receivedMessage = JSON.parse(message.body) as ChatMessage
          addMessage(receivedMessage)
        })
      },
      onStompError: (frame) => {
        setError(`Broker reported error: ${frame.headers["message"]}. Description: ${frame.body}`)
        setConnected(false)
      },
      onWebSocketError: (event) => {
        setError("WebSocket 연결에 실패했습니다.")
        setConnected(false)
      },
      reconnectDelay: 5000,
    })

    client.activate()
    stompClient.current = client
  }, [roomId, clearMessages, setConnected, addMessage]) // 의존성 배열에 함수 내부에서 사용하는 state와 함수들을 명시

  /**
   * @description WebSocket 연결을 종료합니다.
   */
  const disconnect = useCallback(() => {
    if (stompClient.current?.connected) {
      stompClient.current.deactivate()
      // setConnected는 stompjs의 onDisconnect 콜백에서 처리하는 것이 더 안정적일 수 있으나,
      // 현재 구조에서는 명시적으로 호출합니다.
      setConnected(false)
    }
  }, [setConnected]) // 의존성 배열에 setConnected 추가

  /**
   * @description 메시지를 서버로 전송(발행)합니다.
   */
  const sendMessage = useCallback((message: Omit<ChatMessage, "publishedAt">) => {
    if (stompClient.current?.connected && message.content) {
      stompClient.current.publish({
        destination: "/pub/team/message",
        body: JSON.stringify(message),
      })
    } else {
      setError("연결되지 않았거나 메시지가 비어있습니다.")
    }
  }, []) // stompClient.current는 ref이므로 의존성 배열에 추가할 필요 없음

  return { connect, disconnect, sendMessage, error }
}
