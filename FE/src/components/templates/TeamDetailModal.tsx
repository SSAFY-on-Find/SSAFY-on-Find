import type { ITeamDetail } from "@/types"

import { MainTag, PositionTag, UserImg } from "../atoms"
import { TeamDetail } from "../molecules"

import Modal from "./Modal"

interface ITeamDetailModal {
  isOpen: boolean
  onClose: () => void
  teamData: ITeamDetail
}

function TeamDetailModal({ isOpen, onClose, teamData }: ITeamDetailModal) {
  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"m"}>
      <div className="p-6">
        <TeamDetail
          id={teamData.id}
          name={teamData.name}
          description={teamData.description}
          track={teamData.track}
          position={teamData.position}
          maxMembers={teamData.maxMembers}
          currentMembers={teamData.currentMembers}
          members={teamData.members}
        ></TeamDetail>
      </div>
    </Modal>
  )
}

export default TeamDetailModal
