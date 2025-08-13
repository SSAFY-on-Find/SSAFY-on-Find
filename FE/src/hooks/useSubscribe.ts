import { useEffect } from "react"
import { useQueryClient } from "@tanstack/react-query"

import { subscribeSSE } from "@/apis/subscribeApi"
import { sseManager } from "@/libs/sseManager"
import { useNotificationStore } from "@/stores/notificationStore"
import type { EventType, ISubscribe } from "@/types/sse"

export function useNotificationStream(enabled: boolean) {
  const qc = useQueryClient()
  const bump = useNotificationStore((s) => s.bump)

  useEffect(() => {
    if (!enabled) return

    // ====== [added] 유틸: 디바운스 & 중복 이벤트 방지 ======
    const seen = new Set<string>()
    const debounce = <T extends (...a: unknown[]) => void>(fn: T, ms: number) => {
      let t: number | null = null
      return (...args: Parameters<T>) => {
        if (t) clearTimeout(t)
        t = window.setTimeout(() => fn(...args), ms) as unknown as number
      }
    }

    // 대시보드 계열은 서버 집계라 invalidate 권장(이벤트 폭주 대비 디바운스)
    const invalidateDashboards = debounce(() => {
      qc.invalidateQueries({ queryKey: ["dashboard-teamRatio"] })
      qc.invalidateQueries({ queryKey: ["dashboard-positionRatio"] })
      qc.refetchQueries({ queryKey: ["dashboard-teamRatio"], type: "active" })
      qc.refetchQueries({ queryKey: ["dashboard-positionRatio"], type: "active" })
    }, 120)

    const refetchMyNotifications = () => {
      qc.invalidateQueries({ queryKey: ["my-notification"] })
      qc.refetchQueries({ queryKey: ["my-notification"], type: "active" })
    }
    const refetchTeamNotifications = () => {
      qc.invalidateQueries({ queryKey: ["team-notification"] })
      qc.refetchQueries({ queryKey: ["team-notification"], type: "active" })
    }

    // 타입별 핸들러
    const handlers: Record<EventType | string, (msg: ISubscribe<unknown>) => void> = {
      NOTIFICATION: () => {
        refetchTeamNotifications()
        refetchMyNotifications()
        bump()
      },
      DASHBOARD: () => {
        invalidateDashboards()
        bump()
      },
    }

    // 공통 파서 + 라우팅
    const handleRaw = (raw: string) => {
      if (!raw) return // :ping 같은 하트비트 무시
      let msg: ISubscribe
      try {
        msg = JSON.parse(raw) as ISubscribe
      } catch (err) {
        console.error("SSE parse error:", err, raw) // [added] no-empty 방지 및 디버깅
        return
      }

      // 같은 이벤트 id 중복 처리 방지
      if (msg.id && seen.has(msg.id)) return
      if (msg.id) seen.add(msg.id)

      const handler = handlers[msg.type]
      if (handler) {
        handler(msg)
      } else {
        // 모르는 타입이라도 대시보드는 갱신하고, 벨은 눌러주자(옵션)
        // invalidateDashboards()
        // bump()
      }
    }

    // 탭 중복 연결 방지 (선택): 같은 origin 탭끼리 한 탭만 실제 연결
    const bc = new BroadcastChannel("sse-control")
    let leader = false
    const claim = () => bc.postMessage({ type: "PING" })
    let pingTimer: number | null = null

    bc.onmessage = (e) => {
      if (e.data?.type === "PING") bc.postMessage({ type: "PONG" })
      if (e.data?.type === "PONG") leader = false
    }

    leader = true
    claim()
    pingTimer = window.setTimeout(() => {
      if (!leader) return
      // 내가 리더면 실제 EventSource 오픈 (중복 방지)
      const es = sseManager.open(() => subscribeSSE(/* lastEventId 필요시 */))

      es.onopen = () => {
        console.log("[SSE] open:", es.readyState)
      }

      // [changed] 기본(onmessage)과 커스텀 이벤트 둘 다 처리
      es.onmessage = (e) => {
        console.log("[SSE] message received:")
        console.log("- event type:", e.type)
        console.log("- raw data:", e.data)

        try {
          const parsed = JSON.parse(e.data)
          console.log("- parsed type:", parsed.type) // MERGE, INVITATION 등
        } catch (err) {
          console.log("- parse error:", err)
        }
        handleRaw(e.data) // [added]
      }

      es.onerror = (e) => {
        console.log("[SSE] error:", e)
        // 브라우저가 자동 재시도
      }

      // [added] 서버가 event: <커스텀이름>으로 보내는 경우 대비
      es.addEventListener("notification", (e: MessageEvent) => {
        console.log("[SSE] notification:", e.data)
        handleRaw(e.data)
      })

      // (선택) open/error 커스텀 이벤트 로깅
      es.addEventListener("open", () => console.log("[SSE] event:open"))
      es.addEventListener("error", (e) => console.log("[SSE] event:error", e))
    }, 150) as unknown as number

    return () => {
      bc.close()
      if (pingTimer) clearTimeout(pingTimer)
    }
  }, [enabled, qc, bump])
}
