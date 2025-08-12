export interface INotification {
  notificationStatusList: INotificationStatusResponseDto[]
  unReadCount: number
}
export interface INotificationStatusResponseDto {
  statusId: string
  notificationId: string
  targetId: number
  targetType: "team" | "student"
  role: "publisher" | "subscriber"
  status: "pending" | "accepted" | "rejected" | "canceled"
  isRead: boolean
  updatedAt: string
  publisherId: number
  publisherType: "team" | "student"
  pubNotificationTitle: string
  pubNotificationMessage: string
  subscriberId: number
  subscriberType: "team" | "student"
  subNotificationTitle: string
  subNotificationMessage: string
}
export interface IGetTeamNotificationsParams {
  teamId: number
  type: string
}
