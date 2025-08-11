import { Heart } from "lucide-react"

import type { ITeamCard } from "@/types/team"

import { Button, MainTag, PositionTag, UserImg, WhiteTag } from "../atoms"

interface ITeamCardElement extends ITeamCard {
  userTeamId?: number | null
}

function TeamCard({
  teamId,
  teamName,
  description,
  track,
  recruitments,
  members,
  isRecruitingComplete,
  isFavorite,
  onClickFavorite,
  onClickCard,
  variant = "default",
  userTeamId,
}: ITeamCardElement) {
  const handleFavoriteClick = (e: React.MouseEvent) => {
    e.stopPropagation()
    onClickFavorite()
  }
  const handleButtonClick = (e: React.MouseEvent) => {
    e.stopPropagation()
  }

  const renderButtons = () => {
    if (userTeamId === null) {
      return <Button size={"s"} isIcon={false} text="지원하기" variant={btnRecruit} onClick={() => {}} />
    } else if (userTeamId !== teamId) {
      return (
        <>
          <Button size={"m"} isIcon={false} text="지원하기" variant={btnRecruit} onClick={() => {}} />
          <Button size={"m"} isIcon={false} text="팀 합치기" variant={btnMerge} onClick={() => {}} />
        </>
      )
    }
    return <Button size={"m"} isIcon={false} text="내 팀 바로가기" variant={btnRecruit} onClick={() => {}} />
  }

  // 카드 스타일
  const bgColor = variant === "main" ? "bg-gradient-to-tl from-main to-main/80" : "bg-white"
  const textColor = variant === "main" ? "text-white" : "text-text"
  const descriptionColor = variant === "main" ? "text-white" : "text-subtext"
  const titleColor = variant === "main" ? "text-white" : "text-text"
  const heartColor = variant === "main" ? "text-white" : "text-subtext"
  const heartFillColor = variant === "main" ? "fill-white text-white" : "fill-red-500 text-red-500"
  const btnRecruit = variant === "main" ? "white-full" : "primary"
  const btnMerge = variant === "main" ? "white-line" : "outline"
  return (
    <div
      key={teamId}
      onClick={onClickCard}
      className={`${bgColor} border-line flex max-h-[300px] min-h-[300px] max-w-[260px] min-w-[260px] flex-col justify-between rounded-lg border-2 px-2 py-1`}
    >
      <div className="flex flex-col gap-[11px] p-[18px] pb-[18px]">
        <div className="inline-flex items-center justify-between">
          <div className="inline-flex items-center gap-3">
            <h3 className={`${titleColor} text-2xl font-bold`}>{teamName}</h3>
            {variant === "main" ? (
              <WhiteTag tagContent={track.subcodeName} />
            ) : (
              <MainTag tagContent={track.subcodeName} />
            )}
          </div>
          {userTeamId !== teamId && (
            <Heart
              className={`cursor-pointer transition-colors ${isFavorite ? heartFillColor : heartColor}`}
              onClick={handleFavoriteClick}
            />
          )}
        </div>
        <p className={`${descriptionColor} line-clamp-2 min-h-8 text-sm`}>{description}</p>
      </div>
      <div className="p-[20px] pt-0">
        <div className="mb-5 flex px-[5px]">
          {members.map((ele, idx) => (
            <div key={ele.studentId} className={idx > 0 ? "-ml-2" : ""}>
              <UserImg name={ele.name} size={"xs"} showTeamBadge={false} url={ele.profileImageUrl} />
            </div>
          ))}
        </div>
        <p className={`${textColor} mb-[10px] text-sm font-semibold`}>모집중인 포지션</p>
        <div className="flex gap-[10px] overflow-hidden">
          {recruitments.map((pos) => (
            <>
              {variant === "main" ? (
                <WhiteTag tagContent={pos.positionName} fillBg={true} />
              ) : (
                <PositionTag positionName={pos.positionName} />
              )}
            </>
          ))}
        </div>
      </div>
      <div className="flex gap-4 p-[18px] pt-0" onClick={handleButtonClick}>
        {renderButtons()}
      </div>
    </div>
  )
}
export default TeamCard
