import { CheckTag } from "@/components/atoms"
import type { ISubcode } from "@/types/common"

type MajorFilter = "전공" | "비전공" | null
type TeamFilter = "IN_TEAM" | "NO_TEAM" | null

interface IFilterBox {
  positions: ISubcode[]
  tracks: ISubcode[]

  selectedPositions: Set<string>
  selectedTracks: Set<string>
  selectedMajor: MajorFilter
  teamFilter: TeamFilter

  onTogglePosition: (code: string) => void
  onToggleTrack: (code: string) => void
  onToggleMajor: (val: Exclude<MajorFilter, null>) => void
  onToggleTeam: (val: Exclude<TeamFilter, null>) => void
}

export function FilterBox({
  positions,
  tracks,
  selectedPositions,
  selectedTracks,
  selectedMajor,
  teamFilter,
  onTogglePosition,
  onToggleTrack,
  onToggleMajor,
  onToggleTeam,
}: IFilterBox) {
  return (
    <div className="border-line flex w-full flex-col gap-5 rounded-lg border bg-white px-8 py-5">
      {/* 포지션 */}
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 shrink-0 text-base font-semibold">포지션</div>
        <div className="flex flex-row flex-wrap gap-2">
          {positions.map((p) => (
            <CheckTag
              key={p.subcode}
              tagContent={p.subcodeName}
              isChecked={selectedPositions.has(p.subcode)}
              onToggle={() => onTogglePosition(p.subcode)}
            />
          ))}
        </div>
      </div>

      {/* 전공 */}
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 shrink-0 text-base font-semibold">전공</div>
        <div className="flex flex-row flex-wrap gap-2">
          <CheckTag tagContent="전공" isChecked={selectedMajor === "전공"} onToggle={() => onToggleMajor("전공")} />
          <CheckTag
            tagContent="비전공"
            isChecked={selectedMajor === "비전공"}
            onToggle={() => onToggleMajor("비전공")}
          />
        </div>
      </div>

      {/* 트랙 */}
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 shrink-0 text-base font-semibold">트랙</div>
        <div className="flex flex-row flex-wrap gap-2">
          {tracks.map((t) => (
            <CheckTag
              key={t.subcode}
              tagContent={t.subcodeName}
              isChecked={selectedTracks.has(t.subcode)}
              onToggle={() => onToggleTrack(t.subcode)}
            />
          ))}
        </div>
      </div>

      {/* 팀 */}
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 shrink-0 text-base font-semibold">팀</div>
        <div className="flex flex-row flex-wrap gap-2">
          <CheckTag
            tagContent="팀에 소속됨"
            isChecked={teamFilter === "IN_TEAM"}
            onToggle={() => onToggleTeam("IN_TEAM")}
          />
          <CheckTag
            tagContent="팀에 소속되지 않음"
            isChecked={teamFilter === "NO_TEAM"}
            onToggle={() => onToggleTeam("NO_TEAM")}
          />
        </div>
      </div>
    </div>
  )
}

export default FilterBox
