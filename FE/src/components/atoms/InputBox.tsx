interface IInputBox {
  text: string
  size: "s" | "m" | "l"
  placeholder: string
}
const sizeMap = {
  s: "h-[40px]",
  m: "h-[80px]",
  l: "h-[320px]",
}

function InputBox({ text, size, placeholder }: IInputBox) {
  const sizeClass = sizeMap[size] || sizeMap.s
  return (
    <div>
      <textarea
        className={`${sizeClass} border-subtext/30 placeholder:text-subtext w-full rounded-md border-1 py-2 pr-4 pl-2 text-sm`}
        value={text}
        placeholder={placeholder}
      />
    </div>
  )
}
export default InputBox
