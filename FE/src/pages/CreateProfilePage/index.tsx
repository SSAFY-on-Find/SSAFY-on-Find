import { useState } from "react"
import { Eye, Search } from "lucide-react"

import { Button, InputBox, Segmented } from "@/components/atoms"
import { useUserStore } from "@/stores/userStore"

import { FormCard, FormCheckTag, FormDropdown, FormInput } from "./organisms"

export default function ProfileCreatePage() {
  const { user } = useUserStore()
  const [activeMarkdownTab, setActiveMarkdownTab] = useState<"left" | "right">("left")

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
          options={["프론트", "백엔드", "풀스텍", "모바일", "임베디드", "인프라", "AI"]}
          isNecessary={true}
        />
        <FormDropdown
          title={"희망 트랙"}
          placeholder={"희망 트랙을 선택하세요"}
          options={["웹기술", "웹디자인", "모바일", "임베디드"]}
          isNecessary={true}
        />
        <FormCheckTag title={"기술 스택"} isNecessary={true} />
        <FormCheckTag title={"목표"} />
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
