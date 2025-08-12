export interface IAiRecommend {
  studentId: number
  name: string
  profileImageUrl: string | null
  majorYn: boolean
  goal: string
  score: number
  reason: string
}
