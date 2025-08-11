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
    staleTime: 0, // 캐시를 즉시 stale 처리
    refetchOnMount: "always", // 마운트 시 항상 재요청
    enabled: !!studentId,
  })
}

export const useStudentList = () => {
  return useQuery({
    queryKey: ["StudentList"],
    queryFn: async () => {
      const response = await studentApi.getStudentList()
      if (response.status !== "SUCCESS") {
        throw new Error("교육생 목록 조회에 실패했습니다.")
      }
      return response.data.students
    },
    gcTime: 10 * 60 * 1000,
  })
}
