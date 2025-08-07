import { Dropdown } from "@/components/atoms"

interface IFormDropdown {
  title: string
  isNecessary?: boolean
  placeholder: string
  options: string[]
  value?: string
  onChange?: (value: string) => void
}

function FormDropdown({ title, isNecessary = false, placeholder, options, value, onChange }: IFormDropdown) {
  return (
    <div className="flex flex-col gap-1">
      <div className="text-text text-sm font-normal">
        {title}
        {isNecessary && <span className="text-error"> *</span>}
      </div>
      <Dropdown placeholder={placeholder} options={options} />
    </div>
  )
}

export default FormDropdown
