interface ISideMainTag {
  tagContent: string
}
function MajorTag({ tagContent }: ISideMainTag) {
  const isMajor = tagContent === "전공" || tagContent === "비전공"

  return (
    <>
      <div
        className={`${
          isMajor ? "text-main" : "text-subtext"
        } border-subtext/30 inline-block rounded-full border-1 bg-transparent px-[9px] py-[3px] text-xs font-bold whitespace-nowrap`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default MajorTag
