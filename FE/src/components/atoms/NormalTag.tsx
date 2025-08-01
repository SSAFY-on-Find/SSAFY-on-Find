interface INormalTag {
  tagContent: string
}
function NormalTag({ tagContent }: INormalTag) {
  return (
    <>
      <div
        className={`bg-background border-subtext/30 text-text inline-block rounded-full border-2 px-[9px] py-[3px] text-xs font-bold whitespace-nowrap`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default NormalTag
