import { Check } from "lucide-react"

interface ICheckTag {
  tagContent: string
  fillBg?: boolean
}
function CheckCircle({ fillBg = false }) {
  const checkOnOff = fillBg ? "bg-main text-white border-main" : "bg-background text-background border-subtext/30 "
  return (
    <>
      <div className={`${checkOnOff} flex rounded-full border-2 stroke-2 p-0.5`}>
        <Check strokeWidth={4} size={8} />
      </div>
    </>
  )
}
function CheckTag({ tagContent, fillBg = false }: ICheckTag) {
  return (
    <>
      <div
        className={`bg-background border-subtext/30 text-text inline-flex items-center gap-2 rounded-full border-2 px-[11px] py-[3px] text-xs font-bold whitespace-nowrap`}
      >
        <CheckCircle fillBg={fillBg} />
        {tagContent}
      </div>
    </>
  )
}
export default CheckTag
