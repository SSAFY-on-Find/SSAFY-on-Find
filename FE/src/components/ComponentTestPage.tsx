import { useState } from "react"
import { AlarmClock } from "lucide-react"

import { Button, CheckTag, InputBox, MainTag, MajorTag, NormalTag, PositionTag, SearchBar, WhiteTag } from "./atoms"

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

  return (
    <div className="flex flex-col gap-5 p-5">
      <div className="flex flex-row gap-2">
        {" "}
        {/* 태그 small */}
        <Button
          text={"기본"}
          isIcon={false}
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"s"}
        />
        <Button
          text={"아이콘"}
          isIcon={true}
          Icon={AlarmClock}
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"s"}
        />
        <Button
          text={"테두리"}
          isIcon={false}
          variant="outline"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"s"}
        />
        <Button
          text={"경고"}
          isIcon={false}
          variant="danger"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"s"}
        />
        <Button
          text={"텍스트"}
          isIcon={false}
          variant="text"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"s"}
        />
        <Button
          isIcon={true}
          Icon={AlarmClock}
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"s"}
        />
      </div>
      <div className="flex flex-row gap-2">
        {" "}
        {/* 태그 medium */}
        <Button
          text={"기본"}
          isIcon={false}
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"m"}
        />
        <Button
          text={"아이콘"}
          isIcon={true}
          Icon={AlarmClock}
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"m"}
        />
        <Button
          text={"테두리"}
          isIcon={false}
          variant="outline"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"m"}
        />
        <Button
          text={"경고"}
          isIcon={false}
          variant="danger"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"m"}
        />
        <Button
          text={"텍스트"}
          isIcon={false}
          variant="text"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"m"}
        />
        <Button
          isIcon={true}
          Icon={AlarmClock}
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"m"}
        />
      </div>
      <div className="flex flex-row gap-2">
        {" "}
        {/* 태그 large */}
        <Button
          text={"아이콘"}
          isIcon={true}
          Icon={AlarmClock}
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"l"}
        />
        <Button
          text={"테두리"}
          isIcon={true}
          Icon={AlarmClock}
          variant="outline"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"l"}
        />
      </div>
      <div className="bg-black p-2">
        <Button
          text={"흰색"}
          isIcon={false}
          variant="white"
          onClick={function (): void {
            throw new Error("Function not implemented.")
          }}
          size={"m"}
        />
      </div>
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
