import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { useMutation, useQuery } from "@tanstack/react-query"

// 변경된 경로와 생성 함수 임포트
import { createTeamChatRoom, getTeamChatRoomId } from "@/apis/chatRoom"
import { useUserStore } from "@/stores/userStore"

// 변경된 컴포넌트 경로
import TeamChat from "./organisms/teamChat"

export default function MyTeamPage() {
  const navigate = useNavigate()
  const user = useUserStore((state) => state.user)

  // 최종 채팅방 ID를 관리할 state
  const [chatRoomId, setChatRoomId] = useState<number | null>(null)

  const teamId = user?.teamId
  const studentId = user?.studentId

  useEffect(() => {
    if (user && user.teamId === null) {
      navigate("/create-team")
    }
  }, [user, navigate])

  // 채팅방 생성을 위한 useMutation
  // v5: isLoading -> isPending 으로 변경
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
  // v5: onSuccess, onError 콜백 제거
  const {
    data: fetchedRoomId,
    isFetching,
    isError,
    isSuccess,
  } = useQuery({
    queryKey: ["teamChatRoomId", teamId],
    queryFn: () => getTeamChatRoomId(teamId!),
    enabled: !!teamId,
    retry: false, // 에러 발생 시 재시도 안함
  })

  // v5: useEffect를 사용해 쿼리 상태에 따른 부수 효과 처리
  useEffect(() => {
    // 1. 조회 성공 시, state에 채팅방 ID 저장
    if (isSuccess && fetchedRoomId) {
      setChatRoomId(fetchedRoomId)
    }
  }, [isSuccess, fetchedRoomId])

  useEffect(() => {
    // 2. 조회 실패 시, 생성 mutation 실행
    if (isError && teamId) {
      createRoom(teamId)
    }
  }, [isError, teamId, createRoom])

  useEffect(() => {
    // 3. 생성 성공 시, state에 채팅방 ID 저장
    if (createdRoomId) {
      setChatRoomId(createdRoomId)
    }
  }, [createdRoomId])

  // 조회 중이거나 생성 중일 때 로딩 상태로 간주
  const isLoading = isFetching || isCreating

  return (
    <div className="bg-background min-h-screen p-8">
      <div className="flex justify-between border-1 p-1">
        <div className="w-[650px] border-1 p-1">
          <div className="flex border-1 p-1">내팀정보</div>
          <div className="flex border-1 p-1">대기목록</div>
        </div>

        {/* --- 채팅 섹션 --- */}
        <div className="border-line flex h-[600px] w-[430px] flex-col rounded-lg border-1 bg-white">
          {isLoading && <div className="flex h-full items-center justify-center">채팅방 정보를 불러오는 중...</div>}
          {isError && !isCreating && (
            <div className="flex h-full items-center justify-center text-red-500">
              채팅방 정보를 가져오는데 실패했습니다.
            </div>
          )}

          {/* 최종적으로 확정된 chatRoomId와 studentId가 있을 때만 TeamChat 렌더링 */}
          {chatRoomId && studentId && <TeamChat roomId={chatRoomId} studentId={studentId} />}
        </div>
      </div>
    </div>
  )
}
