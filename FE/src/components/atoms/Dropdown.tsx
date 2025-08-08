import { useEffect, useRef, useState } from "react"
import { ChevronDown } from "lucide-react"

interface IDropdown {
  placeholder: string
  options: { label: string; value: string }[]
  value?: string
  onChange?: (value: string) => void
}

function Dropdown({ placeholder, options, value, onChange }: IDropdown) {
  const [open, setOpen] = useState(false)
  const buttonRef = useRef<HTMLButtonElement>(null)
  const menuRef = useRef<HTMLDivElement>(null)

  // 메뉴 바깥 클릭 시 닫기
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (
        menuRef.current &&
        !menuRef.current.contains(event.target as Node) &&
        buttonRef.current &&
        !buttonRef.current.contains(event.target as Node)
      ) {
        setOpen(false)
      }
    }
    if (open) document.addEventListener("mousedown", handleClickOutside)
    return () => document.removeEventListener("mousedown", handleClickOutside)
  }, [open])

  const handleSelect = (option: string) => {
    setOpen(false)
    if (onChange) onChange(option)
  }

  return (
    <div className="relative w-full">
      <button
        type="button"
        ref={buttonRef}
        onClick={() => setOpen((prev) => !prev)}
        className="border-line text-text focus:outline-main flex w-full items-center justify-between rounded-md border bg-white px-3 py-2 text-left text-sm"
      >
        <span className={value ? "text-text" : "text-subtext"}>{value || placeholder}</span>
        <ChevronDown className={`text-line h-5 w-5 transition-transform ${open ? "rotate-180" : ""}`} />
      </button>
      {open && (
        <div
          ref={menuRef}
          className="border-line divide-line absolute left-0 z-20 mt-2 w-full divide-y rounded-md border bg-white shadow-lg"
        >
          {options.map((option) => (
            <div
              key={option.value}
              onClick={() => handleSelect(option.value)}
              className={`hover:bg-main/10 cursor-pointer px-4 py-2 text-sm ${
                value === option.value ? "bg-main/10 font-semibold" : ""
              }`}
            >
              {option.label}
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default Dropdown
