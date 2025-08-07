import { useState } from "react"

import { Button, CheckTag, Dropdown, InputBox } from "@/components/atoms"
import { useCreateTeam, useTeamWarmup } from "@/hooks/useTeam"
import type { ITeamCreate } from "@/types/team"

export default function TeamCreatePage() {
  const createTeamMutation = useCreateTeam()
  const { data: teamWarmup, isLoading: isTeamWarmupLoading } = useTeamWarmup()

  const [createTeamData, setCreateTeamData] = useState<ITeamCreate>({
    description: "",
    track: "",
    positions: [],
  })

  const [selectedTrackCode, setSelectedTrackCode] = useState<string>("")
  const [selectedPositions, setSelectedPositions] = useState<string[]>([])

  const handleDescriptionChange = (value: string) => {
    setCreateTeamData((prev) => ({
      ...prev,
      description: value,
    }))
  }

  const handleTrackChange = (selectedTrackName: string) => {
    const selectedTrack = teamWarmup?.tracks.find((track) => track.subcodeName === selectedTrackName)
    if (selectedTrack) {
      setSelectedTrackCode(selectedTrack.subcode)
      setCreateTeamData((prev) => ({
        ...prev,
        track: selectedTrack.subcode,
      }))
    }
  }

  const handlePositionToggle = (positionCode: string) => (isChecked: boolean) => {
    let newSelectedPositions: string[]

    if (isChecked) {
      newSelectedPositions = [...selectedPositions, positionCode]
    } else {
      newSelectedPositions = selectedPositions.filter((p) => p !== positionCode)
    }

    setSelectedPositions(newSelectedPositions)
    setCreateTeamData((prev) => ({
      ...prev,
      positions: newSelectedPositions,
    }))
  }

  const handleCreateTeam = () => {
    if (!createTeamData.description.trim()) {
      alert("팀 소개를 입력해주세요")
      return
    }
    if (!createTeamData.track) {
      alert("프로젝트 트랙을 선택해주세요")
      return
    }
    if (createTeamData.positions.length === 0) {
      alert("모집 포지션을 하나 이상 선택해주세요")
      return
    }

    createTeamMutation.mutate(createTeamData)
  }
  // 로딩 상태 처리 추가
  if (isTeamWarmupLoading) {
    return (
      <div className="bg-background flex min-h-screen items-center justify-center">
        <div className="text-text">로딩 중...</div>
      </div>
    )
  }

  // 기존 return 문 앞에 추가
  return (
    <div className="bg-background min-h-screen">
      <div className="border-text border-1 p-1">
        <h1 className="text-text text-2xl font-bold">팀 목록</h1>
        <p>팀 규칙</p>
        <p>* 팀원은 6인 1팀이 원칙입니다.</p>
        <p>* 팀원은 비전공 2명, 전공 2명이 반드시 포함되어야 합니다.</p>
        <p>* 특정 전공에 인원이 몰린 팀은 해체 후 리빌딩될 수 있습니다.</p>
      </div>
      <div className="border-text border-1 p-1">
        <div className="border-text border-1 p-1">
          <h1>팀 정보</h1>
          <p>팀 한줄 소개</p>
          <InputBox
            text={createTeamData.description}
            size={"m"}
            placeholder={"팀을 소개해주세요"}
            onChange={handleDescriptionChange}
            variant={"textarea"}
          />
          <p>프로젝트 트랙</p>
          {teamWarmup && (
            <Dropdown
              placeholder={"트랙을 선택해주세요"}
              value={
                selectedTrackCode
                  ? teamWarmup?.tracks.find((t) => t.subcode === selectedTrackCode)?.subcodeName || ""
                  : ""
              }
              options={teamWarmup?.tracks.map((track) => track.subcodeName)}
              onChange={handleTrackChange}
            />
          )}
        </div>
        <div className="border-text border-1 p-1">
          <h1>모집 포지션</h1>
          <p>모집할 팀원의 포지션을 설정해주세요. 나중에 수정할 수 있습니다!</p>
          {teamWarmup?.positions.map((ele) => (
            <CheckTag
              key={ele.subcode}
              tagContent={ele.subcodeName}
              isChecked={selectedPositions.includes(ele.subcode)}
              onToggle={handlePositionToggle(ele.subcode)}
            />
          ))}
        </div>
      </div>
      <Button size={"m"} isIcon={false} text="팀 생성하기" onClick={handleCreateTeam} />
    </div>
  )
}
