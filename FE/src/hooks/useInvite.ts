import { useMutation, useQueryClient } from "@tanstack/react-query"

import { inviteApi } from "@/apis/inviteApi"
import { useUserStore } from "@/stores/userStore"
import type { IInvitationRequest } from "@/types/invitation"
import type { IStudentSignin } from "@/types/student"

// =====================================================
// 1) 초대/지원/합치기 요청 훅
// =====================================================
export function useInvte() {
  const mutation = useMutation({
    mutationKey: ["team-action"],
    mutationFn: async (payload: IInvitationRequest) => {
      await inviteApi.invite(payload)
    },
    retry: false,
  })

  /** 개인이 팀에 지원 */
  const applyAsMate = (targetTeamId: number, myMateId: number) => {
    return mutation.mutateAsync({
      subId: targetTeamId,
      subType: "TEAM",
      pubId: myMateId,
      pubType: "STUDENT",
      type: "APPLICATION",
    })
  }

  /** 내 팀이 다른 팀에 합치기 제안 */
  const mergeTeams = (targetTeamId: number, myTeamId: number) => {
    return mutation.mutateAsync({
      subId: targetTeamId,
      subType: "TEAM",
      pubId: myTeamId,
      pubType: "TEAM",
      type: "MERGE",
    })
  }

  /** 내 팀이 개인에게 초대 요청 */
  const invitation = (targetId: number, myTeamId: number) => {
    return mutation.mutateAsync({
      subId: targetId,
      subType: "STUDENT",
      pubId: myTeamId,
      pubType: "TEAM",
      type: "INVITATION",
    })
  }

  return {
    applyAsMate,
    mergeTeams,
    invitation,
    isPending: mutation.isPending,
    error: mutation.error as Error | null,
  }
}

// =====================================================
// 2) 취소 훅
// =====================================================
export function useInviteCancel(currentTab?: "receive" | "send") {
  const qc = useQueryClient()

  const { mutate, mutateAsync, isPending, error } = useMutation({
    mutationKey: ["invitation-cancel"],
    mutationFn: async (notificationId: string) => {
      await inviteApi.cancel(notificationId)
    },
    retry: false,
    onSuccess: async () => {
      await qc.invalidateQueries({ queryKey: ["my-notification"], exact: false })
      if (currentTab) {
        await qc.invalidateQueries({ queryKey: ["my-notification", currentTab] })
        await qc.invalidateQueries({ queryKey: ["team-notification", currentTab] })
      }
    },
  })

  return { cancelInvitation: mutate, cancleInvitationAsync: mutateAsync, isPending, error: error as Error | null }
}

// =====================================================
// 3) 수락 훅 (★ MERGE 수락 시 nextTeamId 선반영 지원)
//    사용 예: acceptInvitation({ notificationId, nextTeamId: publisherId })
// =====================================================
export function useInviteAccept(currentTab?: "receive" | "send") {
  const qc = useQueryClient()

  type AcceptArgs = { notificationId: string; nextTeamId?: number }

  const { mutate, mutateAsync, isPending, error } = useMutation({
    mutationKey: ["invitation-accept"],
    mutationFn: async ({ notificationId }: AcceptArgs) => {
      await inviteApi.accept(notificationId)
    },
    retry: false,
    onSuccess: async (_data, vars) => {
      //수락으로 팀이 바뀌었을 경우 상태 업데이트
      if (typeof vars?.nextTeamId === "number") {
        const oldTeamId = useUserStore.getState().user?.teamId
        useUserStore.getState().updateUserTeamId(vars.nextTeamId)
        qc.setQueryData<IStudentSignin>(["user-auth"], (prev) => (prev ? { ...prev, teamId: vars.nextTeamId! } : prev))

        // 이전 팀 관련 쿼리 제거/무효화 → 새 팀으로 리패치
        if (typeof oldTeamId === "number") {
          await qc.invalidateQueries({ queryKey: ["team-notification"], exact: false })
        }
        await qc.refetchQueries({ queryKey: ["team-notification"], type: "active" })
      }
      //팀 변경이 없는 일반 수락의 경우
      else {
        await qc.invalidateQueries({ queryKey: ["team-notification"], exact: false })
        await qc.refetchQueries({ queryKey: ["team-notification"], type: "active" })
      }

      await qc.invalidateQueries({ queryKey: ["my-notification"], exact: false })
      await qc.invalidateQueries({ queryKey: ["team-notification"], type: "active" })
      await qc.invalidateQueries({ queryKey: ["myTeam"], exact: false })

      if (currentTab) {
        await qc.invalidateQueries({ queryKey: ["my-notification", currentTab] })
        await qc.invalidateQueries({ queryKey: ["team-notification", currentTab] })
      }
    },
  })

  return { acceptInvitation: mutate, acceptInvitationAsync: mutateAsync, isPending, error: error as Error | null }
}

// =====================================================
// 4) 거절 훅
// =====================================================
export function useInviteReject(currentTab?: "receive" | "send") {
  const qc = useQueryClient()

  const { mutate, mutateAsync, isPending, error } = useMutation({
    mutationKey: ["invitation-reject"],
    mutationFn: async (notificationId: string) => {
      await inviteApi.reject(notificationId)
    },
    retry: false,
    onSuccess: async () => {
      await qc.invalidateQueries({ queryKey: ["my-notification"], exact: false })
      if (currentTab) {
        await qc.invalidateQueries({ queryKey: ["my-notification", currentTab] })
        await qc.invalidateQueries({ queryKey: ["team-notification", currentTab] })
      }
    },
  })

  return { rejectInvitation: mutate, rejectInvitationAsync: mutateAsync, isPending, error: error as Error | null }
}
