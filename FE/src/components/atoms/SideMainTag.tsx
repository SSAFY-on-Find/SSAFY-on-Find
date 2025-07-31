interface ISideMainTag {
  tagContent: string
}
function SideMainTag({ tagContent }: ISideMainTag) {
  return (
    <>
      <div
        className={`text-main border-subtext/30 inline-block rounded-full border-2 bg-transparent px-[11px] py-[3px] text-sm font-bold`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default SideMainTag
