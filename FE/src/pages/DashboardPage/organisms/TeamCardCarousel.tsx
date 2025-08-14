import { useEffect, useLayoutEffect, useRef, useState } from "react"
import { ChevronLeft, ChevronRight } from "lucide-react"

import { Button } from "@/components/atoms"
import { TeamCard } from "@/components/molecules"
import { useUserStore } from "@/stores/userStore"

type TeamItem = React.ComponentProps<typeof TeamCard>
const VISIBLE = 2

export default function TeamCardCarousel({ items }: { items: TeamItem[] }) {
  const [idx, setIdx] = useState(0) // 현재 시작 인덱스
  const [cardW, setCardW] = useState(0) // 카드 하나의 실제 너비(px)
  const [gap, setGap] = useState(12) // gap(px) - 기본 12px (tailwind gap-3)
  const trackRef = useRef<HTMLDivElement>(null)
  const userTeamId = useUserStore((state) => state.user?.teamId)

  // 최초 마운트 시 카드 너비 & gap 측정
  useLayoutEffect(() => {
    const track = trackRef.current
    if (!track || track.children.length === 0) return
    const first = track.children[0] as HTMLElement
    setCardW(first.getBoundingClientRect().width)

    // gap 가져오기 (브라우저별 속성명 대응)
    const cs = getComputedStyle(track)
    const g = parseFloat(cs.columnGap || cs.gap || "12")
    if (!Number.isNaN(g)) setGap(g)
  }, [items])

  // 아이템 수 변경 시 인덱스 보정
  const maxIdx = Math.max(0, items.length - VISIBLE)
  useEffect(() => {
    if (idx > maxIdx) setIdx(maxIdx)
  }, [idx, maxIdx])

  const canPrev = idx > 0
  const canNext = idx < maxIdx

  // 뷰포트 폭 = 카드*2 + gap 1개
  const viewportW = cardW > 0 ? cardW * VISIBLE + gap : undefined
  const translateX = -(idx * (cardW + gap))

  return (
    <div className="relative">
      {/* 컨트롤 (사용자 Button 컴포넌트) */}
      <div className="absolute -top-12 right-0 flex gap-2">
        <div className={`w-9 ${!canPrev ? "pointer-events-none opacity-40" : ""}`}>
          <Button size="m" isIcon Icon={ChevronLeft} variant="text" onClick={() => setIdx((i) => Math.max(0, i - 1))} />
        </div>
        <div className={`w-9 ${!canNext ? "pointer-events-none opacity-40" : ""}`}>
          <Button
            size="m"
            isIcon
            Icon={ChevronRight}
            variant="text"
            onClick={() => setIdx((i) => Math.min(maxIdx, i + 1))}
          />
        </div>
      </div>

      {/* 뷰포트: 가운데 정렬 + 딱 두 장만 보이도록 width 고정 */}
      <div className="mx-auto overflow-hidden" style={{ width: viewportW }}>
        {/* 트랙: gap은 여기에서만! */}
        <div
          ref={trackRef}
          className="flex gap-3 transition-transform duration-300 will-change-transform"
          style={{ transform: `translateX(${translateX}px)` }}
        >
          {items.map((item) => (
            <div key={item.teamId} className="shrink-0">
              <TeamCard {...item} variant="main" userTeamId={Number(userTeamId)} />
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
