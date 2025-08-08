import { Button } from "@/components/atoms"
import type { ITeamDetails } from "@/types/team"

import { TeamDetail } from "../molecules"

import Modal from "./Modal"

interface ITeamDetailModal {
  teamId: number
  isOpen: boolean
  onClose: () => void
  teamData: ITeamDetails
  userTeamId?: number
}

function TeamDetailModal({ teamId, isOpen, onClose, teamData, userTeamId }: ITeamDetailModal) {
  const renderButtons = () => {
    if (userTeamId === null) {
      return <Button size={"m"} isIcon={false} text="지원하기" variant="primary" onClick={function (): void {}} />
    } else if (userTeamId !== teamId) {
      return (
        <>
          <Button size={"m"} isIcon={false} text="지원하기" variant="primary" onClick={function (): void {}} />
          <Button size={"m"} isIcon={false} text="팀 합치기 제안" variant="outline" onClick={function (): void {}} />
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
