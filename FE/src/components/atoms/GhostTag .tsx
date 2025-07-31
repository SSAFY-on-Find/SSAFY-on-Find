interface IGhostTag {
  tagContent: string
}
function GhostTag({ tagContent }: IGhostTag) {
  return (
    <>
      <div
        className={`inline-block rounded-full border-2 border-white/30 bg-white/20 px-[11px] py-[3px] text-sm font-bold text-white`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default GhostTag
