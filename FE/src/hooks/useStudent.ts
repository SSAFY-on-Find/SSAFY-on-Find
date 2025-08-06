import { useMutation } from "@tanstack/react-query"

import { studentApi } from "@/apis/studentApi"

export const useStudentLogin = () => {
  return useMutation({
    mutationFn: async (studentId: number) => {
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
