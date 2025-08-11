// src/hooks/useChatQueries.ts
import { useMutation, useQueryClient } from "@tanstack/react-query"

import { createDirectChatRoom } from "@/apis/chatRoom"
import type { DirectChatRoomRequest } from "@/types/chat/chat"

export const useCreateDirectChatRoom = () => {
  const queryClient = useQueryClient()

  return useMutation({
    // 데이터를 변경하는 함수
    mutationFn: (data: DirectChatRoomRequest) => createDirectChatRoom(data),

    // Mutation 성공 시 실행할 로직
    onSuccess: () => {
      // 채팅방 생성이 성공하면, 기존의 채팅방 목록 캐시를 무효화시켜
      // useMyDirectChatRooms가 새로운 목록을 다시 불러오게 합니다.
      queryClient.invalidateQueries({ queryKey: ["myDirectChatRooms"] })
    },
    onError: (error) => {
      console.error("1:1 채팅방 생성에 실패했습니다.", error)
    },
  })
}
