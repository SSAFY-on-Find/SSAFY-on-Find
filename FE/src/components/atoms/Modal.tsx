import { CircleX } from "lucide-react"

interface IModal {
  isOpen: boolean
  onClose: () => void
  size: "s" | "m" | "l"
  children: React.ReactNode
}
const sizeMap = {
  s: "max-w-sm",
  m: "max-w-lg",
  l: "max-w-xl",
}

function Modal({ isOpen, onClose, size, children }: IModal) {
  if (!isOpen) return null

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/25" onClick={onClose} />
      <div className={`${sizeMap[size]} relative w-full rounded-lg bg-white`}>
        <button className="t-4 r-4 text-main absolute top-4 right-4 z-10" onClick={onClose}>
          <CircleX className="hover:bg-main/10 h-5 w-5 rounded-full"></CircleX>
        </button>
        {children}
      </div>
    </div>
  )
}

export default Modal
