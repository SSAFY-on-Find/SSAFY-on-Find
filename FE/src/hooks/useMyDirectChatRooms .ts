import { useQuery } from "@tanstack/react-query"

import { getMyDirectChatRooms } from "@/apis/chatRoom"

export const useMyDirectChatRooms = () => {
  return useQuery({
    queryKey: ["myDirectChatRooms"],
    queryFn: getMyDirectChatRooms,
  })
}
