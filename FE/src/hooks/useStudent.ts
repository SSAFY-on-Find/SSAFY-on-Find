import { useMutation, useQuery } from "@tanstack/react-query"

import { studentApi, studentInfoApi } from "@/apis/studentApi"

export const useStudentLogin = () => {
  return useMutation({
    mutationFn: async (studentId: string) => {
      try {
        const response = await studentApi.login(studentId)
        if (response.status !== "SUCCESS") {
          throw new Error("로그인에 실패하였습니다.")
        }
        return response.data
      } catch (error) {
        if (error instanceof Error) {
          throw new Error(error.message)
        }
        throw new Error("로그인 중 오류가 발생했습니다.")
      }
    },
  })
}

export const useStudentInfo = (studentId: number) => {
  return useQuery({
    queryKey: ["StudentInfo", studentId],
    queryFn: async () => {
      const response = await studentInfoApi.getStudentInfo(studentId)
      if (response.status !== "SUCCESS") {
        throw new Error("학생 정보 조회에 실패했습니다.")
      }
      return response.data
    },
    gcTime: 10 * 60 * 1000,
    enabled: !!studentId,
  })
}
