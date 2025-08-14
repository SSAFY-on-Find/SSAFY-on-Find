import { useEffect, useMemo, useState } from "react"
import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { isAxiosError } from "axios"
import { UserPlus } from "lucide-react"

import { createTeamChatRoom, getChatMessages, getTeamChatRoomId, leaveChatRoom } from "@/apis/chatRoom"
import { teamApi } from "@/apis/teamApi"
import { Button } from "@/components/atoms"
import { Segmented } from "@/components/atoms"
import { TeamDetail } from "@/components/molecules"
import { StudentSearchModal } from "@/components/templates"
import Loading from "@/components/templates/Loading"
import { useInviteAccept, useInviteCancel, useInviteReject } from "@/hooks/useInvite"
import { useTeamNotification } from "@/hooks/useNotification"
import { useAuth, useStudentList } from "@/hooks/useStudent"
import { useLeaveTeam } from "@/hooks/useTeam"
import type { IMyTeam, ITeamMember } from "@/types/team"

import ApplicantCard from "./organisms/ApplicantCard"
import TeamChat from "./organisms/TeamChat"

function getErrorMessage(err: unknown, fallback: string) {
  if (isAxiosError(err)) {
    return err.response?.data?.data?.message ?? err.message ?? fallback
  }
  if (err instanceof Error) return err.message ?? fallback
  return fallback
}

