import api from "./index"

function buildUrl(path: string) {
  const base = api.defaults.baseURL ?? ""
  const b = base.endsWith("/") ? base.slice(0, -1) : base
  const p = path.startsWith("/") ? path : `/${path}`
  return `${b}${p}`
}

export function subscribeSSE(lastEventId?: string): EventSource {
  const qs = lastEventId ? `?lastEventId=${encodeURIComponent(lastEventId)}` : ""
  const url = buildUrl(`/notifications/subscribe${qs}`)
  const es = new EventSource(url, { withCredentials: true } as EventSourceInit)
  return es
}
