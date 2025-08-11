// src/types/chat.ts

/**
 * @description 채팅 메시지 타입
 * @property {number} roomId - 채팅방 ID
 * @property {number} studentId - 메시지를 보낸 학생 ID
 * @property {string} content - 메시지 내용
 * @property {string} [publishedAt] - 메시지 발행 시간 (서버에서 생성)
 */
export interface ChatMessage {
  roomId: number
  studentId: number
  content: string
  publishedAt?: string
}

/**
 * @description 채팅방 정보를 나타내는 타입
 */
export interface ChatRoom {
  id: number
  name: string // 예시 필드, 실제 백엔드 모델에 맞게 수정 필요
  members: number[] // 예시 필드
}

/**
 * @description 1:1 채팅방 생성 요청 시 사용되는 DTO
 */
export interface DirectChatRoomRequest {
  targetStudentId: number
}
