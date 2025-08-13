import { CheckTag } from "@/components/atoms"
import type { ISubcode } from "@/types/common/ISubcode"
interface IFormCheckTag {
  title: string
  isNecessary?: boolean
  list: ISubcode[]
  selected: ISubcode[]
  onToggle: (item: ISubcode) => void
  hasError?: boolean
}

function FormCheckTag({ title, isNecessary = false, list, selected, onToggle, hasError }: IFormCheckTag) {
  const selectedIds = selected.map((item) => item.subcode)

  return (
    <div className="flex flex-col gap-1">
      <div className={` ${hasError ? "text-error" : "text-text"} text-sm font-bold`}>
        {title}
        {isNecessary && <span className="text-error"> *</span>}
      </div>
      <div className="flex flex-row flex-wrap gap-2">
        {list.map((ele) => (
          <CheckTag
            key={ele.subcode}
            tagContent={ele.subcodeName + (title === "목표" ? "에 집중할래요" : "")}
            isChecked={selectedIds.includes(ele.subcode)}
            onToggle={() => onToggle(ele)}
          />
        ))}
      </div>
    </div>
  )
}

export default FormCheckTag
