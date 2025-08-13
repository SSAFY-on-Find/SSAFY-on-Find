import { useNavigate } from "react-router-dom"
import { FilePenLine, LogOut } from "lucide-react"

import type { ITeamDetails } from "@/types/team"

import { Button, MainTag, PositionTag, UserImg } from "../atoms"
interface ITeamDetailModal extends ITeamDetails {
  varient?: "myteam"
  onLeaveTeam?: () => void
  onClose?: () => void
}
function TeamDetail({
  teamName,
  teamDescription,
  track,
  majorCount,
  nonMajorCount,
  teamCount,
  positions,
  members,
  isFavorite,
  varient,
  onLeaveTeam = () => {},
  onClose = () => {},
}: ITeamDetailModal) {
  const navigate = useNavigate()
  const majorMembers = members.filter((ele) => ele.major === "전공")
  const nonMajorMembers = members.filter((ele) => ele.major === "비전공")
  const handleEditClick = () => {
    navigate(`/edit-team`)
  }
  const handleMemberClick = (studentId: string | number) => {
    onClose()
    navigate(`/studentlist/${studentId}`)
  }

  return (
    <div className="w-full">
      <div className="flex flex-col gap-2 p-8">
        <div className="flex justify-between">
          <div className="inline-flex items-center gap-3">
            <h3 className="text-text text-2xl font-bold">{teamName}</h3>
            <MainTag tagContent={track.subcodeName} />
          </div>
          {varient === "myteam" ? (
            <div className="flex gap-3">
              <div className="w-20">
                <Button
                  size={"m"}
                  isIcon={true}
                  Icon={FilePenLine}
                  variant="text"
                  text="편집"
                  onClick={handleEditClick}
                />
              </div>
              <div className="w-20">
                <Button size={"m"} isIcon={true} Icon={LogOut} variant="danger" text="탈퇴" onClick={onLeaveTeam} />
              </div>
            </div>
          ) : (
            <></>
          )}
        </div>
        <p className="text-subtext text-sm text-pretty">{teamDescription}</p>
      </div>
      <div className="px-8">
        <p className="text-text mb-[10px] text-sm font-semibold">모집중인 포지션</p>
        <div className="flex gap-[10px]">
          {positions && positions.map((pos, idx) => <PositionTag key={idx} positionName={pos.positionName} />)}
        </div>
        <p className="text-text mt-[30px] mb-[15px] text-sm font-semibold">현재팀원 ({teamCount} / 6)</p>
        <div className="flex">
          <div>
            {majorMembers.length === 0 ? (
              <></>
            ) : (
              <>
                <p className="text-text mb-[10px] text-sm font-semibold">전공</p>
                <div className="flex">
                  {majorMembers.map((ele, idx) => (
                    <div
                      key={idx}
                      className="hover:bg-main/10 flex flex-col items-center rounded-md p-1 px-[10px] hover:cursor-pointer"
                      onClick={() => handleMemberClick(ele.studentId)}
                    >
                      <UserImg name={ele.name} size={"m"} showTeamBadge={false} url={ele.profileImageUrl} />
                      <p className="mt-2 mb-[5px] text-sm">{ele.name}</p>
                      {ele.position && <PositionTag positionName={ele.position.subcodeName} />}
                    </div>
                  ))}
                </div>
              </>
            )}
          </div>
          {majorMembers.length === 0 || nonMajorMembers.length === 0 ? (
            <></>
          ) : (
            <>
              <div className="mx-4 w-px bg-gray-300"></div>
            </>
          )}

          <div>
            {nonMajorMembers.length === 0 ? (
              <></>
            ) : (
              <>
                <p className="text-text mb-[10px] text-sm font-semibold">비전공</p>
                <div className="flex">
                  {nonMajorMembers.map((ele, idx) => (
                    <div
                      key={idx}
                      onClick={() => handleMemberClick(ele.studentId)}
                      className="hover:bg-main/10 flex flex-col items-center rounded-md p-1 px-[10px] hover:cursor-pointer"
                    >
                      <UserImg name={ele.name} size={"m"} showTeamBadge={false} url={ele.profileImageUrl} />
                      <p className="mt-2 mb-[5px] text-sm">{ele.name}</p>
                      {ele.position && <PositionTag positionName={ele.position.subcodeName} />}
                    </div>
                  ))}
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
export default TeamDetail
