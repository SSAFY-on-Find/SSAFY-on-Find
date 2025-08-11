import type { IChatRoomInfo } from "@/types/chat/chat"

import { ChatListItem } from "../atoms"

interface ChatListProps {
  rooms: IChatRoomInfo[]
  onRoomClick: (roomId: number) => void
}

function ChatList({ rooms, onRoomClick }: ChatListProps) {
  rooms.map((ele) => console.log(ele))
  return (
    <div className="flex w-full flex-col items-start gap-1 px-2 py-3 whitespace-nowrap">
      {rooms &&
        rooms.map((room) => (
          <ChatListItem key={room.chatRoomId} roomInfo={room} onClick={() => onRoomClick(room.chatRoomId)} />
        ))}
    </div>
  )
}

export default ChatList
