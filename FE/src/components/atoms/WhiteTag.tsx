interface IWhiteTag {
  tagContent: string
  fillBg?: boolean
}
function WhiteTag({ tagContent, fillBg = false }: IWhiteTag) {
  const colorApply = fillBg ? "bg-white/20 text-white border-white/30" : "bg-transparent text-white border-white"
  return (
    <>
      <div
        className={`${colorApply} inline-block rounded-full border-2 bg-transparent px-[11px] py-[3px] text-xs font-bold text-white`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default WhiteTag
