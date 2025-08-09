import { SearchBar } from "@/components/atoms"
import { StudentCard } from "@/components/molecules"
import { useProfileCodes } from "@/hooks/useProfile"

import { FilterBox } from "./organisms/FilterBox"

export default function StudentListPage() {
  const { data: codes, isLoading: isCodesLoading, error: codesError } = useProfileCodes()

  if (isCodesLoading) return <div>로딩 중...</div>
  if (codesError || !codes) return <div>코드 리스트를 불러올 수 없습니다.</div>

  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      <SearchBar onSearch={() => {}} />
      <FilterBox positions={codes.position} tracks={codes.track} />
      <div className="grid w-full grid-cols-3 gap-5">
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
        <StudentCard
          student={{ studentId: 1300001, name: "김싸피", major: "전공" }}
          position={{ subcode: "FE", subcodeName: "프론트" }}
          track={{ subcode: "WEB", subcodeName: "웹기술" }}
          goal={{ subcode: "PORTFOLIO", subcodeName: "포트폴리오" }}
          profileImageUrl={""}
          isFavorite={false}
          teamName={"팀 001"}
        />
      </div>
    </div>
  )
}
