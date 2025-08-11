import type { IApiResponse } from "@/types/common"
import type { IStudentCard, IStudentInfo, IStudentSignin } from "@/types/student"

import api from "./index"

const STUDENT_BASE_URL = "/students"
export const studentApi = {
  login: async (studentId: string): Promise<IApiResponse<IStudentSignin>> => {
    const response = await api.post<IApiResponse<IStudentSignin>>(STUDENT_BASE_URL + "/sign-in", { studentId })
    return response.data
  },
  logout: async (): Promise<IApiResponse<void>> => {
    const response = await api.post<IApiResponse<void>>(STUDENT_BASE_URL + "/sign-out")
    return response.data
  },
  getStudentList: async (): Promise<IApiResponse<{ students: IStudentCard[] }>> => {
    const response = await api.get<IApiResponse<{ students: IStudentCard[] }>>(STUDENT_BASE_URL)
    return response.data
  },
}

const STUDENT_INFO_BASE_URL = "/studentInfos"
export const studentInfoApi = {
  getStudentInfo: async (studentId: number): Promise<IApiResponse<IStudentInfo>> => {
    const response = await api.get<IApiResponse<IStudentInfo>>(`${STUDENT_INFO_BASE_URL}/${studentId}`)
    return response.data
  },
}
