export type Role = "PUBLISHER" | "SUBSCRIBER"
export type TargetType = "TEAM" | "STUDENT"
export type Status = "PENDING" | "ACCEPTED" | "REJECTED" | "CANCELED"

export interface INotificationStatus {
  statusId: string
  notificationId: string
  targetId: number
  targetType: TargetType
  role: Role
  status: Status
  isRead: boolean
  updatedAt: string
  publisherId: number
  publisherType: TargetType
  pubNotificationTitle: string | null
  pubNotificationMessage: string | null
  subscriberId: number
  subscriberType: TargetType
  subNotificationTitle: string | null
  subNotificationMessage: string | null
}

export interface INotification {
  notificationStatusList: INotificationStatus[]
  unReadCount: number
}
