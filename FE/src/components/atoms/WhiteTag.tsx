interface IWhiteTag {
  tagContent: string
}
function WhiteTag({ tagContent }: IWhiteTag) {
  return (
    <>
      <div
        className={`inline-block rounded-full border-2 border-white bg-transparent px-[11px] py-[3px] text-sm font-bold text-white`}
      >
        {tagContent}
      </div>
    </>
  )
}
export default WhiteTag
