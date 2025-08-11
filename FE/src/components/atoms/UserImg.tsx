import { Camera } from "lucide-react"
interface IUserImg {
  name: string | undefined
  size: "xs" | "s" | "m" | "l" | "xl"
  hasTeam?: boolean
  showTeamBadge: boolean
  isEdit?: boolean
  url?: string
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

function UserImg({ name, size, hasTeam = false, showTeamBadge, isEdit = false, url = "" }: IUserImg) {
  const sizeClass = sizeMap[size] || sizeMap.m
  const badgeClass = badgeSizeMap[size] || badgeSizeMap.m

  return (
    <div className={`relative ${sizeClass} border-line aspect-square flex-shrink-0 rounded-full border`}>
      <img
        src={url === "" ? `https://api.dicebear.com/9.x/notionists-neutral/svg?seed=${name}` : url}
        className="h-full w-full rounded-full object-cover"
        alt={`${name} 프로필 이미지`}
      />
      {showTeamBadge && (
        <span
          className={`absolute ${badgeClass} rounded-full ${hasTeam ? "bg-main" : "border-main border-2 bg-white"} `}
        />
      )}
      {isEdit && <Camera className={`absolute ${badgeClass} text-main cursor-pointer`} strokeWidth={1.3} />}
    </div>
  )
}

export default UserImg
