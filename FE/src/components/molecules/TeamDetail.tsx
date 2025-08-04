import type { ITeamDetail } from "@/types"

import { MainTag, PositionTag, UserImg } from "../atoms"

function TeamDetail({ id, name, description, track, position, maxMembers, currentMembers, members }: ITeamDetail) {
  const majorMembers = members.filter((ele) => ele.major === "전공")
  const nonMajorMembers = members.filter((ele) => ele.major === "비전공")

  return (
    <div>
      <div className="flex flex-col gap-2 p-8">
        <div className="inline-flex items-center gap-3">
          <h3 className="text-text text-2xl font-bold">{name}</h3>
          <MainTag tagContent={track} />
        </div>
        <p className="text-subtext text-sm text-pretty">{description}</p>
      </div>
      <div className="px-8">
        <p className="text-text mb-[10px] text-sm font-semibold">모집중인 포지션</p>
        <div className="flex gap-[10px]">
          {position.map((ele) => (
            <PositionTag positionName={ele} />
          ))}
        </div>
        <p className="text-text mt-[30px] mb-[15px] text-sm font-semibold">
          현재팀원 ({currentMembers} / {maxMembers})
        </p>
        <div className="flex">
          <div>
            <p className="text-text mb-[10px] text-sm font-semibold">전공</p>
            <div className="flex">
              {majorMembers.map((ele) => (
                <div className="flex flex-col items-center px-[10px]">
                  <UserImg name={ele.name} size={"m"} showTeamBadge={false} />
                  <p className="mt-2 mb-[5px] text-sm">{ele.name}</p>
                  <PositionTag positionName={ele.position} />
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
                  <PositionTag positionName={ele.position} />
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
