// src/api/chatRoom.ts
import type { ChatMessage, DirectChatRoomRequest } from "@/types/chat/chat"

import api from "./index"

/**
 * @description 팀 채팅방 ID를 조회합니다.
 * @param teamId 팀 ID
 */
export const getTeamChatRoomId = async (teamId: number): Promise<number> => {
  const response = await api.get<{ data: { roomId: number } }>(`/chat-rooms/teams/${teamId}`)
  return response.data.data.roomId
}

/**
 * @description 팀 채팅방을 생성합니다.
 * @param teamId 팀 ID
 * @returns 생성된 채팅방 ID
 */
export const createTeamChatRoom = async (teamId: number): Promise<number> => {
  // ChatRoomController.java의 createTeamChatRoom 메서드에 따라 POST 요청
  const response = await api.post<{ data: { roomId: number } }>(`/chat-rooms/teams/${teamId}`)
  return response.data.data.roomId
}

/**
 * @description 1:1 채팅방을 생성하거나 기존 채팅방 ID를 조회합니다.
 * @param {DirectChatRoomRequest} data - 상대방 학생 ID가 포함된 요청 데이터
 */
export const createDirectChatRoom = async (data: DirectChatRoomRequest): Promise<number> => {
  const response = await api.post<{ data: { roomId: number } }>(`/chat-rooms/individual`, data)
  return response.data.data.roomId
}

/**
 * @description 현재 로그인된 사용자의 모든 1:1 채팅방 목록을 가져옵니다.
 */
export const getMyDirectChatRooms = async () => {
  const response = await api.get("/chat-rooms/individual/me")
  return response.data.data
}
/**
 * @description 채팅방을 나갑니다.
 * @param roomId 채팅방 ID
 */
export const leaveChatRoom = async (roomId: number): Promise<void> => {
  await api.post(`/chat-rooms/rooms/${roomId}/leave`)
}

/**
 * @description 채팅방의 이전 메시지 목록을 불러옵니다.
 * @param roomId 채팅방 ID
 * @returns 메시지 목록
 */
export const getChatMessages = async (roomId: number): Promise<ChatMessage[]> => {
  const response = await api.get(`/chat-rooms/${roomId}/messages`)
  return response.data.data
}
