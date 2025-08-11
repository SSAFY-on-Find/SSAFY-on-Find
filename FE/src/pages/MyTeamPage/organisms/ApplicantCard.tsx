// src/components/organisms/ApplicantCard.tsx

import type { INotificationStatusResponseDto } from "@/types/notification"

interface ApplicantCardProps {
  applicant: INotificationStatusResponseDto
}

export default function ApplicantCard({ applicant }: ApplicantCardProps) {
  return (
    <div className="border-line flex items-center justify-between rounded-lg border-1 p-4">
      <div className="flex items-center gap-4">
        {/* 프로필 이미지는 실제 데이터에 맞게 수정하세요. */}
        <div className="h-12 w-12 rounded-full bg-gray-300" />
        <div>
          <p className="font-bold">{applicant.pubNotificationTitle}</p>
          <p className="text-subtext text-sm">{applicant.pubNotificationMessage}</p>
        </div>
      </div>
    </div>
  )
}
