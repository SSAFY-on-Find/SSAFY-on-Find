import type { Status } from "../notification"

export type EventType = "INVITATION" | "DASHBOARD"

export interface ISubscribe<T = unknown> {
  id: string
  type: EventType
  event: string //세부 디테일 (APPLICATION" | "INVITATION" | "MERGE" ...)
  status: Status
  time: string
  data: T
}
