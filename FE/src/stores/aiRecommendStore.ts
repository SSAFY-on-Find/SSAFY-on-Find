import { create } from "zustand"

import type { IAiRecommend } from "@/types/dashboard"

interface AiRecommendState {
  recommendations: IAiRecommend[] | null
  setRecommendations: (data: IAiRecommend[]) => void
  clearRecommendations: () => void
}

export const useAiRecommendStore = create<AiRecommendState>((set) => ({
  recommendations: null,
  setRecommendations: (data) => set({ recommendations: data }),
  clearRecommendations: () => set({ recommendations: null }),
}))
