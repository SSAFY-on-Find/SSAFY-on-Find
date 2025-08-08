// src/store/chatStore.ts
import { create } from "zustand"

import type { ChatMessage } from "@/types/chat/chat"

interface ChatState {
  messages: ChatMessage[]
  isConnected: boolean
  addMessage: (message: ChatMessage) => void
  setConnected: (status: boolean) => void
  clearMessages: () => void
}

export const useChatStore = create<ChatState>((set) => ({
  messages: [],
  isConnected: false,
  addMessage: (message) => set((state) => ({ messages: [...state.messages, message] })),
  setConnected: (status) => set({ isConnected: status }),
  clearMessages: () => set({ messages: [] }),
}))
