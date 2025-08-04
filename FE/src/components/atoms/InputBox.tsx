interface IInputBox {
  text: string
  size: "s" | "m" | "l"
  placeholder: string
  onChange: (value: string) => void
  isDisabled?: boolean
}
const sizeMap = {
  s: "h-[40px]",
  m: "h-[80px]",
  l: "h-[320px]",
}

function InputBox({ text, size, placeholder, onChange, isDisabled = false }: IInputBox) {
  const sizeClass = sizeMap[size] || sizeMap.s
  const disableOption = isDisabled ? "text-subtext hover:cursor-not-allowed" : "text-text"
  return (
    <div>
      <textarea
        className={`${sizeClass} ${disableOption} border-subtext/30 placeholder:text-subtext w-full rounded-md border-1 py-2 pr-4 pl-2 text-sm focus:outline-none`}
        value={text}
        placeholder={placeholder}
        onChange={(e) => onChange(e.target.value)}
        disabled={isDisabled}
      />
    </div>
  )
}
export default InputBox
