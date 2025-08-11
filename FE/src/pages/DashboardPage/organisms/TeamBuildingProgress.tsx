import { useEffect, useState } from "react"
import { ResponsiveRadialBar } from "@nivo/radial-bar"
import { BasicTooltip } from "@nivo/tooltip"

import type { ITeamRatio } from "@/types/dashboard"

function pct(n: number, d: number) {
  if (!d) return 0
  return Math.round((n / d) * 100)
}

function ProgressBar({
  label,
  value,
  max,
  duration = 700, // ms
}: {
  label: string
  value: number
  max: number
  duration?: number
}) {
  const ratio = max ? (value / max) * 100 : 0

  // 1) width 애니메이션 (초기 0% -> ratio%)
  const [mounted, setMounted] = useState(false)
  useEffect(() => {
    const raf = requestAnimationFrame(() => setMounted(true))
    return () => cancelAnimationFrame(raf)
  }, [])
  // width는 값이 바뀔 때마다 transition으로 자연스럽게 갱신됨

  // 2) 숫자 카운트업 (이전 값 -> 새 값으로 보간)
  const [displayValue, setDisplayValue] = useState(0)
  useEffect(() => {
    let raf: number
    const start = performance.now()
    const from = displayValue
    const to = value

    const tick = (t: number) => {
      const p = Math.min(1, (t - start) / duration)
      // easeOutCubic
      const eased = 1 - Math.pow(1 - p, 3)
      setDisplayValue(Math.round(from + (to - from) * eased))
      if (p < 1) raf = requestAnimationFrame(tick)
    }

    raf = requestAnimationFrame(tick)
    return () => cancelAnimationFrame(raf)
  }, [value, duration])

  return (
    <div className="flex flex-col gap-1">
      <div className="text-text/80 flex items-center justify-between text-sm font-medium">
        <span>{label}</span>
        <span className="tabular-nums">
          {displayValue} / {max} 명
        </span>
      </div>

      <div className="bg-line h-3 w-full rounded-full">
        <div
          className="h-3 rounded-full bg-[#6C5CE7] transition-[width] duration-700 ease-out"
          style={{ width: mounted ? `${ratio}%` : 0 }}
        />
      </div>
    </div>
  )
}

export default function TeamBuildingProgress({ data }: { data: ITeamRatio }) {
  const y = data.all.teamMemberCount
  const max = data.all.totalStudentCount
  const percent = pct(y, max)

  // 1) 차트용 데이터: 0으로 먼저 그리고 → 다음 프레임에 실제 값으로
  const [chartData, setChartData] = useState([{ id: "반 전체", data: [{ x: "빌딩 완료", y: 0 }] }])
  useEffect(() => {
    const raf = requestAnimationFrame(() => {
      setChartData([{ id: "반 전체", data: [{ x: "빌딩 완료", y }] }])
    })
    return () => cancelAnimationFrame(raf)
  }, [y])

  // 2) 중앙 퍼센트 숫자도 카운트업
  const [displayPercent, setDisplayPercent] = useState(0)
  useEffect(() => {
    let raf: number
    const start = performance.now()
    const from = displayPercent
    const to = percent
    const duration = 700

    const tick = (t: number) => {
      const p = Math.min(1, (t - start) / duration)
      const eased = 1 - Math.pow(1 - p, 3)
      setDisplayPercent(Math.round(from + (to - from) * eased))
      if (p < 1) raf = requestAnimationFrame(tick)
    }

    raf = requestAnimationFrame(tick)
    return () => cancelAnimationFrame(raf)
  }, [percent])

  return (
    <div className="p-4">
      {/* 반원 라디얼 게이지 */}
      <div className="relative">
        <div className="h-50">
          <ResponsiveRadialBar
            data={chartData}
            maxValue={max}
            startAngle={-90}
            endAngle={90}
            innerRadius={0.5}
            padding={0.3}
            cornerRadius={0}
            enableTracks
            enableRadialGrid={false}
            enableCircularGrid={false}
            tracksColor="#E5E7EB"
            colors={["#6C5CE7"]}
            radialAxisStart={null}
            circularAxisOuter={null}
            legends={[]}
            labelsSkipAngle={0}
            motionConfig="default"
            transitionMode="startAngle"
            isInteractive={true}
            tooltip={() => <BasicTooltip id="빌딩 완료" value={`${y} / ${max} 명`} color="#6C5CE7" enableChip />}
          />
        </div>
        {/* 중앙 퍼센트 라벨 */}
        <div className="pointer-events-none absolute inset-0 -top-12 flex flex-col items-center justify-center">
          <div className="text-text text-sm">전체</div>
          <div className="text-text text-lg font-bold">{percent}%</div>
        </div>
      </div>

      {/* 하단 전공/비전공 바 */}
      <div className="relative w-full">
        <div className="absolute -top-25 mt-4 flex w-full flex-col gap-3">
          <ProgressBar label="전공" value={data.major.teamMemberCount} max={data.major.totalStudentCount} />
          <ProgressBar label="비전공" value={data.nonMajor.teamMemberCount} max={data.nonMajor.totalStudentCount} />
        </div>
      </div>
    </div>
  )
}
