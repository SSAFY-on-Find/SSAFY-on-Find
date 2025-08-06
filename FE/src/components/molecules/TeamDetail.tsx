import type { ITeamDetails } from "@/types/team"

import { MainTag, PositionTag, UserImg } from "../atoms"

function TeamDetail({
  teamName,
  teamDescription,
  teamTrack,
  majorCount,
  nonMajorCount,
  teamCount,
  positions,
  members,
  isFavorite,
}: ITeamDetails) {
  console.log("members", members)
  console.log("positions", positions)
  const majorMembers = members.filter((ele) => ele.major === "전공")
  const nonMajorMembers = members.filter((ele) => ele.major === "비전공")
  return (
    <div>
      <div className="flex flex-col gap-2 p-8">
        <div className="inline-flex items-center gap-3">
          <h3 className="text-text text-2xl font-bold">{teamName}</h3>
          <MainTag tagContent={teamTrack.subcodeName} />
        </div>
        <p className="text-subtext text-sm text-pretty">{teamDescription}</p>
      </div>
      <div className="px-8">
        <p className="text-text mb-[10px] text-sm font-semibold">모집중인 포지션</p>
        <div className="flex gap-[10px]">
          {positions && positions.map((pos) => <PositionTag positionName={pos.positionName} />)}
        </div>
        <p className="text-text mt-[30px] mb-[15px] text-sm font-semibold">현재팀원 ({teamCount} / 6)</p>
        <div className="flex">
          <div>
            <p className="text-text mb-[10px] text-sm font-semibold">전공</p>
            <div className="flex">
              {majorMembers.map((ele) => (
                <div className="flex flex-col items-center px-[10px]">
                  <UserImg name={ele.name} size={"m"} showTeamBadge={false} />
                  <p className="mt-2 mb-[5px] text-sm">{ele.name}</p>
                  {ele.position && <PositionTag positionName={ele.position.subcodeName} />}
                </div>
              ))}
            </div>
          </div>
          <div className="mx-4 w-px bg-gray-300"></div>
          <div>
            <p className="text-text mb-[10px] text-sm font-semibold">비전공</p>
            <div className="flex">
              {nonMajorMembers.map((ele) => (
                <div className="flex flex-col items-center px-[10px]">
                  <UserImg name={ele.name} size={"m"} showTeamBadge={false} />
                  <p className="mt-2 mb-[5px] text-sm">{ele.name}</p>
                  {ele.position && <PositionTag positionName={ele.position.subcodeName} />}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
export default TeamDetail
