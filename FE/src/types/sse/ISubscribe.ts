import type { Status } from "../notification"

export type NotificationType = "APPLICATION" | "INVITATION" | "MERGE"

export interface ISubscribe<T = unknown> {
  id: string
  event: string
  type: NotificationType
  status: Status
  time: string
  data: T
}
