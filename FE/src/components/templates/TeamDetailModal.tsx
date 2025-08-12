import { Button } from "@/components/atoms"
import { TeamDetail } from "@/components/molecules"
import { useInvte } from "@/hooks/useInvite"
import { useUserStore } from "@/stores/userStore"
import type { ITeamDetails } from "@/types/team"

import Modal from "./Modal"

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

  const renderButtons = () => {
    if (userTeamId === null) {
      return (
        <Button
          size={"m"}
          isIcon={false}
          text="지원하기"
          variant="primary"
          onClick={() => myMateId && applyAsMate(teamId, Number(myMateId))}
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
            onClick={() => myMateId && applyAsMate(teamId, Number(myMateId))}
          />
          <Button
            size={"m"}
            isIcon={false}
            text="팀 합치기"
            variant="outline"
            onClick={() => userTeamId && mergeTeams(teamId, Number(userTeamId))}
          />
        </>
      )
    }
    return null
  }
  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"m"}>
      <div className="p-6">
        <TeamDetail {...teamData}></TeamDetail>
        <div className="mt-10 mb-4 flex gap-3 px-5">{renderButtons()}</div>
      </div>
    </Modal>
  )
}

export default TeamDetailModal
