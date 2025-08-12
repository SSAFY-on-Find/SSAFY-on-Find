import { toast } from "react-toastify"
import { useMutation, useQueryClient } from "@tanstack/react-query"

import { inviteApi } from "@/apis/inviteApi"
import type { IInvitationRequest } from "@/types/invitation"

/** 선택: 내 팀 ID를 넘길 수 있게. 없으면 MERGE 시 에러 처리 */
export function useInvte(myTeamId?: number | null) {
  const qc = useQueryClient()

  const mutation = useMutation({
    mutationKey: ["team-action"],
    mutationFn: async (payload: IInvitationRequest) => {
      const res = await inviteApi.invite(payload)
      if (res.status !== "SUCCESS") {
        throw new Error("초대 요청에 실패했습니다.")
      }
      return res.data
    },
    retry: false,
    onSuccess: () => {
      // 필요 시 관련 캐시 갱신
      // qc.invalidateQueries({ queryKey: ["notifications"] })
      // qc.invalidateQueries({ queryKey: ["teams"] })
      toast.success("초대 요청이 전송되었습니다.")
    },
  })

  /** 개인이 팀에 지원 */
  const applyAsMate = (targetTeamId: number, myMateId: number) => {
    mutation.mutate({
      subId: targetTeamId,
      subType: "TEAM",
      pubId: myMateId,
      pubType: "MATE",
      type: "APPLICATION",
    })
  }

  /** 내 팀이 다른 팀에 합치기 제안 */
  const mergeTeams = (targetTeamId: number, myTeamId: number) => {
    mutation.mutate({
      subId: targetTeamId,
      subType: "TEAM",
      pubId: myTeamId,
      pubType: "TEAM",
      type: "MERGE",
    })
  }

  return {
    applyAsMate,
    mergeTeams,
    isPending: mutation.isPending,
    error: mutation.error as Error | null,
  }
}
