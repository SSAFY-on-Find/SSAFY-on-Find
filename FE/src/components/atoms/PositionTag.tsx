interface IPositionTag {
  positionName: string
}
const getPositionColor = (positionName: string) => {
  switch (positionName) {
    case "프론트":
      return "text-frontend"
    case "백엔드":
      return "text-backend"
    case "인프라":
      return "text-infra"
    case "모바일":
      return "text-mobile"
    case "임베디드":
      return "text-embedded"
    case "풀스텍":
      return "text-fullstack"
    case "AI":
      return "text-ai"
    default:
      return "text-main"
  }
}
function PositionTag({ positionName }: IPositionTag) {
  const textColor = getPositionColor(positionName)
  return (
    <>
      <div
        className={` ${textColor} border-subtext/30 inline-block rounded-full border-2 px-[9px] py-[3px] text-xs font-bold whitespace-nowrap`}
      >
        {positionName}
      </div>
    </>
  )
}
export default PositionTag
