import { useEffect } from "react"
import { useQueryClient } from "@tanstack/react-query"

import { subscribeSSE } from "@/apis/subscribeApi" // EventSource 생성만
import { sseManager } from "@/libs/sseManager"
import type { INotification } from "@/types/notification"

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
      // if (e.data?.type === "NOTIFY") handleMessage(e.data.payload)
    }

    // const handleMessage = (payload: any) => {
    //   // 서버 포맷에 맞게 파싱
    //   const n = payload as INotification
    //   // 알림함 캐시 갱신
    //   // qc.setQueryData<INotification[]>(["my-notification", n.type], (old = []) => [n, ...old])
    //   // if (n.teamId) {
    //   //   qc.setQueryData<INotification[]>(["team-notification", n.teamId, n.type], (old = []) => [n, ...old])
    //   // }
    //   // 대시보드가 서버 집계라면 invalidate로 재페치
    //   qc.invalidateQueries({ queryKey: ["dashboard"] })
    // }

    // 1) 자신이 리더인지 확인 (응답 없으면 내가 리더)
    leader = true
    claim()
    pingTimer = window.setTimeout(() => {
      if (!leader) return
      // 내가 리더면 실제 EventSource 오픈 (중복 방지)
      const es = sseManager.open(() => subscribeSSE(/* lastEventId 필요시 */))

      es.onmessage = (e) => {
        let payload: INotification = e.data
        try {
          payload = JSON.parse(e.data)
        } catch {
          return
        }
        // handleMessage(payload)
        bc.postMessage({ type: "NOTIFY", payload }) // 다른 탭에도 브로드캐스트
      }
      es.onerror = () => {
        // 오류 로그만(브라우저가 재시도)
      }
    }, 150) as unknown as number

    return () => {
      bc.close()
      if (pingTimer) clearTimeout(pingTimer)
      // 전역 유지 원하면 여기서 sseManager.close() 호출하지 마세요.
      // 로그아웃에서만 닫는 게 안전.
    }
  }, [enabled, qc])

  // 로그아웃에서 쓸 종료 함수가 필요하면, 별도 export 하거나 manager를 직접 쓰세요.
}

// import { useEffect, useRef } from "react"
// import { useQueryClient } from "@tanstack/react-query"

// import { subscribeSSE } from "@/apis/subscribeApi"

// export function useNotificationStream(enabled: boolean) {
//   const ref = useRef<EventSource | null>(null)
//   const qc = useQueryClient()

//   useEffect(() => {
//     if (!enabled || ref.current) return
//     const es = subscribeSSE()
//     ref.current = es

//     es.onmessage = (e) => { /* setQueryData 등 */ }
//     es.onerror = () => { /* 로그만 */ }

//     // 전역 유지 원하면 cleanup에서 닫지 말고, 로그아웃 핸들러에서 닫기
//     return () => { es.close(); ref.current = null }
//   }, [enabled, qc])

//   const close = () => {
//     ref.current?.close()
//     ref.current = null
//   }

//   return { close }
// }
