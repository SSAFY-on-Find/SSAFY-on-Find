import { useState } from "react"
import { Eye, Search } from "lucide-react"

import { Button, InputBox, Segmented } from "@/components/atoms"
import { useProfileCodes } from "@/hooks/useProfile"
import { useProfileStore } from "@/stores/profileStroe"
import { useUserStore } from "@/stores/userStore"

import { FormCard, FormCheckTag, FormDropdown, FormInput } from "./organisms"

export default function ProfileCreatePage() {
  const { data: codes, isLoading, error } = useProfileCodes()
  const { user } = useUserStore()
  const { position, track, techStack, goal, mbti, setCodes } = useProfileStore()
  const [activeMarkdownTab, setActiveMarkdownTab] = useState<"left" | "right">("left")

  if (isLoading) return <div>로딩 중...</div>
  if (error || !codes) return <div>코드 리스트를 불러올 수 없습니다.</div>

  return (
    <div className="bg-background flex min-h-screen flex-col gap-7 px-15 py-10">
      <div className="flex flex-col gap-2">
        <h1 className="text-text text-2xl font-bold">자기소개 작성</h1>
        <p className="text-subtext text-sm">
          팀 빌딩에 활용될 자기소개를 작성해주세요. <span className="text-error">*</span> 표시는 필수 입력 사항입니다.
        </p>
      </div>
      <FormCard title={"기본 정보"} info={"수정할 수 없는 기본 정보입니다."}>
        <FormInput title={"이름"} text={""} size={"s"} placeholder={user?.name ?? "-"} isDisabled={true} />
        <FormInput title={"학번"} text={""} size={"s"} placeholder={user?.studentId ?? "-"} isDisabled={true} />
        <FormInput title={"전공여부"} text={""} size={"s"} placeholder={user?.major ?? "-"} isDisabled={true} />
      </FormCard>
      <FormCard title={"필수 정보"} info={"팀 매칭에 필요한 필수 정보입니다."} isNecessary={true}>
        <FormDropdown
          title={"희망 포지션"}
          placeholder={"희망 포지션을 선택하세요"}
          options={codes.position.map((item) => ({
            label: item.subcodeName,
            value: item.subcode,
          }))}
          isNecessary={true}
          value={position?.subcodeName}
          onChange={(selectedSubcode) => {
            const selected = codes.position.find((item) => item.subcode === selectedSubcode) ?? null
            setCodes({ position: selected })
          }}
        />
        <FormDropdown
          title={"희망 트랙"}
          placeholder={"희망 트랙을 선택하세요"}
          options={codes.track.map((item) => ({
            label: item.subcodeName,
            value: item.subcode,
          }))}
          isNecessary={true}
          value={track?.subcodeName}
          onChange={(selectedSubcode) => {
            const selected = codes.track.find((item) => item.subcode === selectedSubcode) ?? null
            setCodes({ track: selected })
          }}
        />
        <FormCheckTag
          title={"기술 스택"}
          isNecessary={true}
          list={codes.techStack}
          selected={techStack}
          onToggle={(item) => {
            const exists = techStack.some((i) => i.subcode === item.subcode)
            const next = exists ? techStack.filter((i) => i.subcode !== item.subcode) : [...techStack, item]
            setCodes({ techStack: next })
          }}
        />
        <FormCheckTag
          title={"목표"}
          isNecessary={true}
          list={codes.goal}
          selected={goal ? [goal] : []}
          onToggle={(item) => {
            if (goal && goal.subcode === item.subcode) {
              setCodes({ goal: null })
            } else {
              setCodes({ goal: item })
            }
          }}
        />
      </FormCard>
      <FormCard title={"선택 정보"} info={"추가로 공유하고 싶은 정보를 입력해주세요."}>
        <FormInput title={"이름"} text={""} size={"s"} placeholder={user?.name ?? ""} isDisabled={true} />
      </FormCard>
      <FormCard title={"자유 형식 자기소개"} info={"마크다운 형식으로 자유롭게 자기소개를 작성해주세요."}>
        <Segmented
          leftText="편집"
          leftIcon={Search}
          rightText="미리보기"
          rightIcon={Eye}
          activeSegment={activeMarkdownTab}
          onSegmentChange={setActiveMarkdownTab}
        />
        <InputBox
          text={""}
          size={"l"}
          placeholder={"마크다운 형식으로 자유롭게 자기소개를 작성해 보세요!"}
          variant="textarea"
        />
      </FormCard>
      <Button text="저장하기" size={"m"} isIcon={false} onClick={() => {}} />
    </div>
  )
}
