import { useNavigate } from "react-router-dom"

import { Button } from "@/components/atoms"
import { TeamCard } from "@/components/molecules"
import { TeamDetailModal } from "@/components/templates"
import { useTeamFavoriteToggle } from "@/hooks/useFavorite"
import { useTeamDetails, useTeams } from "@/hooks/useTeam"
import { useTeamStore } from "@/stores/teamStore"
import { useUserStore } from "@/stores/userStore"

import { TeamCardSkeleton } from "./organisms/TeamCardSkeleton"

export default function TeamListPage() {
  const { data: teams = [], isLoading: isTeamsLoading, isError } = useTeams()
  const { isDetailModalOpen, selectedTeamId, openDetailModal, closeDetailModal } = useTeamStore()
  const { data: selectedTeamData } = useTeamDetails(selectedTeamId || 0)
  const { toggleFavorite } = useTeamFavoriteToggle()
  const userTeamId = useUserStore((state) => state.user?.teamId)
  console.log("userTeamId", userTeamId)

  const navigate = useNavigate()

  if (isError) {
    return <div>다시 시도</div>
  }

  return (
    <div className="bg-background min-h-screen px-15 py-10 pr-20">
      <div className="mb-6 flex items-center gap-5">
        <h1 className="text-text text-2xl font-bold">팀 목록</h1>
        <div className="">
          {userTeamId ? (
            <></>
          ) : (
            <Button size={"m"} isIcon={false} text="팀생성" onClick={() => navigate("/create-team")} />
          )}
        </div>
      </div>
      <div className="grid w-full grid-cols-4 gap-5">
        {isTeamsLoading ? (
          <TeamCardSkeleton />
        ) : (
          teams.map((team) => (
            <TeamCard
              key={team.teamId}
              {...team}
              onClickFavorite={() => toggleFavorite(team.teamId)}
              onClickCard={() => openDetailModal(team.teamId)}
              variant="default"
              userTeamId={userTeamId}
            />
          ))
        )}
      </div>

      {isDetailModalOpen && selectedTeamData && selectedTeamId && (
        <TeamDetailModal
          userTeamId={userTeamId}
          isOpen={isDetailModalOpen}
          onClose={closeDetailModal}
          teamData={selectedTeamData}
          teamId={selectedTeamId}
        />
      )}

      {teams.length === 0 && (
        <div className="py-12 text-center">
          <div className="text-subtext">등록된 팀이 없습니다.</div>
        </div>
      )}
    </div>
  )
}
