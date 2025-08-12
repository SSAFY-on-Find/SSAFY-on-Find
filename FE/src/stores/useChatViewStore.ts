import { create } from "zustand"

type RoomType = "team" | "direct" | null

interface ChatViewState {
  activeRoomId: number | null
  activeRoomType: RoomType
  openChat: (payload: { roomId: number; roomType: NonNullable<RoomType> }) => void
  closeChat: () => void
}

export const useChatViewStore = create<ChatViewState>((set) => ({
  // 초기 상태
  activeRoomId: null,
  activeRoomType: null,

  // 채팅창을 여는 액션
  openChat: (payload) =>
    set({
      activeRoomId: payload.roomId,
      activeRoomType: payload.roomType,
    }),

  // 채팅창을 닫는 액션
  closeChat: () =>
    set({
      activeRoomId: null,
      activeRoomType: null,
    }),
}))
