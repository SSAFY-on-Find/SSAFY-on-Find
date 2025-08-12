// src/types/chat.ts

import type { ISubcode } from "../common"

/**
 * @description 채팅 메시지 타입
 * @property {number} roomId - 채팅방 ID
 * @property {number} studentId - 메시지를 보낸 학생 ID
 * @property {string} content - 메시지 내용
 * @property {string} [publishedAt] - 메시지 발행 시간 (서버에서 생성)
 */
export interface ChatMessage {
  studentId: number
  roomId: number
  content: string
  publishedAt: string
}

/**
 * @description 채팅방 정보를 나타내는 타입
 */
export interface ChatRoom {
  id: number
  name: string
  members: number[] // 예시 필드
}

/**
 * @description 1:1 채팅방 생성 요청 시 사용되는 DTO
 */
export interface DirectChatRoomRequest {
  targetStudentId: number
}

export interface IChatRoomInfo {
  chatRoomId: number
  hasTeam: boolean
  isRead: boolean
  lastChatAt: string
  targetUserId: number
  targetUsername: string
  targetProfileImageUrl: string
  major: string
  position: ISubcode
}
