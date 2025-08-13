import { useEffect } from "react"
import { useQueryClient } from "@tanstack/react-query"

import { subscribeSSE } from "@/apis/subscribeApi"
import { sseManager } from "@/libs/sseManager"
// import type { ISubscribe } from "@/types/sse"

export function useNotificationStream(enabled: boolean) {
  const qc = useQueryClient()

  useEffect(() => {
    if (!enabled) return

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

      es.onmessage = (e) => {
        console.log("[SSE] message:", e.data)

        if (!e.data) return // :ping 등은 무시
        try {
          // const n: ISubscribe = JSON.parse(e.data);

          //대시보드 차트 갱신
          qc.invalidateQueries({ queryKey: ["dashboard-teamRatio"] })
          qc.invalidateQueries({ queryKey: ["dashboard-positionRatio"] })

          //알림함 갱신
          qc.invalidateQueries({ queryKey: ["my-notification"] })
          qc.invalidateQueries({ queryKey: ["team-notification"] })
          // qc.setQueryData<INotification[]>(["my-notification"], (old = []) => [n, ...old]);
        } catch (err) {
          console.error("SSE message parse error:", err)
        }
      }

      es.onerror = (e) => {
        console.log("[SSE] error:", e)
        // 오류 로그만(브라우저가 재시도)
      }

      es.addEventListener("open", () => console.log("[SSE] event:open"))
      es.addEventListener("message", (e) => console.log("[SSE] event:message", e.data))
      es.addEventListener("error", (e) => {
        console.log("[SSE] error:", e)
        // 브라우저가 자동 재시도; 연결 끊겼다면 readyState가 0(connecting) 또는 2(closed)
      })
    }, 150) as unknown as number

    return () => {
      bc.close()
      if (pingTimer) clearTimeout(pingTimer)
    }
  }, [enabled, qc])
}
