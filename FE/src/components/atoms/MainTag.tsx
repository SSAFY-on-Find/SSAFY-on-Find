interface tagProps {
  tagContent: string
  fillBg?: boolean
  size?: "sm" | "lg"
}

function MainTag({ tagContent, fillBg = false, size = "sm" }: tagProps) {
  const colorApply = fillBg ? "bg-main text-white" : "bg-transparent text-main"
  const sizeApply = size === "lg" ? "px-[12px] py-[4px] text-base font-bold" : "px-[9px] py-[3px] text-xs font-bold"

  return (
    <>
      <div
        className={`${colorApply} ${sizeApply} border-main inline-block rounded-full border-2 text-center whitespace-nowrap`}
      >
        {tagContent}
      </div>
    </>
  )
}

export default MainTag
