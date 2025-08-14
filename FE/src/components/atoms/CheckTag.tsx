import { Check } from "lucide-react"

interface ICheckTag {
  tagContent: string
  isChecked?: boolean
  onToggle: (isChecked: boolean) => void
}
function CheckCircle({ isChecked = false }) {
  const checkOnOff = isChecked ? "bg-main text-white border-main" : "bg-background text-background border-subtext/30 "
  return (
    <>
      <div className={`${checkOnOff} flex rounded-full border-1 stroke-2 p-0.5`}>
        <Check strokeWidth={4} size={8} />
      </div>
    </>
  )
}
function CheckTag({ tagContent, isChecked = false, onToggle }: ICheckTag) {
  const handleClick = () => {
    onToggle(!isChecked)
  }
  return (
    <>
      <div
        onClick={handleClick}
        className={`bg-background border-subtext/30 text-text inline-flex cursor-pointer items-center gap-1 rounded-full border-1 px-[11px] py-[3px] text-xs font-bold whitespace-nowrap`}
      >
        <CheckCircle isChecked={isChecked} />
        {tagContent}
      </div>
    </>
  )
}
export default CheckTag
