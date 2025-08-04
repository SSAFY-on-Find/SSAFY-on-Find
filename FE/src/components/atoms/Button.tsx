type ButtonVariant = "primary" | "outline" | "danger" | "text" | "white"

const variantStyles: Record<ButtonVariant, string> = {
  primary: "bg-main text-background",
  outline: "border border-main text-main bg-white",
  danger: "bg-white border border-error text-error",
  text: "bg-transparent text-text border border-line",
  white: "bg-transparent text-white border border-white",
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
  l: "text-xl font-semibold",
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