export default function MyTeamPage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { data: authData } = useAuth()
  const teamId = authData?.teamId
  const studentId = authData?.studentId
  const [chatRoomId, setChatRoomId] = useState<number | null>(null)
  const [requestTab, setRequestTab] = useState<"left" | "right">("left")
  const requestType = requestTab === "left" ? "receive" : "send"

  const { mutate: leaveTeam } = useLeaveTeam()
  const [studentSearchModal, setStudentSearchModal] = useState(false)
  const { data: students, isLoading: isStudentListLoading } = useStudentList()
  const { data: myTeamData, isLoading: isMyTeamLoading } = useQuery<IMyTeam>({
    queryKey: ["myTeam"],
    queryFn: () => teamApi.getMyTeam().then((res) => res.data),
    enabled: !!teamId,
  })
  const studentsExceptMe = students?.filter((s) => s.student.studentId !== Number(authData?.studentId))
  const rawMembers = myTeamData?.teamInfo.members ?? []
  const teamMembers: ITeamMember[] = useMemo(
    () =>
      rawMembers.map((user) => ({
        studentId: user.studentId,
        name: user.name,
        major: user.major,
        profileImageUrl: user.profileImageUrl,
        position: user.position,
      })),
    [rawMembers]
  )

  const { mutate: leaveRoom } = useMutation({
    mutationFn: (id: number) => leaveChatRoom(id),
    onSuccess: () => {},
    onError: (error) => {
      console.error("채팅방 나가기 실패:", error)
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

  const handleInviteStudent = (targetStudentId: number) => {
    // 새로운 학생을 우리 팀으로 초대하는 로직이 들어가야합니다!
  }

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
    data: teamRequests,
    isLoading: isLoadingTeamRequests,
    error: teamRequestsError,
  } = useTeamNotification(typeof teamId === "number" ? teamId : null, requestType)

  const type = requestTab === "left" ? "receive" : "send"
  const { cancleInvitationAsync, isPending: isCanceling } = useInviteCancel(type)
  const { acceptInvitationAsync, isPending: isAccepting } = useInviteAccept(type)
  const { rejectInvitationAsync, isPending: isRejecting } = useInviteReject(type)

  const handleAcceptInvitation = async (id: string) => {
    if (!id) return
    await toast.promise(acceptInvitationAsync(id), {
      success: "초대를 수락했습니다!",
      error: { render: ({ data }) => getErrorMessage(data, "수락 중 오류가 발생했습니다.") },
    })
  }
  const handleRejectInvitation = async (id: string) => {
    if (!id) return
    await toast.promise(rejectInvitationAsync(id), {
      success: "초대를 거절했습니다!",
      error: { render: ({ data }) => getErrorMessage(data, "거절 중 오류가 발생했습니다.") },
    })
  }
  const handleCancelInvitation = async (id: string) => {
    if (!id) return
    await toast.promise(cancleInvitationAsync(id), {
      success: "초대를 취소했습니다!",
      error: { render: ({ data }) => getErrorMessage(data, "취소 중 오류가 발생했습니다.") },
    })
  }

  useEffect(() => {
    if (!teamId && !isMyTeamLoading) {
      navigate("/no-team")
    }
  }, [teamId, navigate, isMyTeamLoading])
  useEffect(() => {
    if (isSuccess && fetchedRoomId) setChatRoomId(fetchedRoomId)
  }, [isSuccess, fetchedRoomId])

  useEffect(() => {
    if (isError && teamId && !isCreating) createRoom(teamId)
  }, [isError, teamId, createRoom, isCreating])

  const isLoading = isFetchingRoomId || isCreating || isMyTeamLoading || isMessagesLoading

  return (
    <div className="bg-background min-h-screen px-15 py-10">
      <div className="flex flex-col gap-6 lg:flex-row lg:gap-8">
        <div className="flex w-full flex-col gap-8 lg:flex-1">
          <div className="border-line flex rounded-xl border-1 bg-white pb-6">
            {myTeamData?.teamInfo && (
              <TeamDetail {...myTeamData?.teamInfo} varient="myteam" onLeaveTeam={handleLeaveTeam} />
            )}
          </div>
          <div className="border-line flex flex-col gap-4 rounded-xl border-1 bg-white p-6">
            <div>
              <div className="flex items-center gap-[15px]">
                <h3 className="text-text text-2xl font-bold">대기목록</h3>
                <div className="w-30">
                  <Button
                    size={"s"}
                    isIcon={true}
                    Icon={UserPlus}
                    variant="outline"
                    text="팀원 초대"
                    onClick={() => setStudentSearchModal(true)}
                  />
                </div>
                {!isStudentListLoading && studentsExceptMe && (
                  <StudentSearchModal
                    isOpen={studentSearchModal}
                    onClose={() => setStudentSearchModal(false)}
                    students={studentsExceptMe}
                    myTeamId={Number(teamId)}
                  />
                )}
              </div>
              <p className="text-subtext mt-2 text-sm text-pretty">
                팀 합류를 신청한 교육생들입니다. 신중하게 검토 후 결정해주세요.
              </p>
            </div>

            <Segmented activeSegment={requestTab} onSegmentChange={setRequestTab} />

            {isLoadingTeamRequests && <Loading text="요청 목록을 불러오는 중" />}
            {teamRequestsError && <div className="text-error">요청 목록을 불러오는 데 실패했습니다.</div>}

            {!isLoadingTeamRequests && !teamRequestsError && (
              <>
                {teamRequests && teamRequests.length > 0 ? (
                  teamRequests.map((req) => (
                    <ApplicantCard
                      key={String(req.notificationId)}
                      {...req}
                      tab={requestType}
                      onAccept={handleAcceptInvitation}
                      onReject={handleRejectInvitation}
                      onCancel={handleCancelInvitation}
                    />
                  ))
                ) : (
                  <p className="text-subtext text-center text-sm">
                    {requestType === "receive" ? "받은 요청이 없습니다." : "보낸 요청이 없습니다."}
                  </p>
                )}
              </>
            )}
          </div>
        </div>

        <div className="border-line flex h-[400px] w-full flex-col rounded-lg border-1 bg-white md:h-[500px] lg:h-[600px] lg:w-[430px] lg:flex-shrink-0">
          {isLoading && <div className="flex h-full items-center justify-center">채팅 정보를 불러오는 중...</div>}
          {isError && !isCreating && !chatRoomId && (
            <div className="text-error flex h-full items-center justify-center">
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
