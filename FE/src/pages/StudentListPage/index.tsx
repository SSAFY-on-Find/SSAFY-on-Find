import { useEffect, useMemo, useState } from "react"
import { useSearchParams } from "react-router-dom"

import { SearchBar } from "@/components/atoms"
import { StudentCard } from "@/components/molecules"
import Loading from "@/components/templates/Loading"
import { useProfileCodes } from "@/hooks/useProfile"
import { useAuth, useStudentList } from "@/hooks/useStudent"

import { FilterBox } from "./organisms/FilterBox"

const toCsv = (set: Set<string>) => Array.from(set).join(",")
const fromCsv = (s?: string | null) => new Set((s ?? "").split(",").filter(Boolean))

type MajorFilter = "전공" | "비전공" | null
type TeamFilter = "IN_TEAM" | "NO_TEAM" | null

export default function StudentListPage() {
  const { data: codes, isLoading: isCodesLoading, error: codesError } = useProfileCodes()
  const { data: students, isLoading: isStudentsLoading, error: studentsError } = useStudentList()
  const [searchParams, setSearchParams] = useSearchParams()
  const { data: authData } = useAuth()
  const [searchQuery, setSearchQuery] = useState(() => searchParams.get("q") ?? "")
  const [selectedPositions, setSelectedPositions] = useState<Set<string>>(() => fromCsv(searchParams.get("pos")))
  const [selectedTracks, setSelectedTracks] = useState<Set<string>>(() => fromCsv(searchParams.get("trk")))
  const [selectedMajor, setSelectedMajor] = useState<"전공" | "비전공" | null>(() => {
    const v = searchParams.get("major")
    return v === "전공" || v === "비전공" ? v : null
  })
  const [teamFilter, setTeamFilter] = useState<"IN_TEAM" | "NO_TEAM" | null>(() => {
    const v = searchParams.get("team")
    return v === "IN_TEAM" || v === "NO_TEAM" ? v : null
  })

  // 상태 → URL 동기화 (뒤로가기/새로고침/공유 시 유지)
  useEffect(() => {
    const next = new URLSearchParams(searchParams)
    const setOrDel = (k: string, v?: string) => (v ? next.set(k, v) : next.delete(k))

    setOrDel("q", searchQuery.trim() || undefined)
    setOrDel("pos", selectedPositions.size ? toCsv(selectedPositions) : undefined)
    setOrDel("trk", selectedTracks.size ? toCsv(selectedTracks) : undefined)
    setOrDel("major", selectedMajor ?? undefined)
    setOrDel("team", teamFilter ?? undefined)

    if (next.toString() !== searchParams.toString()) {
      setSearchParams(next, { replace: true })
    }
  }, [searchQuery, selectedPositions, selectedTracks, selectedMajor, teamFilter])

  // 토글 핸들러
  const togglePosition = (code: string) =>
    setSelectedPositions((prev) => {
      const next = new Set(prev)
      if (next.has(code)) {
        next.delete(code)
      } else {
        next.add(code)
      }
      return next
    })

  const toggleTrack = (code: string) =>
    setSelectedTracks((prev) => {
      const next = new Set(prev)
      if (next.has(code)) {
        next.delete(code)
      } else {
        next.add(code)
      }
      return next
    })

  const toggleMajor = (val: Exclude<MajorFilter, null>) => setSelectedMajor((prev) => (prev === val ? null : val))

  const toggleTeam = (val: Exclude<TeamFilter, null>) => setTeamFilter((prev) => (prev === val ? null : val))

  // 검색 + 필터 적용
  const filtered = useMemo(() => {
    const list = students ?? []
    const q = searchQuery.trim().toLowerCase()

    return list.filter((s) => {
      // 검색(이름)
      if (q) {
        const hit = s.student.name.toLowerCase().includes(q)
        if (!hit) return false
      }

      // 포지션/트랙은 subcode로 비교
      if (selectedPositions.size && !selectedPositions.has(s.position.subcode)) return false
      if (selectedTracks.size && !selectedTracks.has(s.track.subcode)) return false

      // 전공 여부
      if (selectedMajor) {
        const isMajor = s.student.major === "전공"
        if (selectedMajor === "전공" && !isMajor) return false
        if (selectedMajor === "비전공" && isMajor) return false
      }

      // 팀 소속 여부: teamName 유무
      if (teamFilter) {
        const inTeam = Boolean(s.teamName)
        if (teamFilter === "IN_TEAM" && !inTeam) return false
        if (teamFilter === "NO_TEAM" && inTeam) return false
      }

      return true
    })
  }, [students, searchQuery, selectedPositions, selectedTracks, selectedMajor, teamFilter])

  if (isCodesLoading || isStudentsLoading) return <Loading fullScreen />
  if (codesError || !codes) return <div>코드 리스트를 불러올 수 없습니다.</div>
  if (studentsError) return <div>교육생 목록을 불러올 수 없습니다.</div>

  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      <SearchBar onSearch={setSearchQuery} />
      <FilterBox
        positions={codes.position}
        tracks={codes.track}
        selectedPositions={selectedPositions}
        selectedTracks={selectedTracks}
        selectedMajor={selectedMajor}
        teamFilter={teamFilter}
        onTogglePosition={togglePosition}
        onToggleTrack={toggleTrack}
        onToggleMajor={toggleMajor}
        onToggleTeam={toggleTeam}
      />
      <div className="grid w-full grid-cols-3 gap-5">
        {filtered.length ? (
          filtered.map((s) => (
            <StudentCard
              key={s.student.studentId}
              student={s.student}
              position={s.position}
              track={s.track}
              goal={s.goal}
              profileImageUrl={s.profileImageUrl}
              isFavorite={s.isFavorite}
              teamName={s.teamName ?? ""}
              userId={Number(authData?.studentId)}
            />
          ))
        ) : (
          <div className="text-subtext col-span-full text-center">
            {searchQuery || selectedPositions.size || selectedTracks.size || selectedMajor || teamFilter
              ? "검색/필터 결과가 없습니다."
              : "등록된 교육생이 없습니다."}
          </div>
        )}
      </div>
    </div>
  )
}
