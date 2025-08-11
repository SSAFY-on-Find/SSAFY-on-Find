// dummy/teamCards.ts
import type { IRecruitment, ISubcode } from "@/types/common" // 경로 맞게 수정하세요
import type { ITeamCard, ITeamMember } from "@/types/team" // 경로 맞게 수정하세요

// 트랙/포지션(팀원용) - ISubcode
const TR_WEB: ISubcode = { subcode: "trc_web", subcodeName: "웹기술" }
const POS_FE: ISubcode = { subcode: "pos_fe", subcodeName: "프론트엔드" }
const POS_BE: ISubcode = { subcode: "pos_be", subcodeName: "백엔드" }
const POS_INF: ISubcode = { subcode: "pos_inf", subcodeName: "인프라" }
const POS_MOB: ISubcode = { subcode: "pos_mob", subcodeName: "모바일" }
const POS_AI: ISubcode = { subcode: "pos_ai", subcodeName: "AI" }

// 모집 포지션 - IRecruitment
const REC_FE: IRecruitment = { positionCode: "pos_fe", positionName: "프론트엔드" }
const REC_BE: IRecruitment = { positionCode: "pos_be", positionName: "백엔드" }
const REC_INF: IRecruitment = { positionCode: "pos_inf", positionName: "인프라" }
const REC_MOB: IRecruitment = { positionCode: "pos_mob", positionName: "모바일" }
const REC_AI: IRecruitment = { positionCode: "pos_ai", positionName: "AI" }

// 공통 멤버 더미
const membersA: ITeamMember[] = [
  { studentId: 1300001, name: "김싸피", major: "전공", profileImageUrl: "", position: POS_FE },
  { studentId: 1300002, name: "박싸피", major: "비전공", profileImageUrl: "", position: POS_BE },
]

// 단일 카드
export const teamCardDummy: ITeamCard = {
  teamId: 101,
  teamName: "팀 001",
  description: "혁신적인 웹 서비스를 함께 만들 팀원을 찾고 있어요!",
  track: TR_WEB,
  recruitments: [REC_FE, REC_BE, REC_INF], // ✅ IRecruitment 사용
  members: membersA, // ✅ ITeamMember(position: ISubcode)
  isRecruitingComplete: false,
  isFavorite: false,
  onClickFavorite: () => console.log("favorite: 101"),
  onClickCard: () => console.log("open team 101"),
  variant: "main",
}

// 리스트
export const teamCardListDummy: ITeamCard[] = [
  teamCardDummy,
  {
    teamId: 202,
    teamName: "팀 002",
    description: "RN + Nest로 MVP! iOS/Android 환영",
    track: { subcode: "trc_mob", subcodeName: "모바일" },
    recruitments: [REC_MOB, REC_BE],
    members: [
      { studentId: 1300101, name: "최싸피", major: "전공", profileImageUrl: "", position: POS_MOB },
      { studentId: 1300102, name: "정싸피", major: "전공", profileImageUrl: "", position: POS_BE },
    ],
    isRecruitingComplete: false,
    isFavorite: true,
    onClickFavorite: () => console.log("favorite: 202"),
    onClickCard: () => console.log("open team 202"),
    variant: "main",
  },
  {
    teamId: 303,
    teamName: "팀 003",
    description: "K8s/모니터링/CI-CD 셋업",
    track: { subcode: "trc_inf", subcodeName: "인프라" },
    recruitments: [REC_INF, REC_BE],
    members: [
      { studentId: 1300201, name: "오싸피", major: "전공", profileImageUrl: "", position: POS_INF },
      { studentId: 1300202, name: "유싸피", major: "비전공", profileImageUrl: "", position: POS_BE },
    ],
    isRecruitingComplete: false,
    onClickFavorite: () => console.log("favorite: 303"),
    onClickCard: () => console.log("open team 303"),
    variant: "main",
  },
  {
    teamId: 404,
    teamName: "팀 004",
    description: "LLM + RAG 검색/요약",
    track: { subcode: "trc_ai", subcodeName: "AI" },
    recruitments: [REC_AI, REC_FE],
    members: [{ studentId: 1300301, name: "문싸피", major: "전공", profileImageUrl: "", position: POS_AI }],
    isRecruitingComplete: false,
    onClickFavorite: () => console.log("favorite: 404"),
    onClickCard: () => console.log("open team 404"),
    variant: "main",
  },
]
