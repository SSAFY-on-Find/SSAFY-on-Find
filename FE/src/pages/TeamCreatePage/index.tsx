import { useState } from "react"
import { toast } from "react-toastify"

import { Button, CheckTag, Dropdown, InputBox } from "@/components/atoms"
import { useCreateTeam, useTeamWarmup } from "@/hooks/useTeam"
import type { ITeamCreate } from "@/types/team"

import TeamCreateSkeleton from "./organisms/TeamCreateSkeleton"

export default function TeamCreatePage() {
  const createTeamMutation = useCreateTeam()
  const { data: teamWarmup, isLoading: isTeamWarmupLoading } = useTeamWarmup()

  const [createTeamData, setCreateTeamData] = useState<ITeamCreate>({
    description: "",
    track: "",
    positions: [],
  })

  const [selectedTrackCode, setSelectedTrackCode] = useState<string>("")
  const [selectedTrackCodeName, setSelectedTrackCodeName] = useState<string>("")
  const [selectedPositions, setSelectedPositions] = useState<string[]>([])

  const handleDescriptionChange = (value: string) => {
    setCreateTeamData((prev) => ({
      ...prev,
      description: value,
    }))
  }

  const handleTrackChange = (subcode: string) => {
    console.log("handleTrackChange - selectedTrackName", subcode)
    const selectedTrack = teamWarmup?.tracks.find((track) => track.subcode === subcode)
    console.log("selectedTrack", selectedTrack)
    if (selectedTrack) {
      setSelectedTrackCode(selectedTrack.subcode)
      setSelectedTrackCodeName(selectedTrack.subcodeName)
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
      toast.error("팀 소개를 입력해주세요")
      return
    }
    if (!createTeamData.track) {
      toast.error("프로젝트 트랙을 선택해주세요")
      return
    }

    createTeamMutation.mutate(createTeamData)
  }
  if (isTeamWarmupLoading) {
    return <TeamCreateSkeleton />
  }

  return (
    <div className="bg-background min-h-screen px-15 py-10">
      <div className="p-6">
        <h1 className="text-text mb-[10px] text-2xl font-semibold">새로운 팀 생성</h1>
        <div className="text-subtext text-sm font-normal">
          <p>* 팀원은 6인 1팀이 원칙입니다.</p>
          <p>* 팀원은 비전공 2명, 전공 2명이 반드시 포함되어야 합니다.</p>
          <p>* 특정 전공에 인원이 몰린 팀은 해체 후 리빌딩될 수 있습니다.</p>
        </div>
      </div>
      <div className="border-line rounded-[8px] border-1 bg-white px-6 py-4">
        <div className="pb-8">
          <h1 className="text-text pb-4 text-lg font-semibold">팀 정보</h1>
          <p className="text-text pb-[10px] text-sm font-medium">팀 한줄 소개</p>
          <InputBox
            text={createTeamData.description}
            size={"m"}
            placeholder={"팀을 소개하는 한줄 설명을 작성해주세요"}
            onChange={handleDescriptionChange}
            variant={"textarea"}
          />
          <p className="text-text pb-[10px] text-sm font-medium">프로젝트 트랙</p>
          {teamWarmup && (
            <Dropdown
              placeholder={"트랙을 선택해주세요"}
              value={selectedTrackCodeName}
              options={
                teamWarmup?.tracks.map((track) => ({
                  label: track.subcodeName,
                  value: track.subcode,
                })) || []
              }
              onChange={handleTrackChange}
            />
          )}
        </div>
        <div className="pb-6">
          <h1 className="text-text pb-4 text-lg font-semibold">모집 포지션</h1>
          <p className="text-subtext pb-[10px] text-sm">
            모집할 팀원의 포지션을 설정해주세요. 나중에 수정할 수 있습니다!
          </p>
          <div className="flex flex-wrap gap-1">
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
        <div className="flex justify-end">
          <div className="w-25">
            <Button size={"m"} isIcon={false} text="팀 생성하기" onClick={handleCreateTeam} />
          </div>
        </div>
      </div>
    </div>
  )
}
