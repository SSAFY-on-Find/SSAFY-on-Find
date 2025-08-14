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
      qc.invalidateQueries({ queryKey: ["dashboard"], exact: false })
      qc.refetchQueries({ queryKey: ["dashboard"], type: "all" })
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

    // 공통 핸들러: raw(JSON string) → 파싱 → 라우팅
    const handleRaw = (raw: string) => {
      if (!raw || raw.startsWith(":")) return // :connected 등 하트비트 무시
      let msg: ISubscribe
      try {
        msg = JSON.parse(raw)
      } catch {
        return
      }

      const id = msg.id
      if (id && seen.has(id)) return
      if (id) seen.add(id)

      const logicalType = msg.type ?? "UNKNOWN" // "DASHBOARD" | "NOTIFICATION" | ...
      switch (logicalType) {
        case "NOTIFICATION":
          refetchTeamNotifications()
          refetchMyNotifications()
          bump()
          break
        case "DASHBOARD":
          invalidateDashboards()
          bump()
          break
        default:
          // 필요시 기본 처리
          break
      }
    }

    // 탭 중복 연결 방지 (선택): 같은 origin 탭끼리 한 탭만 실제 연결
    const bc = new BroadcastChannel("sse-control")

    // 모든 탭에서 SSE 이벤트 수신 브로드캐스트 처리
    bc.onmessage = (e) => {
      const data = e.data
      if (data?.type === "PONG" || data?.type === "PING") return
      if (data?.type === "SSE_EVENT" && typeof data.payload === "string") {
        // 리더가 보낸 raw 이벤트를 팔로워 탭에서도 처리
        handleRaw(data.payload)
      }
    }

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

      // 1) 커스텀 이벤트: DASHBOARD
      es.addEventListener("DASHBOARD", (e: MessageEvent) => {
        const raw = e.data as string
        handleRaw(raw) // 리더 탭 로컬 처리
        bc.postMessage({ type: "SSE_EVENT", payload: raw }) // 모든 탭에 브로드캐스트
      })

      // 2) 커스텀 이벤트: NOTIFICATION
      es.addEventListener("NOTIFICATION", (e: MessageEvent) => {
        const raw = e.data as string
        handleRaw(raw)
        bc.postMessage({ type: "SSE_EVENT", payload: raw })
      })

      // 3) 이름 없는 기본 메시지(있을 경우 대비)
      es.onmessage = (e) => {
        const raw = e.data as string
        handleRaw(raw)
        bc.postMessage({ type: "SSE_EVENT", payload: raw })
      }

      es.onerror = (e) => {
        console.log("[SSE] error", e)
        // 브라우저 자동 재시도
      }
    }, 150) as unknown as number

    return () => {
      bc.close()
      if (pingTimer) clearTimeout(pingTimer)
    }
  }, [enabled, qc, bump])
}
