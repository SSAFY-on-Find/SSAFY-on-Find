import { SearchBar } from "@/components/atoms"
import { StudentCard } from "@/components/molecules"
import { useProfileCodes } from "@/hooks/useProfile"
import { useStudentList } from "@/hooks/useStudent"

import { FilterBox } from "./organisms/FilterBox"

export default function StudentListPage() {
  const { data: codes, isLoading: isCodesLoading, error: codesError } = useProfileCodes()
  const { data: students, isLoading: isStudentsLoading, error: studentsError } = useStudentList()

  if (isCodesLoading || isStudentsLoading) return <div>로딩 중...</div>
  if (codesError || !codes) return <div>코드 리스트를 불러올 수 없습니다.</div>
  if (studentsError) return <div>교육생 목록을 불러올 수 없습니다.</div>

  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      <SearchBar onSearch={() => {}} />
      <FilterBox positions={codes.position} tracks={codes.track} />
      <div className="grid w-full grid-cols-3 gap-5">
        {students?.length ? (
          students.map((s) => (
            <StudentCard
              key={s.student.studentId}
              student={s.student}
              position={s.position}
              track={s.track}
              goal={s.goal}
              profileImageUrl={s.profileImageUrl}
              isFavorite={s.isFavorite}
              teamName={s.teamName}
            />
          ))
        ) : (
          <div className="text-subtext col-span-full text-center">등록된 교육생이 없습니다.</div>
        )}
      </div>
    </div>
  )
}
