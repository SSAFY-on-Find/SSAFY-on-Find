interface IInputBox {
  text: string
  size: "s" | "m" | "l"
  variant?: "input" | "textarea"
  placeholder: string
  onChange?: (value: string) => void
  onKeyDown?: (e: React.KeyboardEvent<HTMLInputElement | HTMLTextAreaElement>) => void
  isDisabled?: boolean
}
const sizeMap = {
  s: "h-[40px]",
  m: "h-[80px]",
  l: "h-[320px]",
}

function InputBox({ text, size, variant = "input", placeholder, onChange, onKeyDown, isDisabled = false }: IInputBox) {
  const sizeClass = sizeMap[size] || sizeMap.s
  const disableOption = isDisabled ? "text-subtext hover:cursor-not-allowed" : "text-text"

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    if (!isDisabled && onChange) {
      onChange(e.target.value)
    }
  }

  return (
    <div>
      {variant === "input" ? (
        <input
          className={`${sizeClass} ${disableOption} border-subtext/30 placeholder:text-subtext w-full rounded-md border-1 py-2 pr-4 pl-2 text-sm focus:outline-none`}
          value={text}
          placeholder={placeholder}
          onChange={handleChange}
          onKeyDown={onKeyDown}
          disabled={isDisabled}
        />
      ) : (
        <textarea
          className={`${sizeClass} ${disableOption} border-subtext/30 placeholder:text-subtext w-full rounded-md border-1 py-2 pr-4 pl-2 text-sm focus:outline-none`}
          value={text}
          placeholder={placeholder}
          onChange={handleChange}
          disabled={isDisabled}
        />
      )}
    </div>
  )
}
export default InputBox
