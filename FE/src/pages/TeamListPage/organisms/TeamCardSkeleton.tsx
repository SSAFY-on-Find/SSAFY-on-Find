import Skeleton, { SkeletonTheme } from "react-loading-skeleton"

import "react-loading-skeleton/dist/skeleton.css"

export const TeamCardSkeleton = () => (
  <SkeletonTheme baseColor="#f3f4f6" highlightColor="#e5e7eb">
    {[...Array(6)].map((_, idx) => (
      <div className="flex max-w-[260px] min-w-[260px] flex-col overflow-hidden rounded-md">
        <Skeleton height={280} containerClassName="flex-1" style={{ borderRadius: "6px" }} />
      </div>
    ))}
  </SkeletonTheme>
)
