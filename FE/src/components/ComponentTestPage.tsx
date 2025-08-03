import { useState } from "react"

import { CheckTag, MainTag, MajorTag, NormalTag, PositionTag, WhiteTag } from "./atoms"
import { ConfirmModal } from "./molecules"
// API로 기술스택 70여개를 받아온다고 가정하고 상수에 담아두었어요
const TECH_STACKS = [
  { id: "tech001", name: "React" },
  { id: "tech002", name: "TypeScript" },
  { id: "tech003", name: "Node.js" },
  { id: "tech004", name: "Python" },
  { id: "tech005", name: "Java" },
  { id: "tech006", name: "Spring Boot" },
  { id: "tech007", name: "MySQL" },
  { id: "tech008", name: "AWS" },
]

export default function ComponentTestPage() {
  // 기술스택이 선택되면 2개 이상일 경우 관리하기 위하여 배열로 선언.(기술스택 ID를 저장할 예정입니다.)
  const [selectedTechStackIds, setSelectedTechStackIds] = useState<string[]>([])

  // 체크하게 되면 기술 스택 ID를
  const handleTechStackToggle = (teckStackId: string) => (isChecked: boolean) => {
    setSelectedTechStackIds((prev) => {
      if (isChecked) {
        return [...prev, teckStackId]
      }
      return prev.filter((id) => id !== teckStackId)
    })
  }

  const [ConfirmModal_teamout, setConfirmModal_teamout] = useState(false)
  const [ConfirmModal_inviteAccept, setConfirmModal_inviteAccept] = useState(false)
  const handleOut = () => {
    setConfirmModal_teamout(false)
  }
  const handleAccept = () => {
    setConfirmModal_inviteAccept(false)
  }

  return (
    <div>
      <div className="bg-amber-100">
        <h5>선택된 기술 스택 ID: {selectedTechStackIds.join(", ")}</h5>
        {TECH_STACKS.map((ele) => (
          <CheckTag
            key={ele.id}
            tagContent={ele.name}
            isChecked={selectedTechStackIds?.includes(ele.id)}
            onToggle={handleTechStackToggle(ele.id)}
          ></CheckTag>
        ))}
      </div>
      <div className="bg-background p-5">
        <NormalTag tagContent={"기본태그"} />
        <MainTag tagContent={"메인태그"}></MainTag>
        <MainTag tagContent={"메인태그"} fillBg={true}></MainTag>
        <PositionTag positionName={"프론트"}></PositionTag>
        <PositionTag positionName={"백엔드"}></PositionTag>
        <PositionTag positionName={"풀스택"}></PositionTag>
        <PositionTag positionName={"모바일"}></PositionTag>
        <PositionTag positionName={"임베디드"}></PositionTag>
        <PositionTag positionName={"AI"}></PositionTag>
        <MajorTag tagContent={"전공태그"}></MajorTag>
      </div>
      <div className="bg-black p-5">
        <WhiteTag tagContent={"하양"}></WhiteTag>
        <WhiteTag tagContent={"유령"} fillBg={true}></WhiteTag>
      </div>
      <div className="flex gap-4">
        <button
          className="rounded-md bg-red-500 px-4 py-2 text-white hover:bg-red-700"
          onClick={() => setConfirmModal_teamout(true)}
        >
          확인 모달 - 팀 탈퇴
        </button>
        <button
          className="rounded-md bg-blue-500 px-4 py-2 text-white hover:bg-blue-700"
          onClick={() => setConfirmModal_inviteAccept(true)}
        >
          확인 모달 - 초대 수락
        </button>
      </div>
      <ConfirmModal
        isOpen={ConfirmModal_teamout}
        title="팀 탈퇴"
        message={"정말 이 팀을 탈퇴하시겠습니까?"}
        onConfirm={handleOut}
        onCancel={() => setConfirmModal_teamout(false)}
        confirmText="탈퇴"
        isDestructive={true}
      />
      <ConfirmModal
        isOpen={ConfirmModal_inviteAccept}
        title="초대 수락"
        message={"정말 초대를 받으시겠습니까?"}
        onConfirm={handleAccept}
        onCancel={() => setConfirmModal_inviteAccept(false)}
        confirmText="수락"
      />
    </div>
  )
}
