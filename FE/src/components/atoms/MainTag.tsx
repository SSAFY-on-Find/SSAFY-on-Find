interface tagProps {
  tagContent: string
  fillBg?: boolean
}

function MainTag({ tagContent, fillBg = false }: tagProps) {
  const colorApply = fillBg ? "bg-main text-white" : "bg-transparent text-main"
  return (
    <>
      <div
        className={`${colorApply} border-main inline-block rounded-full border-2 px-[9px] py-[3px] text-xs font-bold`}
      >
        {tagContent}
      </div>
    </>
  )
}

export default MainTag
