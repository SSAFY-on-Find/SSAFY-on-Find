export function formatMessageTime(dateString: string): string {
  const messageDate = new Date(dateString)
  const today = new Date()
  const yesterday = new Date(today)
  yesterday.setDate(yesterday.getDate() - 1)

  const timeOptions: Intl.DateTimeFormatOptions = {
    hour: "2-digit",
    minute: "2-digit",
  }

  const dateOptions: Intl.DateTimeFormatOptions = {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  }

  // 날짜 부분만 비교하기 위해 시간은 0으로 설정
  const messageDay = new Date(messageDate).setHours(0, 0, 0, 0)
  const todayDay = new Date(today).setHours(0, 0, 0, 0)
  const yesterdayDay = new Date(yesterday).setHours(0, 0, 0, 0)

  if (messageDay === todayDay) {
    return messageDate.toLocaleTimeString("ko-KR", timeOptions)
  }

  if (messageDay === yesterdayDay) {
    return `어제 ${messageDate.toLocaleTimeString("ko-KR", timeOptions)}`
  }

  return messageDate.toLocaleDateString("ko-KR", dateOptions).slice(0, -1) // 마지막 '.' 제거
}
