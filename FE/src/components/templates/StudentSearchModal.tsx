import { useEffect, useState } from "react"
import { Send } from "lucide-react"

import type { IStudentCard } from "@/types/student"

import { MajorTag, PositionTag, SearchBar, UserImg } from "../atoms"

import Modal from "./Modal"
interface IStudentSearchModal {
  isOpen: boolean
  onClose: () => void
  students: IStudentCard[]
  onStudentClick: (userId: string) => void
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

function StudentSearchModal({ isOpen, onClose, students, onStudentClick }: IStudentSearchModal) {
  const [searchQuery, setSearchQuery] = useState("")

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
          {filteredStudents.map((ele) => (
            <StudentListItem
              key={ele.student.studentId}
              id={String(ele.student.studentId)}
              name={ele.student.name}
              major={ele.student.major}
              position={ele.position.subcodeName}
              hasTeam={ele.teamName ? true : false}
              onClick={() => onStudentClick(String(ele.student.studentId))}
              userProfile={ele.profileImageUrl}
            />
          ))}
        </div>
      </div>
    </Modal>
  )
}
export default StudentSearchModal
