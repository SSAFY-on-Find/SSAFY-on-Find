import { CheckTag } from "@/components/atoms"

interface IFormCheckTag {
  title: string
  isNecessary?: boolean
}

function FormCheckTag({ title, isNecessary = false }: IFormCheckTag) {
  return (
    <div className="flex flex-col gap-1">
      <div className="text-text text-sm font-normal">
        {title}
        {isNecessary && <span className="text-error"> *</span>}
      </div>
      <CheckTag tagContent={""} onToggle={() => {}} />
    </div>
  )
}

export default FormCheckTag
