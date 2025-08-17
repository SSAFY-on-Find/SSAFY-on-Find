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
      qc.invalidateQueries({ queryKey: ["my-notification"], exact: false })
      qc.refetchQueries({ queryKey: ["my-notification"], exact: false, type: "all" })
    }
    const refetchTeamNotifications = () => {
      qc.invalidateQueries({ queryKey: ["team-notification"], exact: false })
      qc.refetchQueries({ queryKey: ["team-notification"], exact: false, type: "all" })
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
    let leader = false

    // 모든 탭에서 SSE 이벤트 수신 브로드캐스트 처리
    bc.onmessage = (e) => {
      const data = e.data
      // 리더 선출용
      if (data?.type === "PING") return bc.postMessage({ type: "PONG" })
      if (data?.type === "PONG") {
        leader = false
        return
      }

      // 이벤트 브로드캐스트 수신
      if (data?.type === "SSE_EVENT" && typeof data.payload === "string") {
        handleRaw(data.payload)
      }
    }

    leader = true
    bc.postMessage({ type: "PING" })
    const pingTimer = window.setTimeout(() => {
      if (!leader) return
      const es = sseManager.open(() => subscribeSSE())

      const relay = (raw: string) => {
        handleRaw(raw) // 리더 자신 처리
        bc.postMessage({ type: "SSE_EVENT", payload: raw }) // 팔로워들에게 전파
      }

      es.addEventListener("DASHBOARD", (e: MessageEvent) => relay(String(e.data)))
      es.addEventListener("NOTIFICATION", (e: MessageEvent) => relay(String(e.data)))
      es.onmessage = (e) => relay(String(e.data))
      es.onerror = (e) => console.log("[SSE] error", e)
    }, 150)

    return () => {
      bc.close()
      if (pingTimer) clearTimeout(pingTimer)
    }
  }, [enabled, qc, bump])
}
