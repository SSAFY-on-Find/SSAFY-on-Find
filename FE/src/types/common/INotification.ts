export type NotificationType = "INVITATION" | "MERGE_PROPOSAL"
export type NotificationStatus = "PENDING" | "ACCEPTED" | "REJECTED" | "CANCELLED"

export interface INotification {
  id: string
  type: NotificationType
  status: NotificationStatus
  title: string
  content: string
  timestamp: string
  teamName?: string
  teamMajor?: string
  studentName?: string
  studentMajor?: string
  studentIsMajor?: boolean
}

export interface INotificationTab {
  id: "received" | "sent"
  label: string
}
