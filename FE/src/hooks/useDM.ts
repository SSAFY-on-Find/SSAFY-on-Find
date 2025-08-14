import { toast } from "react-toastify"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"

import { createDirectChatRoom, getMyDirectChatRooms } from "@/apis/chatRoom"
import type { DirectChatRoomRequest, IChatRoomInfo } from "@/types/chat/chat"

export const useMyDirectChatRooms = () => {
  return useQuery<IChatRoomInfo[]>({
    queryKey: ["myDirectChatRooms"],
    queryFn: async () => {
      const response = await getMyDirectChatRooms()
      return response.chatRooms
    },
    refetchInterval: 5000,
  })
}

export const useCreateDirectChatRoom = () => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data: DirectChatRoomRequest) => createDirectChatRoom(data),

    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["myDirectChatRooms"] })
    },
    onError: (error) => {
      toast.error("1:1 채팅방 생성에 실패했습니다. 다시시도해 주세요")
      console.error("1:1 채팅방 생성에 실패했습니다.", error)
    },
  })
}
