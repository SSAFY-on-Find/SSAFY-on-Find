import { Ellipsis } from "lucide-react"

interface ILoading {
  text?: string
  fullScreen?: boolean
  bg?: string
}

export default function Loading({ text = "로딩 중...", fullScreen = false, bg = "bg-background" }: ILoading) {
  const Wrapper = fullScreen
    ? "flex flex-col items-center justify-center min-h-screen"
    : "flex flex-col items-center justify-center p-4"

  return (
    <div className={`${Wrapper} ${bg}`}>
      <Ellipsis className="text-line h-20 w-20 animate-pulse" />
      {text && <p className="text-subtext text-base">{text}</p>}
    </div>
  )
}
