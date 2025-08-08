import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"

import { createTeamChatRoom, getTeamChatRoomId } from "@/apis/chatRoom"
import { teamApi } from "@/apis/teamApi"
import { useUserStore } from "@/stores/userStore"
import type { IMyTeam, ITeamMember } from "@/types/team"

import TeamChat from "./organisms/TeamChat"

export default function MyTeamPage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient() // queryClient를 가져옵니다.
  const user = useUserStore((state) => state.user)

  const [chatRoomId, setChatRoomId] = useState<number | null>(null)

  const teamId = user?.teamId
  const studentId = user?.studentId

  const { data: myTeamData, isLoading: isMyTeamLoading } = useQuery<IMyTeam>({
    queryKey: ["myTeam"],
    queryFn: () => teamApi.getMyTeam().then((res) => res.data),
    enabled: !!teamId,
  })

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
    // --- 여기가 수정된 부분입니다 ---
    onSuccess: () => {
      // 채팅방 생성 성공 시, 채팅방 조회 쿼리를 무효화하여 다시 불러오게 합니다.
      // 이렇게 하면 isError 상태가 초기화되고, isSuccess가 true가 됩니다.
      queryClient.invalidateQueries({ queryKey: ["teamChatRoomId", teamId] })
    },
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
    // 채팅방 생성을 시도하는 로직은 그대로 둡니다.
    if (isError && teamId && !isCreating) {
      createRoom(teamId)
    }
  }, [isError, teamId, createRoom, isCreating])

  useEffect(() => {
    // 이 로직은 생성 후 ID를 더 빨리 반영하기 위해 유지할 수 있습니다.
    if (createdRoomId) {
      setChatRoomId(createdRoomId)
    }
  }, [createdRoomId])

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

          {/* 채팅방이 최종적으로 없을 때만 에러 메시지를 표시하도록 수정 */}
          {isError && !isCreating && !chatRoomId && (
            <div className="flex h-full items-center justify-center text-red-500">
              채팅방 정보를 가져오는데 실패했습니다.
            </div>
          )}

          {chatRoomId && studentId && teamMembers && (
            <TeamChat roomId={chatRoomId} studentId={studentId} members={teamMembers} />
          )}
        </div>
      </div>
    </div>
  )
}
