import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { useMutation, useQuery } from "@tanstack/react-query"

import { createTeamChatRoom, getTeamChatRoomId } from "@/apis/chatRoom"
import { teamApi } from "@/apis/teamApi"
import { useUserStore } from "@/stores/userStore"
import type { IMyTeam, ITeamMember } from "@/types/team"

import TeamChat from "./organisms/teamChat"

export default function MyTeamPage() {
  const navigate = useNavigate()
  const user = useUserStore((state) => state.user)

  const [chatRoomId, setChatRoomId] = useState<number | null>(null)

  const teamId = user?.teamId
  const studentId = user?.studentId

  // --- myTeamApi를 호출하여 팀 정보 전체를 가져오도록 수정 ---
  const {
    data: myTeamData, // select 옵션을 제거하고 원본 데이터를 받습니다.
    isLoading: isMyTeamLoading,
  } = useQuery<IMyTeam>({
    // 반환 타입에서 TeamMember[]를 제거합니다.
    queryKey: ["myTeam"], // /me 엔드포인트는 teamId가 필요 없으므로 제거합니다.
    queryFn: () => teamApi.getMyTeam().then((res) => res.data),
    enabled: !!teamId,
  })

  // 가져온 myTeamData에서 채팅에 필요한 팀원 정보만 파싱합니다.
  const teamMembers: ITeamMember[] | undefined = myTeamData?.teamInfo.members?.map((user) => ({
    studentId: user.studentId,
    name: user.name,
    major: user.major,
    profileImageUrl: user.profileImageUrl,
    position: user.position,
  }))

  // 채팅방 생성을 위한 useMutation
  const {
    mutate: createRoom,
    isPending: isCreating,
    data: createdRoomId,
  } = useMutation({
    mutationFn: (id: number) => createTeamChatRoom(id),
    onError: () => {
      console.error("채팅방 생성에 실패했습니다.")
    },
  })

  // 채팅방 조회를 위한 useQuery
  const {
    data: fetchedRoomId,
    isFetching: isFetchingRoomId,
    isError,
    isSuccess,
  } = useQuery({
    queryKey: ["teamChatRoomId", teamId],
    queryFn: () => getTeamChatRoomId(teamId!),
    enabled: !!teamId,
    retry: false,
  })

  useEffect(() => {
    if (user && user.teamId === null) {
      navigate("/create-team")
    }
  }, [user, navigate])

  useEffect(() => {
    if (isSuccess && fetchedRoomId) {
      setChatRoomId(fetchedRoomId)
    }
  }, [isSuccess, fetchedRoomId])

  useEffect(() => {
    if (isError && teamId) {
      createRoom(teamId)
    }
  }, [isError, teamId, createRoom])

  useEffect(() => {
    if (createdRoomId) {
      setChatRoomId(createdRoomId)
    }
  }, [createdRoomId])

  // 채팅방 정보와 팀원 정보를 모두 로딩 중일 때 로딩 상태로 간주
  const isLoading = isFetchingRoomId || isCreating || isMyTeamLoading

  return (
    <div className="bg-background min-h-screen p-8">
      <div className="flex justify-between border-1 p-1">
        <div className="w-[650px] border-1 p-1">
          <div className="flex border-1 p-1">내팀정보</div>
          <div className="flex border-1 p-1">대기목록</div>
        </div>

        <div className="border-line flex h-[600px] w-[430px] flex-col rounded-lg border-1 bg-white">
          {isLoading && <div className="flex h-full items-center justify-center">채팅 정보를 불러오는 중...</div>}

          {isError && !isCreating && (
            <div className="flex h-full items-center justify-center text-red-500">
              채팅방 정보를 가져오는데 실패했습니다.
            </div>
          )}

          {/* 모든 정보(채팅방ID, 유저ID, 팀원목록)가 준비되었을 때 TeamChat 렌더링 */}
          {chatRoomId && studentId && teamMembers && (
            <TeamChat
              roomId={chatRoomId}
              studentId={studentId}
              members={teamMembers} // 파싱된 팀원 목록을 prop으로 전달
            />
          )}
        </div>
      </div>
    </div>
  )
}
