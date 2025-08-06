import { TeamCard } from "@/components/molecules"
import { useTeams } from "@/hooks/useTeam"
import { useTeamStore } from "@/stores/teamStore"

export default function TeamListPage() {
  const { data: teams = [], isLoading, error, isError } = useTeams()
  const { favoriteTeams, toggleFavorite, openDetailModal, closeDetailModal } = useTeamStore()

  if (isLoading) {
    return <div>로딩중</div>
  }

  // 에러 상태
  if (isError) {
    return (
      <div className="bg-background flex min-h-screen items-center justify-center">
        <div className="text-center">
          <div className="text-error mb-4">{error?.message || "팀 목록을 불러오는데 실패했습니다."}</div>
          <button onClick={() => window.location.reload()} className="text-main hover:underline">
            다시 시도
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="bg-background min-h-screen">
      <div className="p-6">
        <h1 className="text-text mb-6 text-2xl font-bold">팀 목록</h1>

        <div className="mb-4">
          <p className="text-subtext text-sm">총 {teams.length}개의 팀</p>
        </div>

        {/* 실제 팀 카드들 */}
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {teams.map((team) => (
            <TeamCard
              key={team.teamId}
              {...team}
              isFavorite={favoriteTeams.includes(team.teamId)}
              onClickFavorite={() => toggleFavorite(team.teamId)}
              onClickCard={() => openDetailModal(team.teamId)}
            />
          ))}
        </div>

        {teams.length === 0 && (
          <div className="py-12 text-center">
            <div className="text-subtext">등록된 팀이 없습니다.</div>
          </div>
        )}
      </div>
    </div>
  )
}
