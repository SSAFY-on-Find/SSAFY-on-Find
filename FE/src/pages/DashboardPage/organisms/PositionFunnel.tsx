// components/charts/PositionFunnel.tsx
import { useEffect, useMemo, useState } from "react"
import { ResponsiveFunnel } from "@nivo/funnel"
import { ResponsiveWaffle } from "@nivo/waffle"

import { PositionTag } from "@/components/atoms"

type MajorKind = "전공" | "비전공"
type MajorItem = { name: MajorKind; count: number }
type PositionRow = { position: string; totalCount: number; majorType: MajorItem[] }
export type PositionApi = { status: "SUCCESS"; data: PositionRow[] }

const ROW_H = 72

function pickMajorNon(arr: MajorItem[]) {
  const m = arr.find((d) => d.name === "전공")?.count ?? 0
  const n = arr.find((d) => d.name === "비전공")?.count ?? 0
  return { m, n }
}

/** 전공/비전공 Waffle (2x10=20칸) - 지연 후 채우기 */
function MajorWaffle({
  m,
  n,
  delay = 250,
  cells = 20,
  rows = 2,
  columns = 10,
}: {
  m: number
  n: number
  /** 채움 시작 지연(ms) */
  delay?: number
  cells?: number
  rows?: number
  columns?: number
}) {
  type WaffleDatum = { id: MajorKind; label: string; value: number }
  const total = Math.max(1, m + n)
  const targetMajor = Math.round((m / total) * cells)
  const targetNon = cells - targetMajor

  const [data, setData] = useState<WaffleDatum[]>([
    { id: "전공", label: "전공", value: 0 },
    { id: "비전공", label: "비전공", value: 0 },
  ])

  useEffect(() => {
    // 값이 바뀔 때마다 0칸 → delay 후 목표칸으로 (애니메이션 트리거)
    setData([
      { id: "전공", label: "전공", value: 0 },
      { id: "비전공", label: "비전공", value: 0 },
    ])
    const t = setTimeout(() => {
      setData([
        { id: "전공", label: "전공", value: targetMajor },
        { id: "비전공", label: "비전공", value: targetNon },
      ])
    }, delay)
    return () => clearTimeout(t)
  }, [targetMajor, targetNon, delay])

  return (
    <div className="h-12 w-28">
      <ResponsiveWaffle<WaffleDatum>
        data={data}
        total={cells}
        rows={rows}
        columns={columns}
        colors={(d) => (d.id === "비전공" ? "#6C5CE7" : "#E6E6E6")}
        margin={{ top: 2, right: 2, bottom: 2, left: 2 }}
        padding={1}
        borderRadius={2}
        borderWidth={0}
        fillDirection="left"
        isInteractive
        animate
        motionConfig="gentle"
        legends={[]}
        valueFormat={(v) => `${Math.round(Number(v))}칸`}
        theme={{
          tooltip: {
            container: { minWidth: 120, whiteSpace: "nowrap", wordBreak: "keep-all" },
          },
        }}
      />
    </div>
  )
}

export default function PositionFunnel({ api }: { api: PositionApi }) {
  // 인기 순 정렬
  const rows = useMemo(() => [...api.data].sort((a, b) => b.totalCount - a.totalCount), [api.data])

  type FunnelDatum = { id: string; value: number; label: string }
  const targetFunnelData: FunnelDatum[] = useMemo(
    () => rows.map((r) => ({ id: r.position, value: r.totalCount, label: r.position })),
    [rows]
  )
  const chartHeight = Math.max(1, rows.length) * ROW_H

  // ✅ 초기 애니메이션: 0 → 실제값
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

  return (
    <div className="flex gap-6 px-5">
      {/* 왼쪽: 포지션 인기 Funnel */}
      <div style={{ height: chartHeight }} className="relative min-w-0 flex-1">
        <ResponsiveFunnel<FunnelDatum>
          data={funnelData}
          margin={{ top: 8, right: 8, bottom: 8, left: 8 }}
          valueFormat={(v) => `${v}명`}
          colors={{ scheme: "purple_blue" }}
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
        {rows.map((r, i) => {
          const { m, n } = pickMajorNon(r.majorType)
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
              <MajorWaffle m={m} n={n} delay={500 + i * 80} />
            </div>
          )
        })}
      </div>
    </div>
  )
}
