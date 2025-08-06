import Skeleton, { SkeletonTheme } from "react-loading-skeleton"

import { TeamCard } from "@/components/molecules"
import { TeamDetailModal } from "@/components/templates"
import { useTeamDetails, useTeams } from "@/hooks/useTeam"
import { useTeamStore } from "@/stores/teamStore"

import "react-loading-skeleton/dist/skeleton.css"

const TeamCardSkeleton = () => (
  <div className="flex max-w-[260px] min-w-[260px] flex-col overflow-hidden rounded-md">
    <Skeleton height={280} containerClassName="flex-1" style={{ borderRadius: "6px" }} />
  </div>
)

export default function TeamListPage() {
  const { data: teams = [], isLoading: isTeamsLoading, error, isError } = useTeams()
  const { favoriteTeams, isDetailModalOpen, selectedTeamId, toggleFavorite, openDetailModal, closeDetailModal } =
    useTeamStore()
  const {
    data: selectedTeamData,
    isLoading: isTeamDetailLoading,
    error: detailError,
  } = useTeamDetails(selectedTeamId || 0)

  if (isError) {
    return <div>다시 시도</div>
  }

  return (
    <div className="bg-background min-h-screen">
      <div className="p-6">
        <h1 className="text-text mb-6 text-2xl font-bold">팀 목록</h1>
        <div className="flex flex-wrap gap-[10px]">
          {isTeamsLoading ? (
            <SkeletonTheme baseColor="#f3f4f6" highlightColor="#e5e7eb">
              {[...Array(6)].map((_, idx) => (
                <TeamCardSkeleton key={idx} />
              ))}
            </SkeletonTheme>
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
