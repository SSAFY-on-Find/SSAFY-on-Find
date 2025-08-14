import { useEffect, useRef, useState } from "react"
import ReactMarkdown from "react-markdown"
import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { useQueryClient } from "@tanstack/react-query"
import { CirclePlus, Eye, Search, X } from "lucide-react"

import { Button, CheckTag, InputBox, Segmented, UserImg } from "@/components/atoms"
import { FormCard, FormCheckTag, FormDropdown, FormInput } from "@/components/molecules"
import Loading from "@/components/templates/Loading"
import { useProfileCodes } from "@/hooks/useProfile"
import { useCreateProfile } from "@/hooks/useProfile"
import { useProfileStore } from "@/stores/profileStore"
import { useUserStore } from "@/stores/userStore"
import type { IStudentSignin } from "@/types/student"

import "github-markdown-css/github-markdown-light.css"
interface TechStackItem {
  subcode: string
  subcodeName: string
}

interface CategorizedTechStack {
  frontend: TechStackItem[]
  backend: TechStackItem[]
  language: TechStackItem[]
  database: TechStackItem[]
  mobile: TechStackItem[]
  infrastructure: TechStackItem[]
  embedded: TechStackItem[]
}
const mbtiPairs = [
  ["I", "E"],
  ["N", "S"],
  ["T", "F"],
  ["P", "J"],
]
const PROFILE_MAX = 1 * 1024 * 1024
const PORTFOLIO_MAX = 50 * 1024 * 1024

