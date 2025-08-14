import { useEffect, useState } from "react"
import { toast } from "react-toastify"
import { isAxiosError } from "axios"
import { Send } from "lucide-react"

import { MajorTag, PositionTag, SearchBar, UserImg } from "@/components/atoms"
import { useInvte } from "@/hooks/useInvite"
import type { IStudentCard } from "@/types/student"

import Modal from "./Modal"

function getErrorMessage(err: unknown, fallback: string) {
  if (isAxiosError(err)) {
    return err.response?.data?.data?.message ?? err.message ?? fallback
  }
  if (err instanceof Error) return err.message ?? fallback
  return fallback
}
interface IStudentSearchModal {
  isOpen: boolean
  onClose: () => void
  students: IStudentCard[]
  onStudentClick?: (userId: string) => void
  myTeamId?: number
}
interface IChatUser {
  id: string
  name: string
  major: string
  position: string
  hasTeam: boolean
}
interface IStudentListItem extends IChatUser {
  onClick: () => void
  userProfile?: string
}
function StudentListItem({ name, major, position, hasTeam, onClick, userProfile }: IStudentListItem) {
  // 1대1 채팅이나 초대 요청시 id기반으로 동작 예정
  return (
    <div
      className="hover:bg-main/10 flex w-full cursor-pointer items-center justify-between gap-2 rounded-md px-1 py-1"
      onClick={onClick}
    >
      <div className="flex items-center gap-2">
        {userProfile ? (
          <UserImg name={name} size={"s"} showTeamBadge={true} hasTeam={hasTeam} url={userProfile} />
        ) : (
          <UserImg name={name} size={"s"} showTeamBadge={true} hasTeam={hasTeam} />
        )}
        <div className="text-sm font-medium">{name}</div>
        <div className="flex items-center justify-center gap-1">
          <MajorTag tagContent={major} />
          {position && <PositionTag positionName={position} />}
        </div>
      </div>
      <Send className="text-main hover:cursor-pointer" />
    </div>
  )
}

function StudentSearchModal({ isOpen, onClose, students, onStudentClick, myTeamId }: IStudentSearchModal) {
  const [searchQuery, setSearchQuery] = useState("")
  const { invitation } = useInvte(myTeamId ?? null)

  useEffect(() => {
    if (isOpen) {
      setSearchQuery("")
    }
  }, [isOpen])

  const filteredStudents = students.filter((ele) => ele.student.name.toLowerCase().includes(searchQuery.toLowerCase()))
  const handleSearch = (query: string) => {
    setSearchQuery(query)
  }
  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"m"}>
      <div className="min-h-[550px] p-10">
        <div className="px-[10px] py-[10px]">
          <h3 className="text-text mb-[15px] text-xl font-bold">교육생 찾기</h3>
          <SearchBar onSearch={handleSearch} />
        </div>
        <div className="flex max-h-[370px] flex-col gap-3 overflow-y-auto px-[15px] pt-[10px]">
          {filteredStudents.map((ele) => {
            const idNum = Number(ele.student.studentId)
            const idStr = String(ele.student.studentId)

            const handlePick = async () => {
              if (typeof myTeamId === "number") {
                await toast.promise(invitation(idNum, myTeamId), {
                  success: "초대를 보냈습니다!",
                  error: { render: ({ data }) => getErrorMessage(data, "초대 전송에 실패했습니다.") },
                })
              } else {
                onStudentClick?.(idStr)
              }
            }

            return (
              <StudentListItem
                key={idStr}
                id={idStr}
                name={ele.student.name}
                major={ele.student.major}
                position={ele.position.subcodeName}
                hasTeam={!!ele.teamName}
                onClick={handlePick}
                userProfile={ele.profileImageUrl}
              />
            )
          })}
        </div>
      </div>
    </Modal>
  )
}
export default StudentSearchModal
