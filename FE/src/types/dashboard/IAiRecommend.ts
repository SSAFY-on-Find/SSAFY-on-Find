export interface IAiRecommend {
  studentId: number
  name: string
  profileImageUrl: string | null
  position: string
  majorYn: boolean
  goal: string
  score: number
  reason: string
}
