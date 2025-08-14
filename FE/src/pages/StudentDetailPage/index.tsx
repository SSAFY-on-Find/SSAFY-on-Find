import { useMemo } from "react"
import ReactMarkdown from "react-markdown"
import { useNavigate, useParams } from "react-router-dom"
import { toast } from "react-toastify"
import { isAxiosError } from "axios"
import { Send, UserRoundPlus } from "lucide-react"

import { Button, MajorTag, NormalTag, PositionTag } from "@/components/atoms"
import { StudentInfo } from "@/components/molecules"
import { TeamDetailModal } from "@/components/templates"
import Loading from "@/components/templates/Loading"
import { useCreateDirectChatRoom } from "@/hooks/useDM"
import { useInvte } from "@/hooks/useInvite"
import { useStudentInfo } from "@/hooks/useStudent"
import { useTeamDetails } from "@/hooks/useTeam"
import { useTeamStore } from "@/stores/teamStore"
import { useChatViewStore } from "@/stores/useChatViewStore"
import { useUserStore } from "@/stores/userStore"
import type { ISubcode } from "@/types/common"

import "github-markdown-css/github-markdown-light.css"

export default function StudentDetailPage() {
  const navigate = useNavigate()
  const { studentId } = useParams<{ studentId: string }>()
  const parsedId = Number(studentId)
  const { data, isLoading, isError, error } = useStudentInfo(parsedId)
  const { isDetailModalOpen, selectedTeamId, closeDetailModal } = useTeamStore()
  const { data: selectedTeamData } = useTeamDetails(selectedTeamId || 0)
  const userTeamId = useUserStore((state) => state.user?.teamId)
  const openChat = useChatViewStore((state) => state.openChat)
  const { mutate: createChat } = useCreateDirectChatRoom()

  const {
    student,
    position,
    track,
    goal,
    mbti,
    techStack,
    strength,
    description,
    profileImageUrl,
    portfolio,
    isFavorite,
    teamInfo,
  } = useMemo(() => {
    return {
      student: data?.student ?? null,
      position: data?.position ?? null,
      track: data?.track ?? null,
      goal: data?.goal ?? null,
      mbti: data?.mbti ?? null,
      techStack: (data?.techStack ?? []) as ISubcode[],
      strength: (data?.strength ?? []) as string[],
      description: data?.description ?? null,
      profileImageUrl: data?.profileImageUrl ?? null,
      portfolio: data?.portfolio ?? null,
      isFavorite: data?.isFavorite ?? false,
      teamInfo: data?.teamInfo ?? null,
    }
  }, [data, parsedId])

  const targetUserTeamId = teamInfo?.teamId

  if (!studentId || Number.isNaN(parsedId) || studentId.length !== 7) {
    return (
      <div className="text-text bg-background flex min-h-screen items-center justify-center">
        잘못된 학생 ID 입니다.
      </div>
    )
  }

  if (isLoading) {
    return <Loading fullScreen />
  }

  if (isError) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center gap-3">
        <div className="text-text">학생 정보를 불러오지 못했습니다.</div>
        <div className="text-subtext text-sm">{(error as Error)?.message}</div>
        <Button variant="outline" text="뒤로가기" onClick={() => navigate(-1)} size={"m"} isIcon={false} />
      </div>
    )
  }
  const handleStartNewChat = (targetStudentId: number) => {
    createChat(
      { targetStudentId },
      {
        onSuccess: (roomId) => {
          openChat({ roomId, roomType: "direct" })
        },
        onError: (error) => {},
      }
    )
  }

  return (
    <div className="bg-background flex min-h-screen flex-col gap-7 px-15 py-10">
      <div className="flex flex-row gap-5">
        <div className="w-30">
          <Button
            size={"m"}
            isIcon={true}
            Icon={Send}
            variant="outline"
            text="채팅하기"
            onClick={() => handleStartNewChat(parsedId)}
          />
        </div>
      </div>
      <div>
        <StudentInfo
          isFavorite={isFavorite}
          name={student?.name ?? ""}
          studentId={student?.studentId?.toString() ?? ""}
          imgUrl={profileImageUrl ?? ""}
          teamInfo={teamInfo}
          variant="detail"
        />
      </div>
      <div className="border-line flex w-full flex-col gap-7 rounded-lg border bg-white p-10">
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">기본정보</div>
          <div className="flex flex-wrap gap-2">
            {student?.major && <MajorTag tagContent={student.major} />}
            {position?.subcodeName && <PositionTag positionName={position.subcodeName} />}
            {track?.subcodeName && <NormalTag tagContent={track.subcodeName} />}
            {goal?.subcodeName && <NormalTag tagContent={`${goal.subcodeName} 우선`} />}
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">기술스택</div>
          <div className="flex flex-wrap gap-2">
            {techStack.length > 0 ? (
              techStack.map((stack: ISubcode) => <NormalTag key={stack.subcode} tagContent={stack.subcodeName} />)
            ) : (
              <span className="text-subtext text-xs">등록된 기술스택이 없습니다.</span>
            )}
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">강점 & MBTI</div>
          <div className="flex flex-wrap gap-2">
            {strength.length > 0 ? strength.map((s) => <NormalTag key={s} tagContent={s} />) : <></>}
            {mbti && <NormalTag key={mbti.subcode} tagContent={mbti.subcodeName} />}
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">첨부파일</div>
          <div className="text-subtext text-xs font-light">
            {portfolio !== null ? portfolio.originalFileName : "파일 없음"}
          </div>
        </div>
      </div>
      {description && (
        <div className="markdown-body border-line items-center justify-start rounded-lg border bg-white p-10">
          <ReactMarkdown>{description}</ReactMarkdown>
        </div>
      )}
      {isDetailModalOpen && selectedTeamData && selectedTeamId && (
        <TeamDetailModal
          userTeamId={userTeamId}
          isOpen={isDetailModalOpen}
          onClose={closeDetailModal}
          teamData={selectedTeamData}
          teamId={selectedTeamId}
        />
      )}
    </div>
  )
}
