import { Button } from "@/components/atoms"
import { TeamCard } from "@/components/molecules"
import { TeamDetailModal } from "@/components/templates"
import { useTeamFavoriteToggle } from "@/hooks/useFavorite"
import { useTeamDetails, useTeams } from "@/hooks/useTeam"
import { useTeamStore } from "@/stores/teamStore"

import { TeamCardSkeleton } from "./organisms/TeamCardSkeleton"

import "react-loading-skeleton/dist/skeleton.css"

export default function TeamListPage() {
  const { data: teams = [], isLoading: isTeamsLoading, error, isError } = useTeams()
  const { isDetailModalOpen, selectedTeamId, openDetailModal, closeDetailModal } = useTeamStore()
  const {
    data: selectedTeamData,
    isLoading: isTeamDetailLoading,
    error: detailError,
  } = useTeamDetails(selectedTeamId || 0)
  const { toggleFavorite, isLoading: teamFavoriteToggleLoading } = useTeamFavoriteToggle()

  if (isError) {
    return <div>다시 시도</div>
  }

  return (
    <div className="bg-background min-h-screen">
      <div className="p-6">
        <div className="mb-6 flex items-center gap-5">
          <h1 className="text-text text-2xl font-bold">팀 목록</h1>
          <div className="">
            <Button size={"m"} isIcon={false} text="팀생성" onClick={() => ""} />
          </div>
        </div>
        <div className="flex flex-wrap gap-[10px]">
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
              />
            ))
          )}
        </div>

        {isDetailModalOpen && selectedTeamData && (
          <TeamDetailModal isOpen={isDetailModalOpen} onClose={closeDetailModal} teamData={selectedTeamData} />
        )}

        {teams.length === 0 && (
          <div className="py-12 text-center">
            <div className="text-subtext">등록된 팀이 없습니다.</div>
          </div>
        )}
      </div>
    </div>
  )
}
