import { MainTag, PositionTag, UserImg } from "../atoms"

import Modal from "./Modal"

interface ITeamDetailModal {
  isOpen: boolean
  onClose: () => void
  teamData: {
    id: string
    name: string
    description: string
    track: string
    position: string[]
    maxMembers: number
    currentMembers: number
    members: Array<{
      id: string
      name: string
      position: string
      major: string
    }>
  }
}

function TeamDetailModal({ isOpen, onClose, teamData }: ITeamDetailModal) {
  const majorMembers = teamData.members.filter((ele) => ele.major === "전공")
  const nonMajorMembers = teamData.members.filter((ele) => ele.major === "비전공")
  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"m"}>
      <div className="p-6">
        <div className="flex flex-col gap-2 p-8">
          <div className="inline-flex items-center gap-3">
            <h3 className="text-text text-2xl font-bold">{teamData.name}</h3>
            <MainTag tagContent={teamData.track} />
          </div>
          <p className="text-subtext text-sm text-pretty">{teamData.description}</p>
        </div>
        <div className="px-8">
          <p className="text-text mb-[10px] text-sm font-semibold">모집중인 포지션</p>
          <div className="flex gap-[10px]">
            {teamData.position.map((ele) => (
              <PositionTag positionName={ele} />
            ))}
          </div>
          <p className="text-text mt-[30px] mb-[15px] text-sm font-semibold">
            현재팀원 ({teamData.currentMembers} / {teamData.maxMembers})
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
        {/* 버튼 생기면 추가하기 */}
      </div>
    </Modal>
  )
}

export default TeamDetailModal
