// ChatListItem.tsx
import { MajorTag, PositionTag, UserImg } from "@/components/atoms"
import type { IChatRoomInfo } from "@/types/chat/chat"

interface ChatListItemProps {
  roomInfo: IChatRoomInfo
  onClick: () => void
}

function ChatListItem({ roomInfo, onClick }: ChatListItemProps) {
  const unReadBG = roomInfo.isRead ? "" : "bg-main" // isRead 정상 작동시 사용하기
  return (
    <div
      className={`hover:bg-main/10 flex w-full cursor-pointer items-center justify-start gap-2 rounded-md px-1 py-1`}
      onClick={onClick}
    >
      <UserImg
        name={roomInfo.targetUsername}
        size={"s"}
        showTeamBadge={true}
        hasTeam={roomInfo.hasTeam}
        url={roomInfo.targetProfileImageUrl}
      />
      <div className="text-sm font-medium">{roomInfo.targetUsername}</div>
      <div className="flex items-center justify-center gap-1">
        <MajorTag tagContent={roomInfo.major} />
        <PositionTag positionName={roomInfo.position.subcodeName} />
      </div>
    </div>
  )
}

export default ChatListItem
