import { create } from "zustand"

import type { ISubcode } from "@/types/common/ISubcode"

interface IProfileState {
  position: ISubcode | null
  track: ISubcode | null
  techStack: ISubcode[]
  goal: ISubcode | null
  mbti: ISubcode | null
  setCodes: (data: Partial<IProfileState>) => void
  reset: () => void
}

export const useProfileStore = create<IProfileState>((set) => ({
  position: null,
  track: null,
  techStack: [],
  goal: null,
  mbti: null,
  setCodes: (data) => set(data),
  reset: () =>
    set({
      position: null,
      track: null,
      techStack: [],
      goal: null,
      mbti: null,
    }),
}))
