import { useRef, useState } from "react"

interface ITooltip {
  content: string
  children: React.ReactNode
  side?: "top" | "bottom" | "left" | "right"
}

function Tooltip({ content, children, side = "top" }: ITooltip) {
  const [open, setOpen] = useState(false)
  const wrapperRef = useRef<HTMLDivElement>(null)

  // 툴팁 위치 스타일링
  const positionClass = {
    top: "bottom-full left-1/2 -translate-x-1/2 mb-2",
    bottom: "top-full left-1/2 -translate-x-1/2 mt-2",
    left: "right-full top-1/2 -translate-y-1/2 mr-2",
    right: "left-full top-1/2 -translate-y-1/2 ml-2",
  }[side]

  return (
    <div
      ref={wrapperRef}
      className="relative inline-block"
      onMouseEnter={() => setOpen(true)}
      onMouseLeave={() => setOpen(false)}
      onFocus={() => setOpen(true)}
      onBlur={() => setOpen(false)}
      tabIndex={0}
    >
      {children}
      <div
        className={`text-subtext border-line pointer-events-none absolute z-20 w-full rounded-md border bg-white p-2 text-left text-xs font-normal shadow transition-opacity ${positionClass} ${open ? "visible opacity-100" : "invisible opacity-0"} `}
        role="tooltip"
      >
        {content}
      </div>
    </div>
  )
}

export default Tooltip
