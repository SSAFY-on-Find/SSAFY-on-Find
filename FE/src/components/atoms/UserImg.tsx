interface IUserImg {
  name: string
  size: "xs" | "s" | "m" | "l" | "xl"
  hasTeam?: boolean
  showTeamBadge: boolean
}

const sizeMap = {
  xs: "w-[25px]",
  s: "w-[30px]",
  m: "w-[40px]",
  l: "w-[80px]",
  xl: "w-[130px]",
}

const badgeSizeMap = {
  xs: "w-[8px] h-[8px] right-0 bottom-0",
  s: "w-[10px] h-[10px] right-0 bottom-0",
  m: "w-[12px] h-[12px] right-0 bottom-0",
  l: "w-[22px] h-[22px] -right-[1px] -bottom-[1px]",
  xl: "w-[32px] h-[32px] -right-[2px] -bottom-[2px]",
}

function UserImg({ name, size, hasTeam = false, showTeamBadge }: IUserImg) {
  const sizeClass = sizeMap[size] || sizeMap.m
  const badgeClass = badgeSizeMap[size] || badgeSizeMap.m

  return (
    // <div className={`${sizeClass} aspect-square border border-line rounded-full overflow-hidden`}>
    <div className={`relative ${sizeClass} border-line aspect-square flex-shrink-0 rounded-full border`}>
      <img
        src={`https://api.dicebear.com/9.x/notionists-neutral/svg?seed=${name}`}
        className="h-full w-full rounded-full object-cover"
        alt={`${name} 프로필 이미지`}
      />
      {showTeamBadge && (
        <span
          className={`absolute ${badgeClass} rounded-full ${hasTeam ? "bg-main" : "border-main border-2 bg-white"} `}
        />
      )}
    </div>
  )
}

export default UserImg
