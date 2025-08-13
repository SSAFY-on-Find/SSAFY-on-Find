import { Dropdown } from "@/components/atoms"

interface IFormDropdown {
  title: string
  isNecessary?: boolean
  placeholder: string
  options: { label: string; value: string }[]
  value?: string
  onChange?: (value: string) => void
  hasError?: boolean
}

function FormDropdown({ title, isNecessary = false, placeholder, options, value, onChange, hasError }: IFormDropdown) {
  return (
    <div className="flex flex-col gap-1">
      <div className={` ${hasError ? "text-error" : "text-text"} text-sm font-bold`}>
        {title}
        {isNecessary && <span className="text-error"> *</span>}
      </div>
      <Dropdown placeholder={placeholder} options={options} value={value} onChange={onChange} />
    </div>
  )
}

export default FormDropdown
