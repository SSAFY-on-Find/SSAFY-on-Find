import { create } from "zustand"

import type { IFile } from "@/types/common/IFile"
import type { ISubcode } from "@/types/common/ISubcode"
import type { IStudent } from "@/types/student"

export interface IProfileState {
  student: IStudent | null
  position: ISubcode | null
  track: ISubcode | null
  techStack: ISubcode[]
  goal: ISubcode | null
  profileImageUrl: string
  profileImageFile: File | null // 새로 업로드할 파일
  strength: string[]
  mbti: ISubcode | null
  description: string
  portfolio: IFile | null
  portfolioFile: File | null // 새로 업로드할 파일
  setCodes: (data: Partial<IProfileState>) => void
  reset: () => void
}

export const useProfileStore = create<IProfileState>((set) => ({
  student: null,
  position: null,
  track: null,
  techStack: [],
  goal: null,
  profileImageUrl: "",
  profileImageFile: null,
  strength: [],
  mbti: null,
  description: "",
  portfolio: null,
  portfolioFile: null,
  setCodes: (data) => set(data),
  reset: () =>
    set({
      position: null,
      track: null,
      techStack: [],
      goal: null,
      profileImageUrl: "",
      profileImageFile: null,
      strength: [],
      mbti: null,
      description: "",
      portfolio: null,
      portfolioFile: null,
    }),
}))
