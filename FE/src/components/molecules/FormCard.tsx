interface IFormCard {
  title: string
  info: string
  isNecessary?: boolean
  children: React.ReactNode
}

function FormCard({ title, info, isNecessary = false, children }: IFormCard) {
  return (
    <div className="border-line flex w-full flex-col gap-5 rounded-lg border bg-white p-5">
      <div>
        <div className="text-text text-lg font-bold">
          {title}
          {isNecessary && <span className="text-error"> *</span>}
        </div>
        <div className="text-subtext text-sm font-normal">{info}</div>
      </div>
      {children}
    </div>
  )
}

export default FormCard
