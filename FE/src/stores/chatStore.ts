import { create } from "zustand"

import type { ChatMessage } from "@/types/chat/chat"

interface ChatState {
  messagesByRoom: Record<string, ChatMessage[]>
  isConnected: boolean

  addMessage: (roomId: string, message: ChatMessage) => void
  setConnected: (status: boolean) => void
  clearMessages: (roomId: string) => void
  setMessages: (roomId: string, messages: ChatMessage[]) => void
}

export const useChatStore = create<ChatState>((set) => ({
  messagesByRoom: {},
  isConnected: false,

  addMessage: (roomId, message) =>
    set((state) => ({
      messagesByRoom: {
        ...state.messagesByRoom,
        [roomId]: [...(state.messagesByRoom[roomId] || []), message],
      },
    })),

  setConnected: (status) => set({ isConnected: status }),

  clearMessages: (roomId) =>
    set((state) => {
      const newMessagesByRoom = { ...state.messagesByRoom }
      delete newMessagesByRoom[roomId]
      return { messagesByRoom: newMessagesByRoom }
    }),

  setMessages: (roomId, messages) =>
    set((state) => ({
      messagesByRoom: {
        ...state.messagesByRoom,
        [roomId]: messages,
      },
    })),
}))
