// components/charts/TeamBuildingProgress.tsx
import { useMemo } from "react"
import { ResponsiveRadialBar } from "@nivo/radial-bar"

type Section = {
  type: "전체" | "전공" | "비전공"
  totalStudentCount: number
  teamMemberCount: number
}
type TeamBuildingApi = {
  all: Section
  major: Section
  nonMajor: Section
}

function pct(n: number, d: number) {
  if (!d) return 0
  return Math.round((n / d) * 100)
}

function ProgressBar({ label, value, max }: { label: string; value: number; max: number }) {
  const ratio = max ? (value / max) * 100 : 0
  return (
    <div className="flex flex-col gap-1">
      <div className="text-text/80 flex items-center justify-between text-sm">
        <span>{label}</span>
        <span className="tabular-nums">
          {value} / {max} 명
        </span>
      </div>
      <div className="h-2 w-full rounded-full bg-zinc-200">
        <div className="h-2 rounded-full bg-[#6C5CE7]" style={{ width: `${ratio}%` }} />
      </div>
    </div>
  )
}

export default function TeamBuildingProgress({ data }: { data: TeamBuildingApi }) {
  const overallPercent = pct(data.all.teamMemberCount, data.all.totalStudentCount)

  return (
    <div className="border-line rounded-lg border bg-white p-4">
      {/* 반원 하단 여백 없음 */}
      <div className="flex justify-center">
        {/* <SemicircleChart value={overallPercent} width={260} thickness={30} /> */}
      </div>

      <div className="mt-4 flex flex-col gap-3">
        <ProgressBar label="전공" value={data.major.teamMemberCount} max={data.major.totalStudentCount} />
        <ProgressBar label="비전공" value={data.nonMajor.teamMemberCount} max={data.nonMajor.totalStudentCount} />
      </div>
    </div>
  )
}
