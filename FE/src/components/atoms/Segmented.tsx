import React, { useState } from "react"
import { Inbox, Send } from "lucide-react"

interface ISegmented {
  leftText?: string
  leftIcon?: React.ElementType
  rightText?: string
  rightIcon?: React.ElementType
  activeSegment?: "left" | "right"
  onSegmentChange: (activeSegment: "left" | "right") => void
}
function Segmented({
  leftText,
  leftIcon: LeftIcon,
  rightText,
  rightIcon: RightIcon,
  activeSegment = "left",
  onSegmentChange,
}: ISegmented) {
  const handleSegmentClick = (segment: "left" | "right") => {
    onSegmentChange(segment)
  }
  return (
    <div className="bg-background relative flex h-10 w-full rounded-lg p-1">
      <div
        className={`absolute top-1 bottom-1 w-1/2 rounded-md bg-white shadow-sm transition-transform duration-300 ease-out ${
          activeSegment === "right" ? "translate-x-full" : "translate-x-0"
        }`}
      />

      <button
        className={`relative z-10 flex flex-1 items-center justify-center gap-2 rounded-md px-4 transition-all duration-300 ${
          activeSegment === "left" ? "text-text scale-105" : "text-subtext hover:scale-102"
        }`}
        onClick={() => handleSegmentClick("left")}
      >
        {LeftIcon ? <LeftIcon size={16} /> : <Inbox size={16} />}
        <span className="text-sm font-medium">{leftText ? leftText : "받은 요청"}</span>
      </button>

      <button
        className={`relative z-10 flex flex-1 items-center justify-center gap-2 rounded-md px-4 transition-all duration-300 ${
          activeSegment === "right" ? "text-text scale-105" : "text-subtext hover:scale-102"
        }`}
        onClick={() => handleSegmentClick("right")}
      >
        {RightIcon ? <RightIcon size={16} /> : <Send size={16} />}
        <span className="text-sm font-medium">{rightText ? rightText : "보낸 요청"}</span>
      </button>
    </div>
  )
}
export default Segmented
