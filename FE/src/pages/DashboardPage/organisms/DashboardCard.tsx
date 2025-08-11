interface IDashboardCard {
  title: string
  children: React.ReactNode
}

export function DashboardCard({ title, children }: IDashboardCard) {
  return (
    <div className="border-line w-full gap-5 rounded-lg border bg-white px-8 py-7">
      <div className="text-text text-lg font-semibold">{title}</div>
      {children}
    </div>
  )
}

export default DashboardCard
