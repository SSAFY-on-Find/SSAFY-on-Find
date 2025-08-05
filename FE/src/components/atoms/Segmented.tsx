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
    <div className="bg-background flex h-8 w-full rounded-md p-1">
      <button
        className={`flex flex-1 items-center justify-center gap-2 rounded-xs px-4 transition-colors ${
          activeSegment === "left" ? "text-text bg-white" : "text-subtext"
        }`}
        onClick={() => handleSegmentClick("left")}
      >
        {LeftIcon ? <LeftIcon size={16} /> : <Inbox size={16} />}
        <span className="text-sm font-medium">{leftText ? leftText : "받은 요청"}</span>
      </button>

      <button
        className={`flex flex-1 items-center justify-center gap-2 rounded-xs px-4 transition-colors ${
          activeSegment === "right" ? "text-text bg-white" : "text-subtext"
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
