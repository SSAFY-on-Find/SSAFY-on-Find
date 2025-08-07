type ButtonVariant = "primary" | "outline" | "danger" | "text" | "white-full" | "white-line"

const variantStyles: Record<ButtonVariant, string> = {
  primary: "bg-main text-background hover:bg-main/90",
  outline: "border border-main text-main bg-transparent hover:bg-main/10",
  danger: "bg-white border border-error text-error hover:bg-error/10",
  text: "bg-transparent text-text border border-line hover:bg-subtext/10",
  "white-full": "border border-main text-main bg-white hover:text-white hover:bg-white/20",
  "white-line": "bg-transparent text-white border border-white hover:bg-white/10",
}

interface IButton {
  text?: string
  variant?: ButtonVariant
  size: "s" | "m" | "l"
  isIcon: boolean
  Icon?: React.ElementType
  onClick: () => void
}

const sizeMap = {
  s: "text-xs",
  m: "text-sm",
  l: "text-lg font-semibold",
}

const iconSizeMap = {
  s: "w-3.5 h-3.5",
  m: "w-4 h-4",
  l: "w-6 h-6",
}

function Button({ text, variant = "primary", size, isIcon = false, Icon, onClick }: IButton) {
  const sizeClass = sizeMap[size] || sizeMap.m
  const iconSizeClass = iconSizeMap[size] || iconSizeMap.m

  return (
    <button
      type="button"
      className={`flex w-full cursor-pointer items-center justify-center gap-2 rounded-md px-3 py-1.5 transition-colors ${variantStyles[variant]} ${sizeClass}`}
      onClick={onClick}
    >
      {isIcon && Icon && <Icon className={iconSizeClass} />}
      {text && <span>{text}</span>}
    </button>
  )
}

export default Button
