import { Heart } from "lucide-react"

import type { ITeamDetail } from "@/types"

import { Button, MainTag, PositionTag, UserImg } from "../atoms"

interface ITeamCard extends ITeamDetail {
  isFavorite?: boolean
  onClickFavorite: () => void
  onClickCard: () => void
}

function TeamCard({
  id,
  name,
  description,
  track,
  position,
  members,
  isFavorite = false,
  onClickFavorite,
  onClickCard,
}: ITeamCard) {
  const handleFavoriteClick = (e: React.MouseEvent) => {
    e.stopPropagation()
    onClickFavorite()
  }
  const handleButtonClick = (e: React.MouseEvent) => {
    e.stopPropagation()
  }

  return (
    <div
      key={id}
      onClick={onClickCard}
      className="border-main flex max-w-[260px] min-w-[260px] flex-col rounded-md border-2"
    >
      <div className="flex flex-col gap-[11px] p-[25px] pb-[18px]">
        <div className="inline-flex items-center justify-between">
          <div className="inline-flex items-center gap-3">
            <h3 className="text-main text-2xl font-bold">{name}</h3>
            <MainTag tagContent={track} />
          </div>
          <Heart
            className={`cursor-pointer transition-colors ${isFavorite ? "fill-red-500 text-red-500" : "text-subtext"}`}
            onClick={handleFavoriteClick}
          />
        </div>
        <p className="text-text line-clamp-2 text-sm">{description}</p>
      </div>
      <div className="p-[18px] pt-0">
        <div className="mb-5 flex px-[5px]">
          {members.map((ele, idx) => (
            <div key={ele.id} className={idx > 0 ? "-ml-2" : ""}>
              <UserImg name={ele.name} size={"xs"} showTeamBadge={false} />
            </div>
          ))}
        </div>
        <p className="text-text mb-[10px] text-sm font-semibold">모집중인 포지션</p>
        <div className="flex gap-[10px]">
          {position.map((ele) => (
            <PositionTag positionName={ele} />
          ))}
        </div>
      </div>
      <div className="flex gap-4 p-[25px] pt-0" onClick={handleButtonClick}>
        <Button size={"s"} isIcon={false} text="지원하기" variant="primary" onClick={function (): void {}} />
        <Button size={"s"} isIcon={false} text="팀 합치기 제안" variant="outline" onClick={function (): void {}} />
      </div>
    </div>
  )
}
export default TeamCard
