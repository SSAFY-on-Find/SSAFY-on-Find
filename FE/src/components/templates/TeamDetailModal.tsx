import { Button } from "@/components/atoms"
import type { ITeamDetails } from "@/types/team"

import { TeamDetail } from "../molecules"

import Modal from "./Modal"

interface ITeamDetailModal {
  isOpen: boolean
  onClose: () => void
  teamData: ITeamDetails
}

function TeamDetailModal({ isOpen, onClose, teamData }: ITeamDetailModal) {
  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"m"}>
      <div className="p-6">
        <TeamDetail {...teamData}></TeamDetail>
        <div className="mt-10 mb-4 flex gap-3 px-5">
          <Button size={"m"} isIcon={false} text="지원하기" variant="primary" onClick={function (): void {}} />
          <Button size={"m"} isIcon={false} text="팀 합치기 제안" variant="outline" onClick={function (): void {}} />
        </div>
      </div>
    </Modal>
  )
}

export default TeamDetailModal
