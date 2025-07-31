type tagProps = {
  tagContent?: string
  fillBg?: boolean
}

function MainTag({ tagContent, fillBg = false }: tagProps) {
  if (!tagContent) return null
  const colorApply = fillBg ? "bg-main text-white" : "bg-transparent text-main"
  return (
    <>
      <div
        className={`${colorApply} border-main inline-block rounded-full border-2 px-[11px] py-[3px] text-sm font-bold`}
      >
        {tagContent}
      </div>
    </>
  )
}

export default MainTag
