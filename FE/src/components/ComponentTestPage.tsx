import { useState } from "react"

import { CheckTag, InputBox, MainTag, MajorTag, NormalTag, PositionTag, SearchBar, WhiteTag } from "./atoms"
import { ConfirmModal, StudentSearchModal, TeamDetailModal } from "./templates"

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
// 팀 세부 정보 모달 테스트용 MockData 입니다
const sampleTeamData = {
  id: "team001",
  name: "팀 001",
  description:
    "팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명팀한줄설명",
  track: "웹 디자인",
  position: ["백엔드", "인프라"],
  maxMembers: 6,
  currentMembers: 4,
  members: [
    {
      id: "member001",
      name: "김태호",
      position: "프론트",
      major: "전공",
    },
    {
      id: "member001",
      name: "김태호",
      position: "프론트",
      major: "비전공",
    },
    {
      id: "member001",
      name: "김태호",
      position: "프론트",
      major: "전공",
    },
    {
      id: "member002",
      name: "이영희",
      position: "백엔드",
      major: "비전공",
    },
    {
      id: "member003",
      name: "박민수",
      position: "풀스택",
      major: "비전공",
    },
    {
      id: "member004",
      name: "최지은",
      position: "프론트",
      major: "전공",
    },
  ],
}
// 교육생 검색 모달 테스트용 MockData 입니다
const students = [
  { id: "std001", name: "김싸피", major: "비전공", position: "임베디드", hasTeam: true },
  { id: "std002", name: "이싸", major: "전공", position: "풀스텍", hasTeam: false },
  { id: "std003", name: "박싸피", major: "비전공", position: "백엔드", hasTeam: true },
  { id: "std004", name: "조싸피", major: "전공", position: "모바일", hasTeam: true },
  { id: "std005", name: "이싸피", major: "비전공", position: "임베디드", hasTeam: false },
  { id: "std006", name: "김박싸피", major: "비전공", position: "임베디드", hasTeam: true },
  { id: "std007", name: "이싸", major: "전공", position: "풀스텍", hasTeam: false },
  { id: "std008", name: "박싸피", major: "비전공", position: "백엔드", hasTeam: true },
  { id: "std009", name: "조싸피", major: "전공", position: "모바일", hasTeam: true },
  { id: "std010", name: "이싸피", major: "비전공", position: "임베디드", hasTeam: false },
  { id: "std011", name: "김싸피", major: "비전공", position: "임베디드", hasTeam: true },
  { id: "std012", name: "이싸", major: "전공", position: "풀스텍", hasTeam: false },
  { id: "std013", name: "박싸피", major: "비전공", position: "백엔드", hasTeam: true },
  { id: "std014", name: "조싸피", major: "전공", position: "모바일", hasTeam: true },
  { id: "std015", name: "이싸피", major: "비전공", position: "임베디드", hasTeam: false },
]
export default function ComponentTestPage() {
  // 1. CheckTag 사용법
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

  // 2. SearchBar 사용법
  const [searchQuery, setSearchQuery] = useState("")
  // searchBar에서 매개변수로 사용할 검색함수입니다.
  const handleSearch = (query: string) => {
    setSearchQuery(query)
  }

  // 3. InputBox 사용법
  const [inputBoxValueSmall, setInputBoxValueSmall] = useState("")
  const [inputBoxValueMedium, setInputBoxValueMedium] = useState("")
  const [inputBoxValueLarge, setInputBoxValueLarge] = useState("")

  // 4. 모달 사용법
  const [ConfirmModal_teamout, setConfirmModal_teamout] = useState(false)
  const [ConfirmModal_inviteAccept, setConfirmModal_inviteAccept] = useState(false)
  const [teamDetailModal, setTeamDetailModal] = useState(false)
  const [selectedTeam, setSelectedTeam] = useState(sampleTeamData)
  const [studentSearchModal, setStudentSearchModal] = useState(false)

  const handleOut = () => {
    setConfirmModal_teamout(false)
  }
  const handleAccept = () => {
    setConfirmModal_inviteAccept(false)
  }
  const handleTeamSelect = (teamData: typeof sampleTeamData) => {
    setSelectedTeam(teamData)
    setTeamDetailModal(true)
  }

  return (
    <div className="flex flex-col gap-5 p-5">
      <div>
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
      <div className="bg-background">
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
      <div className="bg-main p-5">
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
        <button
          className="rounded-md bg-green-500 px-4 py-2 text-white hover:bg-green-700"
          onClick={() => handleTeamSelect(sampleTeamData)}
        >
          팀 상세 정보
        </button>

        <button
          className="rounded-md bg-amber-600 px-4 py-2 text-white hover:bg-amber-800"
          onClick={() => setStudentSearchModal(true)}
        >
          교육생 찾기 모달
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
      <TeamDetailModal isOpen={teamDetailModal} onClose={() => setTeamDetailModal(false)} teamData={selectedTeam} />
      <StudentSearchModal
        isOpen={studentSearchModal}
        onClose={() => setStudentSearchModal(false)}
        students={students}
      />
      <div className="">
        <p>small - 채팅입력이나 기본 입력</p>
        <InputBox
          text={inputBoxValueSmall}
          size={"s"}
          placeholder={"메시지를 입력하세요..."}
          onChange={setInputBoxValueSmall}
        ></InputBox>
        <p>small - isDisabled=true</p>
        <InputBox
          text={"사전에 설정된 이름입니다."}
          size={"s"}
          placeholder={"메시지를 입력하세요..."}
          onChange={setInputBoxValueSmall}
          isDisabled={true}
        ></InputBox>
        <p>medium - 팀소개</p>
        <InputBox
          text={inputBoxValueMedium}
          size={"m"}
          placeholder={"팀을 소개하는 한줄 설명을 작성해주세요"}
          onChange={setInputBoxValueMedium}
        ></InputBox>
        <p>large - 자기소개 마크다운</p>
        <InputBox
          text={inputBoxValueLarge}
          size={"l"}
          placeholder={"마크다운 형식으로 자유롭게 자기소개를 작성해 보세요!"}
          onChange={setInputBoxValueLarge}
        ></InputBox>
        <div>
          <p>현재 검색어: {searchQuery}</p>
          <SearchBar onSearch={handleSearch} />
        </div>
      </div>
    </div>
  )
}
