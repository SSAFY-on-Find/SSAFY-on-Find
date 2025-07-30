type tagProps = {
  tagContent: string | undefined
}

export const MainTag = ({ tagContent }: tagProps) => {
  return (
    <>
      <div className="bg-main text-white px-[11px] py-[4px]  rounded-full text-sm font-medium inline-block">
        {tagContent}
      </div>
    </>
  )
}
