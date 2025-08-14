import { useMutation, useQueryClient } from "@tanstack/react-query"

import { inviteApi } from "@/apis/inviteApi"
import type { IInvitationRequest } from "@/types/invitation"

/** 선택: 내 팀 ID를 넘길 수 있게. 없으면 MERGE 시 에러 처리 */
export function useInvte(myTeamId?: number | null) {
  // const qc = useQueryClient()

  const mutation = useMutation({
    mutationKey: ["team-action"],
    mutationFn: async (payload: IInvitationRequest) => {
      await inviteApi.invite(payload)
    },
    retry: false,
    onSuccess: () => {
      // 필요 시 관련 캐시 갱신
      // qc.invalidateQueries({ queryKey: ["notifications"] })
      // qc.invalidateQueries({ queryKey: ["teams"] })
    },
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

export function useInviteAccept(currentTab?: "receive" | "send") {
  const qc = useQueryClient()

  const { mutate, mutateAsync, isPending, error } = useMutation({
    mutationKey: ["invitation-accept"],
    mutationFn: async (notificationId: string) => {
      await inviteApi.accept(notificationId)
    },
    retry: false,
    onSuccess: async () => {
      await qc.invalidateQueries({ queryKey: ["my-notification"] })
      await qc.invalidateQueries({ queryKey: ["myTeam"] })

      if (currentTab) {
        await qc.invalidateQueries({ queryKey: ["my-notification", currentTab] })
        await qc.invalidateQueries({ queryKey: ["team-notification", currentTab] })
      }
    },
  })

  return { acceptInvitation: mutate, acceptInvitationAsync: mutateAsync, isPending, error: error as Error | null }
}

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
