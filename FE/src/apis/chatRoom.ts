// src/api/chatRoom.ts
import type { ChatMessage, DirectChatRoomRequest } from "@/types/chat/chat"

import api from "./index"

export const getTeamChatRoomId = async (teamId: number): Promise<number> => {
  const response = await api.get<{ data: { roomId: number } }>(`/chat-rooms/teams/${teamId}`)
  return response.data.data.roomId
}

export const createTeamChatRoom = async (teamId: number): Promise<number> => {
  // ChatRoomController.java의 createTeamChatRoom 메서드에 따라 POST 요청
  const response = await api.post<{ data: { roomId: number } }>(`/chat-rooms/teams/${teamId}`)
  return response.data.data.roomId
}

export const createDirectChatRoom = async (data: DirectChatRoomRequest): Promise<number> => {
  const response = await api.post<{ data: { roomId: number } }>(`/chat-rooms/individual`, data)
  return response.data.data.roomId
}

export const getMyDirectChatRooms = async () => {
  const response = await api.get("/chat-rooms/individual/me")
  return response.data.data
}
export const leaveChatRoom = async (roomId: number): Promise<void> => {
  await api.post(`/chat-rooms/rooms/${roomId}/leave`)
}

export const getChatMessages = async (roomId: number): Promise<ChatMessage[]> => {
  const response = await api.get(`/chat-rooms/${roomId}/messages`)
  return response.data.data
}

export const updateLastReadAt = async (roomId: number): Promise<void> => {
  console.log("lastread 호출 : ", roomId)

  await api.post(`/chat-rooms/${roomId}/last-read`)
}
