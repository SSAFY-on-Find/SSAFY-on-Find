import { MajorTag, PositionTag, UserImg } from "@/components/atoms"

interface IChatUser {
  name: string
  major: string
  position: string
  hasTeam: boolean
  profile?: string
}

function ChatListItem({ name, major, position, hasTeam, profile }: IChatUser) {
  return (
    <div className="hover:bg-main/10 flex w-full cursor-pointer items-center justify-start gap-2 rounded-md px-1 py-1">
      <UserImg name={name} size={"s"} showTeamBadge={true} hasTeam={hasTeam} url={profile} />
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
    <div className="flex w-full flex-col items-start gap-2 px-2 py-3 whitespace-nowrap">
      <ChatListItem name={"김싸피"} major={"비전공"} position={"임베디드"} hasTeam={true} />
      <ChatListItem name={"이싸"} major={"전공"} position={"풀스텍"} hasTeam={false} />
      <ChatListItem name={"박싸피"} major={"비전공"} position={"백엔드"} hasTeam={true} />
      <ChatListItem name={"조싸피"} major={"전공"} position={"모바일"} hasTeam={true} />
      <ChatListItem name={"이싸피"} major={"비전공"} position={"임베디드"} hasTeam={false} />
      <ChatListItem name={"김박싸피"} major={"비전공"} position={"임베디드"} hasTeam={true} />
      <ChatListItem name={"이싸"} major={"전공"} position={"풀스텍"} hasTeam={false} />
      <ChatListItem name={"박싸피"} major={"비전공"} position={"백엔드"} hasTeam={true} />
      <ChatListItem name={"조싸피"} major={"전공"} position={"모바일"} hasTeam={true} />
      <ChatListItem name={"이싸피"} major={"비전공"} position={"임베디드"} hasTeam={false} />
      <ChatListItem name={"김싸피"} major={"비전공"} position={"임베디드"} hasTeam={true} />
      <ChatListItem name={"이싸"} major={"전공"} position={"풀스텍"} hasTeam={false} />
      <ChatListItem name={"박싸피"} major={"비전공"} position={"백엔드"} hasTeam={true} />
      <ChatListItem name={"조싸피"} major={"전공"} position={"모바일"} hasTeam={true} />
      <ChatListItem name={"이싸피"} major={"비전공"} position={"임베디드"} hasTeam={false} />
    </div>
  )
}

export default ChatList
