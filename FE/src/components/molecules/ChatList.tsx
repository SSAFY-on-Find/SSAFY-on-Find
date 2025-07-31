import { MajorTag, PositionTag } from "@/components/atoms"

interface IChatUser {
  img: string
  name: string
  major: string
  position: string
}

function Chat({ img, name, major, position }: IChatUser) {
  return (
    <div className="flex items-center justify-center gap-2">
      <div className="bg-main rounded-full"></div>
      <div className="text-sm font-medium">{name}</div>
      <div className="flex items-center justify-center gap-1">
        <MajorTag tagContent={major} />
        <PositionTag positionName={position} />
      </div>
    </div>
  )
}

function ChatList() {
  return (
    <div className="flex flex-col gap-2 px-2 py-4">
      <Chat img={"SL"} name={"김싸피"} major={"전공"} position={"풀스텍"} />
    </div>
  )
}

export default ChatList
