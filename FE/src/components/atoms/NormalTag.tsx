interface INormalTag {
  tagContent: string
}
function NormalTag({ tagContent }: INormalTag) {
  return (
    <>
      <div
        className={`bg-background border-subtext/30 inline-block rounded-full border-2 px-[11px] py-[3px] text-sm font-bold text-black`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default NormalTag
