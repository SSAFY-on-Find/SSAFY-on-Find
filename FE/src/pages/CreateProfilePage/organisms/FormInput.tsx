import { InputBox } from "@/components/atoms"

interface IFormInput {
  title: string
  isNecessary?: boolean
  text: string
  size: "s" | "m" | "l"
  variant?: "input" | "textarea"
  placeholder: string
  onChange?: (value: string) => void
  onKeyDown?: (e: React.KeyboardEvent<HTMLInputElement | HTMLTextAreaElement>) => void
  isDisabled?: boolean
}

function FormInput({
  title,
  isNecessary = false,
  text,
  size,
  variant = "input",
  placeholder,
  onChange,
  onKeyDown,
  isDisabled = false,
}: IFormInput) {
  return (
    <div className="flex flex-col gap-1">
      <div className="text-text text-sm font-normal">
        {title}
        {isNecessary && <span className="text-error"> *</span>}
      </div>
      <InputBox
        text={text}
        size={size}
        placeholder={placeholder}
        isDisabled={isDisabled}
        variant={variant}
        onChange={onChange}
        onKeyDown={onKeyDown}
      />
    </div>
  )
}

export default FormInput
