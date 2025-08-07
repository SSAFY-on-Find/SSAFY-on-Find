import Skeleton, { SkeletonTheme } from "react-loading-skeleton"

import "react-loading-skeleton/dist/skeleton.css"

export default function TeamCreateSkeleton() {
  return (
    <SkeletonTheme baseColor="#f3f4f6" highlightColor="#e5e7eb">
      <div className="bg-background min-h-screen p-8">
        <div className="p-6">
          <Skeleton height={32} width={200} className="mb-[10px]" />
          <div className="space-y-1">
            <Skeleton height={16} width="100%" />
            <Skeleton height={16} width="90%" />
            <Skeleton height={16} width="95%" />
          </div>
        </div>

        <div className="border-line rounded-[8px] border-1 bg-white px-6 py-4">
          <div className="pb-8">
            <Skeleton height={24} width={100} className="pb-4" />
            <Skeleton height={16} width={120} className="pb-[10px]" />
            <Skeleton height={80} className="mb-4" />

            <Skeleton height={16} width={120} className="pb-[10px]" />
            <Skeleton height={40} />
          </div>

          <div className="pb-6">
            <Skeleton height={24} width={120} className="pb-4" />
            <Skeleton height={16} width="80%" className="pb-[10px]" />

            <div className="flex flex-wrap gap-1">
              {Array.from({ length: 7 }).map((_, index) => (
                <Skeleton key={index} height={28} width={80} className="rounded-full" />
              ))}
            </div>
          </div>

          <div className="flex justify-end">
            <Skeleton height={40} width={100} />
          </div>
        </div>
      </div>
    </SkeletonTheme>
  )
}
