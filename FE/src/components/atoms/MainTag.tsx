interface tagProps {
  tagContent: string
  fillBg?: boolean
}

function MainTag({ tagContent, fillBg = false }: tagProps) {
  const colorApply = fillBg ? "bg-main text-white" : "bg-transparent text-main"
  return (
    <>
      <div
        className={`${colorApply} border-main inline-flex h-6 items-center justify-center rounded-full border-2 px-[9px] text-xs font-bold whitespace-nowrap`}
      >
        {tagContent}
      </div>
    </>
  )
}

export default MainTag
