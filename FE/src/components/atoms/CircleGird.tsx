interface ICircleGrid {
  memberCount: number
  variant?: "dashboard" | "detail"
}

const sizeMap = {
  detail: "h-6 w-6",
  dashboard: "h-5 w-5",
}

function CircleGrid({ memberCount, variant = "detail" }: ICircleGrid) {
  const totalSlots = 6
  const circles = [...Array(memberCount).fill("bg-main"), ...Array(totalSlots - memberCount).fill("bg-line")]

  return (
    <div className="flex flex-col items-center gap-2">
      <div className="flex gap-2">
        {circles.slice(0, 3).map((color, idx) => (
          <div key={idx} className={`${sizeMap[variant]} rounded-full ${color}`} />
        ))}
      </div>
      <div className="flex gap-2">
        {circles.slice(3, 6).map((color, idx) => (
          <div key={idx} className={`${sizeMap[variant]} rounded-full ${color}`} />
        ))}
      </div>
    </div>
  )
}

export default CircleGrid