export default function ProfileCreatePage() {
  const { data: codes, isLoading: isCodesLoading, error: codesError } = useProfileCodes()
  const { user } = useUserStore()
  const {
    position,
    track,
    techStack,
    goal,
    strength,
    portfolio,
    portfolioFile,
    profileImageUrl,
    profileImageFile,
    description,
    setCodes,
  } = useProfileStore()
  const [activeMarkdownTab, setActiveMarkdownTab] = useState<"left" | "right">("left")
  const [strengthInput, setStrengthInput] = useState("")
  const [isEditingMbti, setIsEditingMbti] = useState(false)
  const [mbtiSelected, setMbtiSelected] = useState(["I", "N", "T", "P"])
  const profileImgInputRef = useRef<HTMLInputElement>(null)
  const portfolioInputRef = useRef<HTMLInputElement>(null)
  const { mutate: createProfile, isSuccess, error: createError } = useCreateProfile()
  const profileStore = useProfileStore()
  const navigate = useNavigate()
  const qc = useQueryClient()
  const markProfileCreated = useUserStore((s) => s.markProfileCreated)

  // 에러 표시해주는 상태 만들기
  const [errors, setErrors] = useState<{ [key: string]: boolean }>({})
  const fieldRefs = {
    position: useRef<HTMLDivElement>(null),
    track: useRef<HTMLDivElement>(null),
    goal: useRef<HTMLDivElement>(null),
    techStack: useRef<HTMLDivElement>(null),
  }
  const parseTechStackByCategory = (techStackData: TechStackItem[]): CategorizedTechStack => {
    const categories: CategorizedTechStack = {
      frontend: techStackData.filter((item) => item.subcode.startsWith("FE_")),
      backend: techStackData.filter((item) => item.subcode.startsWith("BE_")),
      language: techStackData.filter((item) => item.subcode.startsWith("LN_")),
      database: techStackData.filter((item) => item.subcode.startsWith("DB_")),
      mobile: techStackData.filter((item) => item.subcode.startsWith("MO_")),
      infrastructure: techStackData.filter((item) => item.subcode.startsWith("IN_")),
      embedded: techStackData.filter((item) => item.subcode.startsWith("EM_")),
    }
    return categories
  }

  const categorizedTechStack: CategorizedTechStack | null = codes ? parseTechStackByCategory(codes.techStack) : null

  useEffect(() => {
    if (isSuccess) {
      // 1) React Query 캐시 갱신 (가드가 이 값을 봄)
      qc.setQueryData<IStudentSignin>(["user-auth"], (prev) => (prev ? { ...prev, isCreatedStudentInfo: true } : prev))

      // 2) zustand도 갱신
      markProfileCreated()

      // 3) UX
      toast.success("자기소개가 성공적으로 저장되었습니다!")
      navigate("/") // 마지막에 이동
    }
  }, [isSuccess, navigate])

  useEffect(() => {
    if (createError) {
      toast.error("자기소개 저장 중 오류가 발생했습니다.")
    }
  }, [createError])

  const handleProfileImgChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]

    if (!file) return
    if (file.size > PROFILE_MAX) {
      toast.warn(`이미지 크기는 최대 1MB까지 업로드할 수 있습니다. (현재 ${(file.size / 1024 / 1024).toFixed(2)}MB)`)
      e.target.value = ""
      return
    }

    setCodes({ profileImageFile: file })
  }

  const handlePortfolioChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]

    if (!file) return
    if (file.size > PORTFOLIO_MAX) {
      toast.warn(`파일 크기는 최대 50MB까지 업로드할 수 있습니다. (현재 ${(file.size / 1024 / 1024).toFixed(2)}MB)`)
      e.target.value = ""
      return
    }

    setCodes({ portfolioFile: file })
  }

  const handleSave = () => {
    const newErrors: { [key: string]: boolean } = {}

    if (!position) {
      newErrors.position = true
      fieldRefs.position.current?.scrollIntoView({ behavior: "smooth", block: "center" })
      setErrors(newErrors)

      toast.error("희망 포지션을 선택해주세요.")
      return
    }
    if (!track) {
      newErrors.track = true
      fieldRefs.track.current?.scrollIntoView({ behavior: "smooth", block: "center" })
      toast.warn("희망 트랙을 선택해주세요.")
      setErrors(newErrors)
      return
    }
    if (!goal) {
      newErrors.goal = true
      fieldRefs.goal.current?.scrollIntoView({ behavior: "smooth", block: "center" })
      toast.warn("목표를 선택해주세요.")
      setErrors(newErrors)
      return
    }
    if (techStack.length === 0) {
      newErrors.techStack = true
      fieldRefs.techStack.current?.scrollIntoView({ behavior: "smooth", block: "center" })
      toast.warn("기술 스택을 선택해주세요.")
      setErrors(newErrors)
      return
    }
    setErrors({})
    createProfile(profileStore)
  }

  function setMbtiToStore(mbtiArr: string[]) {
    if (!codes) return
    const mbtiString = mbtiArr.join("")
    const selectedMbtiObj = codes.mbti.find((item) => item.subcodeName === mbtiString) ?? null
    setCodes({ mbti: selectedMbtiObj })
  }

  const handleToggle = (idx: number, value: string) => {
    const next = [...mbtiSelected]
    next[idx] = value
    setMbtiSelected(next)
    setMbtiToStore(next)
  }

  const handleReset = () => {
    setIsEditingMbti(false)
    setCodes({ mbti: null })
  }

  if (isCodesLoading) return <Loading />
  if (codesError || !codes) return <div>코드 리스트를 불러올 수 없습니다.</div>

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
        <div ref={fieldRefs.position}>
          <FormDropdown
            title={"희망 포지션"}
            placeholder={"희망 포지션을 선택하세요"}
            options={codes.position.map((item) => ({
              label: item.subcodeName,
              value: item.subcode,
            }))}
            hasError={errors.position}
            isNecessary={true}
            value={position?.subcodeName}
            onChange={(selectedSubcode) => {
              const selected = codes.position.find((item) => item.subcode === selectedSubcode) ?? null
              setCodes({ position: selected })
            }}
          />
        </div>
        <div ref={fieldRefs.track}>
          <FormDropdown
            title={"희망 트랙"}
            placeholder={"희망 트랙을 선택하세요"}
            options={codes.track.map((item) => ({
              label: item.subcodeName,
              value: item.subcode,
            }))}
            hasError={errors.track}
            isNecessary={true}
            value={track?.subcodeName}
            onChange={(selectedSubcode) => {
              const selected = codes.track.find((item) => item.subcode === selectedSubcode) ?? null
              setCodes({ track: selected })
            }}
          />
        </div>
        <div ref={fieldRefs.goal}>
          <FormCheckTag
            title={"목표"}
            isNecessary={true}
            list={codes.goal}
            hasError={errors.goal}
            selected={goal ? [goal] : []}
            onToggle={(item) => {
              if (goal && goal.subcode === item.subcode) {
                setCodes({ goal: null })
              } else {
                setCodes({ goal: item })
              }
            }}
          />
        </div>
        <div ref={fieldRefs.techStack}>
          <div className="flex flex-col gap-1">
            <div className="flex items-center gap-1">
              <div className="text-text text-sm font-bold">기술 스택</div>
              <span className="text-error">*</span>
            </div>
            {errors.techStack && <div className="text-error text-xs">기술 스택을 선택해주세요.</div>}

            {categorizedTechStack && (
              <div className="flex flex-col gap-4">
                {/* 프론트엔드 */}
                {categorizedTechStack.frontend.length > 0 && (
                  <div>
                    <hr className="text-line pb-4" />
                    <h4 className="text-text mb-2 text-xs font-medium">프론트엔드</h4>
                    <div className="flex flex-wrap gap-2 pr-40">
                      {categorizedTechStack.frontend.map((item) => (
                        <CheckTag
                          key={item.subcode}
                          tagContent={item.subcodeName}
                          isChecked={techStack.some((selected) => selected.subcode === item.subcode)}
                          onToggle={() => {
                            const exists = techStack.some((i) => i.subcode === item.subcode)
                            const next = exists
                              ? techStack.filter((i) => i.subcode !== item.subcode)
                              : [...techStack, item]
                            setCodes({ techStack: next })
                          }}
                        />
                      ))}
                    </div>
                  </div>
                )}

                {/* 백엔드 */}
                {categorizedTechStack.backend.length > 0 && (
                  <div>
                    <hr className="text-line pb-4" />
                    <h4 className="text-text mb-2 text-xs font-medium">백엔드</h4>
                    <div className="flex flex-wrap gap-2 pr-40">
                      {categorizedTechStack.backend.map((item) => (
                        <CheckTag
                          key={item.subcode}
                          tagContent={item.subcodeName}
                          isChecked={techStack.some((selected) => selected.subcode === item.subcode)}
                          onToggle={() => {
                            const exists = techStack.some((i) => i.subcode === item.subcode)
                            const next = exists
                              ? techStack.filter((i) => i.subcode !== item.subcode)
                              : [...techStack, item]
                            setCodes({ techStack: next })
                          }}
                        />
                      ))}
                    </div>
                  </div>
                )}

                {/* 프로그래밍 언어 */}
                {categorizedTechStack.language.length > 0 && (
                  <div>
                    <hr className="text-line pb-4" />
                    <h4 className="text-text mb-2 text-xs font-medium">프로그래밍 언어</h4>
                    <div className="flex flex-wrap gap-2 pr-40">
                      {categorizedTechStack.language.map((item) => (
                        <CheckTag
                          key={item.subcode}
                          tagContent={item.subcodeName}
                          isChecked={techStack.some((selected) => selected.subcode === item.subcode)}
                          onToggle={() => {
                            const exists = techStack.some((i) => i.subcode === item.subcode)
                            const next = exists
                              ? techStack.filter((i) => i.subcode !== item.subcode)
                              : [...techStack, item]
                            setCodes({ techStack: next })
                          }}
                        />
                      ))}
                    </div>
                  </div>
                )}

                {/* 데이터베이스 */}
                {categorizedTechStack.database.length > 0 && (
                  <div>
                    <hr className="text-line pb-4" />
                    <h4 className="text-text mb-2 text-xs font-medium">데이터베이스</h4>
                    <div className="flex flex-wrap gap-2 pr-40">
                      {categorizedTechStack.database.map((item) => (
                        <CheckTag
                          key={item.subcode}
                          tagContent={item.subcodeName}
                          isChecked={techStack.some((selected) => selected.subcode === item.subcode)}
                          onToggle={() => {
                            const exists = techStack.some((i) => i.subcode === item.subcode)
                            const next = exists
                              ? techStack.filter((i) => i.subcode !== item.subcode)
                              : [...techStack, item]
                            setCodes({ techStack: next })
                          }}
                        />
                      ))}
                    </div>
                  </div>
                )}

                {/* 모바일 */}
                {categorizedTechStack.mobile.length > 0 && (
                  <div>
                    <hr className="text-line pb-4" />
                    <h4 className="text-text mb-2 text-xs font-medium">모바일</h4>
                    <div className="flex flex-wrap gap-2 pr-40">
                      {categorizedTechStack.mobile.map((item) => (
                        <CheckTag
                          key={item.subcode}
                          tagContent={item.subcodeName}
                          isChecked={techStack.some((selected) => selected.subcode === item.subcode)}
                          onToggle={() => {
                            const exists = techStack.some((i) => i.subcode === item.subcode)
                            const next = exists
                              ? techStack.filter((i) => i.subcode !== item.subcode)
                              : [...techStack, item]
                            setCodes({ techStack: next })
                          }}
                        />
                      ))}
                    </div>
                  </div>
                )}

                {/* 인프라 */}
                {categorizedTechStack.infrastructure.length > 0 && (
                  <div>
                    <hr className="text-line pb-4" />
                    <h4 className="text-text mb-2 text-xs font-medium">인프라</h4>
                    <div className="flex flex-wrap gap-2 pr-40">
                      {categorizedTechStack.infrastructure.map((item) => (
                        <CheckTag
                          key={item.subcode}
                          tagContent={item.subcodeName}
                          isChecked={techStack.some((selected) => selected.subcode === item.subcode)}
                          onToggle={() => {
                            const exists = techStack.some((i) => i.subcode === item.subcode)
                            const next = exists
                              ? techStack.filter((i) => i.subcode !== item.subcode)
                              : [...techStack, item]
                            setCodes({ techStack: next })
                          }}
                        />
                      ))}
                    </div>
                  </div>
                )}

                {/* 임베디드 */}
                {categorizedTechStack.embedded.length > 0 && (
                  <div>
                    <hr className="text-line pb-4" />
                    <h4 className="text-text mb-2 text-xs font-medium">임베디드</h4>
                    <div className="flex flex-wrap gap-2 pr-40">
                      {categorizedTechStack.embedded.map((item) => (
                        <CheckTag
                          key={item.subcode}
                          tagContent={item.subcodeName}
                          isChecked={techStack.some((selected) => selected.subcode === item.subcode)}
                          onToggle={() => {
                            const exists = techStack.some((i) => i.subcode === item.subcode)
                            const next = exists
                              ? techStack.filter((i) => i.subcode !== item.subcode)
                              : [...techStack, item]
                            setCodes({ techStack: next })
                          }}
                        />
                      ))}
                    </div>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </FormCard>
      <FormCard title={"선택 정보"} info={"추가로 공유하고 싶은 정보를 입력해주세요."}>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">프로필 이미지</div>
          <div className="p-3" onClick={() => profileImgInputRef.current?.click()}>
            <input
              type="file"
              accept=".png,.jpg,.jpeg"
              ref={profileImgInputRef}
              style={{ display: "none" }}
              onChange={handleProfileImgChange}
            />
            <UserImg
              name={user?.name}
              size={"xl"}
              showTeamBadge={false}
              isEdit={true}
              url={profileImageFile ? URL.createObjectURL(profileImageFile) : profileImageUrl}
            />
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">강점</div>
          <InputBox
            text={strengthInput}
            size="s"
            placeholder="강점을 키워드로 입력해주세요 (*키워드 당 공백제외 최대 5자, 총 3개까지 설정 가능합니다 ex: 발표잘함, 팀장가능 등)"
            onChange={setStrengthInput}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                const value = strengthInput.trim()
                if (value && !strength.includes(value) && value.replace(/\s/g, "").length <= 5 && strength.length < 3) {
                  setCodes({ strength: [...strength, value] })
                  setStrengthInput("")
                } else if (!value) {
                  toast.warn("키워드를 입력해주세요!")
                } else if (strength.includes(value)) {
                  toast.warn("이미 등록된 키워드입니다!")
                } else if (value.replace(/\s/g, "").length > 5) {
                  toast.warn("키워드는 최대 5글자까지 가능합니다!")
                } else if (strength.length >= 3) {
                  toast.warn("키워드는 총 3개까지 설정 가능합니다!")
                }
                e.preventDefault()
              }
            }}
          />
          <div className="mt-2 flex flex-wrap gap-2">
            {strength.map((ele) => (
              <CheckTag
                key={ele}
                tagContent={ele}
                isChecked={true}
                onToggle={() => {
                  setCodes({ strength: strength.filter((v) => v !== ele) })
                }}
              />
            ))}
          </div>
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">MBTI</div>
          {!isEditingMbti ? (
            <div className="w-30">
              <Button
                text="추가하기"
                size={"m"}
                variant="outline"
                isIcon={true}
                Icon={CirclePlus}
                onClick={() => {
                  setIsEditingMbti(true)
                  setMbtiToStore(mbtiSelected)
                }}
              />
            </div>
          ) : (
            <div className="flex items-center gap-3">
              {mbtiPairs.map(([a, b], idx) => (
                <div key={idx} className="flex gap-0.5 rounded-full bg-[#F3F4F6] px-2 py-1">
                  <div key={idx} className="flex gap-0.5">
                    {[a, b].map((type) => (
                      <button
                        key={type}
                        onClick={() => handleToggle(idx, type)}
                        className={`aspect-square rounded-full border-0 px-2 py-1 text-xs font-bold transition-colors duration-300 ease-in-out ${
                          mbtiSelected[idx] === type ? "bg-main text-white" : "text-text"
                        }`}
                        style={{ minWidth: 28 }}
                      >
                        {type}
                      </button>
                    ))}
                  </div>
                </div>
              ))}
              <button className="ml-2" onClick={handleReset} type="button" aria-label="MBTI 선택 닫기">
                <X className="text-subtext hover:bg-main/10 h-5 w-5 cursor-pointer rounded-md" />
              </button>
            </div>
          )}
        </div>
        <div className="flex flex-col gap-1">
          <div className="text-text text-sm font-normal">파일 첨부</div>
          <div className="w-30" onClick={() => portfolioInputRef.current?.click()}>
            <input
              type="file"
              accept=".png,.pptx,.jpg,.pdf, .docx"
              ref={portfolioInputRef}
              style={{ display: "none" }}
              onChange={handlePortfolioChange}
            />
            <Button
              text={portfolio ? "변경하기" : "추가하기"}
              isIcon={true}
              Icon={CirclePlus}
              variant={"outline"}
              size={"m"}
              onClick={() => {}}
            />
            {(portfolioFile || portfolio) && (
              <div className="text-subtext mt-2 ml-2 text-xs font-light">
                {portfolioFile?.name ?? portfolio?.originalFileName}
              </div>
            )}
          </div>
        </div>
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
        {activeMarkdownTab === "left" ? (
          <InputBox
            text={description}
            size="l"
            placeholder="마크다운 형식으로 자유롭게 자기소개를 작성해 보세요!"
            variant="textarea"
            onChange={(value) => setCodes({ description: value })}
          />
        ) : (
          <div className="markdown-body border-line min-h-[320px] rounded-md border bg-white p-4">
            <ReactMarkdown>{description}</ReactMarkdown>
          </div>
        )}
      </FormCard>
      <Button
        text="등록하기"
        size={"m"}
        isIcon={false}
        onClick={() => {
          handleSave()
        }}
      />
    </div>
  )
}
