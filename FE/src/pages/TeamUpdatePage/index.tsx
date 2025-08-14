import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"

import { Button, CheckTag, Dropdown, InputBox } from "@/components/atoms"
import { ConfirmModal } from "@/components/templates"
import { useMyTeam, useTeamDetails, useTeamWarmup, useUpdateTeam } from "@/hooks/useTeam"
import { useUserStore } from "@/stores/userStore"
import type { ITeamCreate } from "@/types/team"

import TeamCreateSkeleton from "../TeamCreatePage/organisms/TeamCreateSkeleton"

export default function TeamEditPage() {
  const navigate = useNavigate()
  const user = useUserStore()
  const teamId = user.user?.teamId
  // 수정할 팀의 기존 정보와 warm-up 데이터(트랙, 포지션)를 불러옵니다.
  const { data: myTeam, isLoading: isMyTeamLoading } = useMyTeam()
  const { data: teamWarmup, isLoading: isTeamWarmupLoading } = useTeamWarmup()

  // 팀 수정 mutation hook
  const updateTeamMutation = useUpdateTeam()

  // 모달 상태 관리
  const [modalState, setModalState] = useState({
    isOpen: false,
    title: "",
    message: "",
  })

  // 폼 데이터를 관리하는 state
  const [updateTeamData, setUpdateTeamData] = useState<ITeamCreate>({
    description: "",
    track: "",
    positions: [],
  })

  // UI 표시에 사용될 state
  const [selectedTrackCodeName, setSelectedTrackCodeName] = useState<string>("")

  // useEffect를 사용하여 서버에서 데이터를 받아오면 state를 초기화합니다.
  useEffect(() => {
    if (myTeam && teamWarmup) {
      setUpdateTeamData({
        description: myTeam.teamInfo.teamDescription,
        track: myTeam.teamInfo.track.subcode,
        positions: myTeam.teamInfo.positions.map((p) => p.positionCode),
      })
      const trackName = teamWarmup.tracks.find((t) => t.subcode === myTeam.teamInfo.track.subcode)?.subcodeName || ""
      setSelectedTrackCodeName(trackName)
    }
  }, [myTeam, teamWarmup])

  const showModal = (title: string, message: string) => {
    setModalState({
      isOpen: true,
      title,
      message,
    })
  }

  const closeModal = () => {
    setModalState({
      isOpen: false,
      title: "",
      message: "",
    })
  }

  const handleDescriptionChange = (value: string) => {
    setUpdateTeamData((prev) => ({ ...prev, description: value }))
  }

  const handleTrackChange = (subcode: string) => {
    const selectedTrack = teamWarmup?.tracks.find((track) => track.subcode === subcode)
    if (selectedTrack) {
      setSelectedTrackCodeName(selectedTrack.subcodeName)
      setUpdateTeamData((prev) => ({ ...prev, track: selectedTrack.subcode }))
    }
  }

  const handlePositionToggle = (positionCode: string) => (isChecked: boolean) => {
    let newPositions: string[]
    if (isChecked) {
      newPositions = [...updateTeamData.positions, positionCode]
    } else {
      newPositions = updateTeamData.positions.filter((p) => p !== positionCode)
    }
    setUpdateTeamData((prev) => ({ ...prev, positions: newPositions }))
  }

  const handleUpdateTeam = () => {
    // 5. teamId를 myTeam 데이터에서 가져옵니다.
    if (!teamId) {
      showModal("오류", "수정할 팀 정보를 찾을 수 없습니다.")
      return
    }

    // 유효성 검사
    if (!updateTeamData.description.trim()) {
      showModal("알림", "팀 소개를 입력해주세요")
      return
    }
    if (!updateTeamData.track) {
      showModal("알림", "프로젝트 트랙을 선택해주세요")
      return
    }
    updateTeamMutation.mutate({ teamId: teamId, updateTeamDto: updateTeamData })
  }

  // 로딩 중일 때 스켈레톤 UI를 보여줍니다.
  if (isMyTeamLoading || isTeamWarmupLoading) {
    return <TeamCreateSkeleton />
  }

  return (
    <>
      <div className="bg-background min-h-screen px-15 py-10">
        <div className="p-6">
          <h1 className="text-text mb-[10px] text-2xl font-semibold">팀 정보 수정</h1>
          <div className="text-subtext text-sm font-normal">
            <p>팀 정보를 수정할 수 있습니다.</p>
          </div>
        </div>
        <div className="border-line rounded-[8px] border-1 bg-white px-6 py-4">
          <div className="pb-8">
            <h1 className="text-text pb-4 text-lg font-semibold">팀 정보</h1>
            <p className="text-text pb-[10px] text-sm font-medium">팀 한줄 소개</p>
            <InputBox
              text={updateTeamData.description}
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
            <p className="text-subtext pb-[10px] text-sm">모집할 팀원의 포지션을 설정해주세요.</p>
            <div className="flex flex-wrap gap-1">
              {teamWarmup?.positions.map((ele) => (
                <CheckTag
                  key={ele.subcode}
                  tagContent={ele.subcodeName}
                  isChecked={updateTeamData.positions.includes(ele.subcode)}
                  onToggle={handlePositionToggle(ele.subcode)}
                />
              ))}
            </div>
          </div>
          <div className="flex justify-end gap-2">
            <div className="w-25">
              <Button size={"m"} isIcon={false} text="취소" onClick={() => navigate(-1)} variant="primary" />
            </div>
            <div className="w-25">
              <Button size={"m"} isIcon={false} text="수정하기" onClick={handleUpdateTeam} />
            </div>
          </div>
        </div>
      </div>

      <ConfirmModal
        isOpen={modalState.isOpen}
        title={modalState.title}
        message={modalState.message}
        confirmText="확인"
        onConfirm={closeModal}
        onCancel={closeModal}
        isSingleBtn={true}
      />
    </>
  )
}
