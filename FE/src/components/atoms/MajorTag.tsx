interface ISideMainTag {
  tagContent: string
}
function MajorTag({ tagContent }: ISideMainTag) {
  return (
    <>
      <div
        className={`text-main border-subtext/30 inline-block rounded-full border-1 bg-transparent px-[9px] py-[3px] text-xs font-bold whitespace-nowrap`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default MajorTag
