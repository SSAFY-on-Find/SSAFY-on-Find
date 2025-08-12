export type EntityType = "MATE" | "TEAM"
export type RequestType = "APPLICATION" | "INVITATION" | "MERGE"

export interface IInvitationRequest {
  subId: number
  subType: EntityType
  pubId: number
  pubType: EntityType
  type: RequestType
}
