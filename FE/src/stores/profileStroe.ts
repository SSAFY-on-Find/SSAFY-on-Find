import { create } from "zustand"

import type { IFile } from "@/types/common/IFile"
import type { ISubcode } from "@/types/common/ISubcode"

interface IProfileState {
  position: ISubcode | null
  track: ISubcode | null
  techStack: ISubcode[]
  goal: ISubcode | null
  profileImageUrl: string
  strength: string[]
  mbti: ISubcode | null
  description: string
  portfolio: IFile | null
  setCodes: (data: Partial<IProfileState>) => void
  reset: () => void
}

export const useProfileStore = create<IProfileState>((set) => ({
  position: null,
  track: null,
  techStack: [],
  goal: null,
  profileImageUrl: "",
  strength: [],
  mbti: null,
  description: "",
  portfolio: null,
  setCodes: (data) => set(data),
  reset: () =>
    set({
      position: null,
      track: null,
      techStack: [],
      goal: null,
      profileImageUrl: "",
      strength: [],
      mbti: null,
      description: "",
      portfolio: null,
    }),
}))
