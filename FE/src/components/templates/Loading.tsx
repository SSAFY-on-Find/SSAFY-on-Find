import { Ellipsis } from "lucide-react"

interface ILoading {
  text?: string
  fullScreen?: boolean
}

export default function Loading({ text = "로딩 중...", fullScreen = false }: ILoading) {
  const Wrapper = fullScreen
    ? "flex flex-col items-center justify-center min-h-screen bg-background"
    : "flex flex-col items-center justify-center p-4 bg-background"

  return (
    <div className={Wrapper}>
      <Ellipsis className="text-line h-20 w-20 animate-pulse" />
      {text && <p className="text-subtext text-base">{text}</p>}
    </div>
  )
}
