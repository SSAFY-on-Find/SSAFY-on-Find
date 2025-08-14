import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { isAxiosError } from "axios"

import { Button } from "@/components/atoms"
import { TeamDetail } from "@/components/molecules"
import { useInvte } from "@/hooks/useInvite"
import { useTeamStore } from "@/stores/teamStore"
import { useUserStore } from "@/stores/userStore"
import type { ITeamDetails } from "@/types/team"

import Modal from "./Modal"

function getErrorMessage(err: unknown, fallback: string) {
  if (isAxiosError(err)) {
    return err.response?.data?.data?.message ?? err.message ?? fallback
  }
  if (err instanceof Error) return err.message ?? fallback
  return fallback
}

interface ITeamDetailModal {
  teamId: number
  isOpen: boolean
  onClose: () => void
  teamData: ITeamDetails
  userTeamId?: number | null
}

function TeamDetailModal({ teamId, isOpen, onClose, teamData, userTeamId }: ITeamDetailModal) {
  const { applyAsMate, mergeTeams } = useInvte(userTeamId ?? null)
  const myMateId = useUserStore((s) => s.user?.studentId)
  const navigate = useNavigate()
  const { closeDetailModal } = useTeamStore()
  const renderButtons = () => {
    if (userTeamId === null) {
      return (
        <Button
          size={"m"}
          isIcon={false}
          text="지원하기"
          variant="primary"
          onClick={() => {
            if (!myMateId) return
            toast.promise(applyAsMate(teamId, Number(myMateId)), {
              success: "팀에 지원을 보냈습니다!",
              error: { render: ({ data }) => getErrorMessage(data, "지원 전송에 실패했습니다.") },
            })
          }}
        />
      )
    } else if (userTeamId !== teamId) {
      return (
        <>
          <Button
            size={"m"}
            isIcon={false}
            text="지원하기"
            variant="primary"
            onClick={() => {
              if (!myMateId) return
              toast.promise(applyAsMate(teamId, Number(myMateId)), {
                success: "팀에 지원을 보냈습니다!",
                error: { render: ({ data }) => getErrorMessage(data, "지원 전송에 실패했습니다.") },
              })
            }}
          />
          <Button
            size={"m"}
            isIcon={false}
            text="팀 합치기"
            variant="outline"
            onClick={() => {
              if (!userTeamId) return
              toast.promise(mergeTeams(teamId, Number(userTeamId)), {
                success: "팀 합치기 제안을 보냈습니다!",
                error: { render: ({ data }) => getErrorMessage(data, "팀 합치기 요청 실패했습니다.") },
              })
            }}
          />
        </>
      )
    }
    return (
      <Button
        size={"m"}
        isIcon={false}
        text="내 팀 바로가기"
        variant="primary"
        onClick={() => {
          navigate("/myteam")
          onClose()
          closeDetailModal()
        }}
      />
    )
  }
  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"m"}>
      <div className="p-6">
        <TeamDetail {...teamData} onClose={onClose}></TeamDetail>
        <div className="mt-10 mb-4 flex gap-3 px-5">{renderButtons()}</div>
      </div>
    </Modal>
  )
}

export default TeamDetailModal
