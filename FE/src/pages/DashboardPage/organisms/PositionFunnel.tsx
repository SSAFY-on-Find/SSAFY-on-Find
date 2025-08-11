import { useEffect, useMemo, useState } from "react"
import { ResponsiveFunnel } from "@nivo/funnel"
import { ResponsiveWaffle } from "@nivo/waffle"

import { PositionTag } from "@/components/atoms"
import type { IPositionRatioNode, PositionRow, TeamName } from "@/types/dashboard"
import { positionColor } from "@/utils/positionColor"

type Props = { api: PositionRow[] }

const ROW_H = 72

function pickMajorNon(arr: IPositionRatioNode[]) {
  const isTeam = arr.find((d) => d.name === "isTeam")?.count ?? 0
  const notTeam = arr.find((d) => d.name === "notTeam")?.count ?? 0
  return { isTeam, notTeam }
}

// HEX lighten 유틸
function lighten(hex: string, amt = 0) {
  const c = hex.replace("#", "")
  const num = parseInt(c, 16)
  const r = Math.min(255, ((num >> 16) & 0xff) + Math.round((255 * amt) / 100))
  const g = Math.min(255, ((num >> 8) & 0xff) + Math.round((255 * amt) / 100))
  const b = Math.min(255, (num & 0xff) + Math.round((255 * amt) / 100))
  return `#${((1 << 24) | (r << 16) | (g << 8) | b).toString(16).slice(1)}`
}

/** 전공/비전공 Waffle (2x10=20칸) - 지연 후 채우기 */
function MajorWaffle({
  isTeam,
  notTeam,
  teamColor = "var(--color-main)",
  delay = 250,
  cells = 20,
  rows = 2,
  columns = 10,
}: {
  isTeam: number
  notTeam: number
  teamColor?: string
  delay?: number
  cells?: number
  rows?: number
  columns?: number
}) {
  type WaffleDatum = { id: TeamName; label: string; value: number }
  const total = Math.max(1, isTeam + notTeam)
  const targetYes = Math.round((isTeam / total) * cells)
  const targetNo = cells - targetYes

  const [data, setData] = useState<WaffleDatum[]>([
    { id: "isTeam", label: "팀 O", value: 0 },
    { id: "notTeam", label: "팀 X", value: 0 },
  ])

  useEffect(() => {
    setData([
      { id: "isTeam", label: "팀 O", value: 0 },
      { id: "notTeam", label: "팀 X", value: 0 },
    ])
    const t = setTimeout(() => {
      setData([
        { id: "isTeam", label: "팀 O", value: targetYes },
        { id: "notTeam", label: "팀 X", value: targetNo },
      ])
    }, delay)
    return () => clearTimeout(t)
  }, [targetYes, targetNo, delay])

  return (
    <div className="h-12 w-28">
      <ResponsiveWaffle<WaffleDatum>
        data={data}
        total={cells}
        rows={rows}
        columns={columns}
        // colors={(d) => (d.id === "isTeam" ? "#6C5CE7" : "#E6E6E6")}
        colors={(d) => (d.id === "isTeam" ? teamColor : "var(--color-line)")}
        margin={{ top: 2, right: 2, bottom: 2, left: 2 }}
        padding={1}
        borderRadius={2}
        borderWidth={0}
        fillDirection="right"
        isInteractive
        animate
        motionConfig="gentle"
        legends={[]}
        valueFormat={(v) => `${Math.round((Number(v) / cells) * 100)}%`}
        theme={{
          tooltip: {
            container: { minWidth: 120, whiteSpace: "nowrap", wordBreak: "keep-all" },
          },
        }}
      />
    </div>
  )
}

export default function PositionFunnel({ api }: Props) {
  // 인기 순 정렬
  const sorted = useMemo(() => [...api].sort((a, b) => b.totalCount - a.totalCount), [api])

  type FunnelDatum = { id: string; value: number; label: string; color?: string }
  const targetFunnelData: FunnelDatum[] = useMemo(
    () => sorted.map((r) => ({ id: String(r.position), value: r.totalCount, label: String(r.position) })),
    [sorted]
  )
  const chartHeight = Math.max(1, sorted.length) * ROW_H

  // 초기 애니메이션: 0 → 실제값
  const [mounted, setMounted] = useState(false)
  const [funnelData, setFunnelData] = useState<FunnelDatum[]>(() => targetFunnelData.map((d) => ({ ...d, value: 0 })))
  useEffect(() => {
    setMounted(false)
    setFunnelData(targetFunnelData.map((d) => ({ ...d, value: 0 })))
    const raf = requestAnimationFrame(() => {
      setMounted(true)
      setFunnelData(targetFunnelData)
    })
    return () => cancelAnimationFrame(raf)
  }, [targetFunnelData])

  const base = "#6C5CE7"
  const palette = useMemo(() => {
    const n = Math.max(1, targetFunnelData.length)
    const start = 25
    const end = 65
    const denom = Math.max(1, n - 1)
    return Array.from({ length: n }, (_, i) => lighten(base, start + ((end - start) * i) / denom))
  }, [targetFunnelData.length])

  const funnelDataWithColor = useMemo(
    () => funnelData.map((d, i) => ({ ...d, color: palette[i] })),
    [funnelData, palette]
  )

  return (
    <div className="flex gap-6 px-5">
      {/* 왼쪽: 포지션 인기 Funnel */}
      <div style={{ height: chartHeight }} className="relative min-w-0 flex-1">
        <ResponsiveFunnel<FunnelDatum>
          data={funnelDataWithColor}
          margin={{ top: 8, right: 8, bottom: 8, left: 8 }}
          valueFormat={(v) => `${v}명`}
          // colors={{ scheme: "purple_blue" }}
          colors={{ datum: "color" }}
          shapeBlending={0.6}
          direction="vertical"
          enableLabel
          labelColor={{ theme: "labels.text.fill" }}
          isInteractive
          animate
          motionConfig="gentle"
          enableBeforeSeparators={false}
          enableAfterSeparators={true}
          afterSeparatorLength={50}
          afterSeparatorOffset={20}
          currentPartSizeExtension={13}
          currentBorderWidth={30}
          theme={{
            tooltip: {
              container: { minWidth: 140, whiteSpace: "nowrap", wordBreak: "keep-all" },
            },
          }}
        />
      </div>

      {/* 오른쪽: 포지션별 Waffle (컨테이너는 페이드 유지) */}
      <div
        className="flex flex-col gap-4 transition-opacity duration-500"
        style={{ height: chartHeight, opacity: mounted ? 1 : 0 }}
      >
        {api.map((r, i) => {
          const { isTeam, notTeam } = pickMajorNon(r.teamType)
          return (
            <div
              key={r.position}
              className="border-line flex h-[65px] w-[220px] items-center justify-between rounded-md border bg-white px-4 transition-transform duration-500 will-change-transform"
              style={{
                transform: mounted ? "translateY(0)" : "translateY(4px)",
              }}
            >
              {/* <div className="w-16 truncate text-center text-sm text-subtext">{r.position}</div> */}
              <PositionTag positionName={r.position} />
              {/* 👇 페이드가 거의 끝난 뒤(500ms) + 순차 80ms 간격으로 채우기 시작 */}
              <MajorWaffle
                isTeam={isTeam}
                notTeam={notTeam}
                delay={500 + i * 80}
                teamColor={positionColor(r.position)}
              />
            </div>
          )
        })}
      </div>
    </div>
  )
}
