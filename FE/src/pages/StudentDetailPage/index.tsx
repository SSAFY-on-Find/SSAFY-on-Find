import ReactMarkdown from "react-markdown"
import { useNavigate } from "react-router-dom"
import { FilePenLine } from "lucide-react"

import { Button, MajorTag, NormalTag, PositionTag } from "@/components/atoms"
import { StudentInfo } from "@/components/molecules"

import "github-markdown-css/github-markdown-light.css"

export default function StudentDetailPage() {
  const navigate = useNavigate()

  return (
    <div className="bg-background flex min-h-screen flex-col gap-7 px-15 py-10">
      {/* 마이 페이지 일때만 */}
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
        <StudentInfo name={"김싸피"} studentId={"1300001"} teamInfo={null} />
      </div>
      <div className="border-line flex w-full flex-col gap-7 rounded-lg border bg-white p-10">
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">기본정보</div>
          <div className="flex flex-wrap gap-2">
            <MajorTag tagContent={"전공"} />
            <PositionTag positionName={"프론트"} />
            <NormalTag tagContent={"웹기술"} />
            <NormalTag tagContent={"포트폴리오 우선"} />
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">기술스택</div>
          <div className="flex flex-wrap gap-2">
            <NormalTag tagContent={"React"} />
            <NormalTag tagContent={"Spring Boot"} />
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">강점 & MBTI</div>
          <div className="flex flex-wrap gap-2">
            <NormalTag tagContent={"발표잘함"} />
            <NormalTag tagContent={"ENTP"} />
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">첨부파일</div>
          <div className="text-subtext text-xs font-light">
            {/* {portfolioFile?.name ?? portfolio?.originalFileName} */}
            파일 없음
          </div>
        </div>
      </div>
      <div className="markdown-body border-line items-center justify-start rounded-lg border bg-white p-10">
        <ReactMarkdown>{"# description \n ```code``` "}</ReactMarkdown>
      </div>
    </div>
  )
}
