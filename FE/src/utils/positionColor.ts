import type { PositionRow } from "@/types/dashboard"

export const positionColor = (pos: PositionRow["position"]) => {
  switch (pos) {
    case "프론트":
      return "var(--color-frontend)"
    case "백엔드":
      return "var(--color-backend)"
    case "풀스텍":
      return "var(--color-fullstack)"
    case "임베디드":
      return "var(--color-embedded)"
    case "인프라":
      return "var(--color-infra)"
    case "AI":
      return "var(--color-ai)"
    case "모바일":
      return "var(--color-mobile)"
    default:
      return "var(--color-main)"
  }
}
