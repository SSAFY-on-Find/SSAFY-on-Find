import { useMemo } from "react"
import ReactMarkdown from "react-markdown"
import { useNavigate } from "react-router-dom"
import { FilePenLine } from "lucide-react"

import { Button, MajorTag, NormalTag, PositionTag } from "@/components/atoms"
import { StudentInfo } from "@/components/molecules"
import { useGetProfile } from "@/hooks/useProfile"
import type { ISubcode } from "@/types/common"

import "github-markdown-css/github-markdown-light.css"

export default function MyProfile() {
  const navigate = useNavigate()
  const { data, isLoading, isError, error } = useGetProfile()
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
      teamInfo: data?.teamInfo ?? null,
    }
  }, [data])

  if (isLoading) {
    return (
      <div className="bg-background flex min-h-screen flex-col gap-7 px-15 py-10">
        <div className="text-text">로딩 중입니다.</div>
      </div>
    )
  }

  if (isError) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center gap-3">
        <div className="text-text">내 프로필 정보를 불러오지 못했습니다.</div>
        <div className="text-subtext text-sm">{(error as Error)?.message}</div>
        <Button variant="outline" text="뒤로가기" onClick={() => navigate(-1)} size={"m"} isIcon={false} />
      </div>
    )
  }

  return (
    <div className="bg-background flex min-h-screen flex-col gap-7 px-15 py-10">
      <div className="w-30">
        <Button
          size={"m"}
          isIcon={true}
          Icon={FilePenLine}
          variant="outline"
          text="편집"
          onClick={() => navigate("/edit-profile")}
        />
      </div>
      <div>
        <StudentInfo
          isMyProfile={true}
          name={student?.name ?? ""}
          studentId={student?.studentId?.toString() ?? ""}
          imgUrl={profileImageUrl ?? ""}
          teamInfo={teamInfo}
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
    </div>
  )
}
