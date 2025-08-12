import { useRef, useState } from "react"

interface ITooltip {
  content: React.ReactNode
  children: React.ReactNode
  side?: "top" | "bottom" | "left" | "right"
  className?: string
}

function Tooltip({ content, children, side = "top", className }: ITooltip) {
  const [open, setOpen] = useState(false)
  const wrapperRef = useRef<HTMLDivElement>(null)

  // 툴팁 위치 스타일링
  const positionClass = {
    top: "bottom-full left-1/2 -translate-x-1/2 mb-2",
    bottom: "top-full left-1/2 -translate-x-1/2 mt-2",
    left: "right-full top-1/2 -translate-y-1/2 mr-2",
    right: "left-full top-1/2 -translate-y-1/2 ml-2",
  }[side]

  // 🔹 외곽(라인) 삼각형
  const arrowBorderTri = {
    top: "border-x-[15px] border-t-[9px] border-x-transparent border-t-line",
    bottom: "border-x-[15px] border-b-[9px] border-x-transparent border-b-line",
    left: "border-y-[15px] border-l-[9px] border-y-transparent border-l-line",
    right: "border-y-[15px] border-r-[9px] border-y-transparent border-r-line",
  }[side]

  // 🔹 내부(배경) 삼각형
  const arrowBgTri = {
    top: "border-x-8 border-t-8 border-x-transparent border-t-white",
    bottom: "border-x-8 border-b-8 border-x-transparent border-b-white",
    left: "border-y-8 border-l-8 border-y-transparent border-l-white",
    right: "border-y-8 border-r-8 border-y-transparent border-r-white",
  }[side]

  // 위치 (외곽/내부 살짝 차이)
  const arrowBorderPos = {
    top: "-bottom-[9px] left-1/2 -translate-x-1/2",
    bottom: "-top-[9px] left-1/2 -translate-x-1/2",
    left: "top-1/2 -right-[9px] -translate-y-1/2",
    right: "top-1/2 -left-[9px] -translate-y-1/2",
  }[side]

  const arrowBgPos = {
    top: "-bottom-2 left-1/2 -translate-x-1/2",
    bottom: "-top-2 left-1/2 -translate-x-1/2",
    left: "top-1/2 -right-2 -translate-y-1/2",
    right: "top-1/2 -left-2 -translate-y-1/2",
  }[side]

  return (
    <div
      ref={wrapperRef}
      className={`relative ${className ?? "inline-block"}`}
      onMouseEnter={() => setOpen(true)}
      onMouseLeave={() => setOpen(false)}
      onFocus={() => setOpen(true)}
      onBlur={() => setOpen(false)}
      tabIndex={0}
    >
      {children}
      <div
        className={`text-subtext border-line pointer-events-none absolute z-20 w-auto max-w-[250px] min-w-[70px] rounded-md border bg-white p-2 text-left text-xs font-normal break-words shadow transition-opacity ${positionClass} ${open ? "visible opacity-100" : "invisible opacity-0"} `}
        role="tooltip"
      >
        {content}
        {/* 말풍선 화살표 */}
        <span aria-hidden className={`absolute ${arrowBorderPos} h-0 w-0 ${arrowBorderTri}`} />
        <span aria-hidden className={`absolute ${arrowBgPos} h-0 w-0 ${arrowBgTri}`} />{" "}
      </div>
    </div>
  )
}

export default Tooltip
