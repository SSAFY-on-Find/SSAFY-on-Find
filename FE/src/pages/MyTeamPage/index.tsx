import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { UserPlus } from "lucide-react"

import { createTeamChatRoom, getChatMessages, getTeamChatRoomId, leaveChatRoom } from "@/apis/chatRoom"
import { teamApi } from "@/apis/teamApi"
import { TeamDetail } from "@/components/molecules"
import { useTeamNotifications } from "@/hooks/useTeamNotifications"
import { useUserStore } from "@/stores/userStore"
import type { IMyTeam, ITeamMember } from "@/types/team"

import ApplicantCard from "./organisms/ApplicantCard"
import TeamChat from "./organisms/TeamChat"

export default function MyTeamPage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const teamId = useUserStore((state) => state.user?.teamId)
  const studentId = useUserStore((state) => state.user?.studentId)
  const [chatRoomId, setChatRoomId] = useState<number | null>(null)

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
  const { mutate: leaveRoom } = useMutation({
    mutationFn: (id: number) => leaveChatRoom(id),
    onSuccess: () => {
      console.log("채팅방에서 성공적으로 나갔습니다.")
    },
    onError: (error) => {
      console.error("채팅방 나가기 실패:", error)
    },
  })
  const { mutate: leaveTeam } = useMutation({
    mutationFn: () => teamApi.leaveTeam(), // teamApi에 leaveTeam 함수가 있다고 가정
    onSuccess: () => {
      // 1. 팀 탈퇴 성공 시, 채팅방 나가기 실행
      if (chatRoomId) {
        leaveRoom(chatRoomId)
      }
      queryClient.invalidateQueries({ queryKey: ["myTeam"] })
      alert("팀에서 성공적으로 탈퇴했습니다.")
      navigate("/") // 메인 페이지 등으로 이동
    },
    onError: (error) => {
      alert("팀 탈퇴에 실패했습니다.")
      console.error(error)
    },
  })
  const handleLeaveTeam = () => {
    if (window.confirm("정말로 팀에서 탈퇴하시겠습니까?")) {
      leaveTeam()
    }
  }
  const { mutate: createRoom, isPending: isCreating } = useMutation({
    mutationFn: (id: number) => createTeamChatRoom(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["teamChatRoomId", teamId] })
    },
    onError: () => console.error("채팅방 생성에 실패했습니다."),
  })

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

  const { data: initialMessages, isLoading: isMessagesLoading } = useQuery({
    queryKey: ["chatMessages", chatRoomId],
    queryFn: () => getChatMessages(chatRoomId!),
    enabled: !!chatRoomId, // chatRoomId가 확정된 후에만 실행
  })

  const {
    notifications: applicants,
    isLoading: isLoadingApplicants,
    error: applicantsError,
  } = useTeamNotifications(teamId!, "receive")

  useEffect(() => {
    if (!teamId) {
      navigate("/create-team")
    }
  }, [teamId, navigate])

  useEffect(() => {
    if (isSuccess && fetchedRoomId) setChatRoomId(fetchedRoomId)
  }, [isSuccess, fetchedRoomId])

  useEffect(() => {
    if (isError && teamId && !isCreating) createRoom(teamId)
  }, [isError, teamId, createRoom, isCreating])

  // [수정] 전체 로딩 상태에 isMessagesLoading 추가
  const isLoading = isFetchingRoomId || isCreating || isMyTeamLoading || isMessagesLoading

  return (
    <div className="bg-background min-h-screen px-15 py-10">
      <div className="flex justify-between">
        <div className="flex w-[650px] flex-col gap-8">
          <div className="border-line flex rounded-xl border-1 bg-white pb-6">
            {myTeamData?.teamInfo && (
              <TeamDetail {...myTeamData?.teamInfo} varient="myteam" onLeaveTeam={handleLeaveTeam} />
            )}
          </div>
          <div className="border-line flex flex-col gap-4 rounded-xl border-1 bg-white p-6">
            <div>
              <div className="flex items-center gap-[15px]">
                <h3 className="text-text text-2xl font-bold">대기목록</h3>
                <UserPlus />
              </div>
              <p className="text-subtext text-sm text-pretty">
                팀 합류를 신청한 교육생들입니다. 신중하게 검토 후 결정해주세요.
              </p>
            </div>

            {isLoadingApplicants && <div>대기 목록을 불러오는 중...</div>}
            {applicantsError && <div className="text-red-500">대기 목록을 불러오는 데 실패했습니다.</div>}

            {!isLoadingApplicants && !applicantsError && (
              <>
                {applicants && applicants.length > 0 ? (
                  applicants.map((applicant) => <ApplicantCard key={applicant.statusId} applicant={applicant} />)
                ) : (
                  <p className="text-center text-gray-500">합류 신청자가 없습니다.</p>
                )}
              </>
            )}
          </div>
        </div>

        <div className="border-line flex h-[600px] w-[430px] flex-col rounded-lg border-1 bg-white">
          {isLoading && <div className="flex h-full items-center justify-center">채팅 정보를 불러오는 중...</div>}
          {isError && !isCreating && !chatRoomId && (
            <div className="flex h-full items-center justify-center text-red-500">
              채팅방 정보를 가져오는데 실패했습니다.
            </div>
          )}
          {chatRoomId && studentId && teamMembers && (
            <TeamChat
              roomId={chatRoomId}
              studentId={Number(studentId)}
              members={teamMembers}
              initialMessages={initialMessages}
            />
          )}
        </div>
      </div>
    </div>
  )
}
