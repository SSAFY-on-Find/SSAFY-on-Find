import React from "react"

import { UserImg } from "../atoms"

interface IMessageBox {
  who: string
  content: string
  time: string
  name?: string
  profile?: string
}

function MessageBox({ who, content, time, name = "누군가", profile }: IMessageBox) {
  const commonBoxStyle = "p-3 rounded-xl  max-w-80"
  const commonTextStyle = "text-sm break-words"
  const commonTimeStyle = " text-xs"
  // 'me'가 보낸 메시지는 오른쪽 정렬
  if (who === "system") {
    return (
      <div className="flex justify-center">
        <p className={`${commonTimeStyle} text-subtext`}>{content}</p>
      </div>
    )
  } else if (who === "me") {
    return (
      <div className="flex justify-end">
        <div className={`${commonBoxStyle} bg-main/80 flex flex-col gap-1 text-white`}>
          <p className={`${commonTextStyle}`}>{content}</p>
          <p className={`${commonTimeStyle} text-white/60`}>{time}</p>
        </div>
      </div>
    )
  }

  // 상대방이 보낸 메시지는 왼쪽 정렬
  return (
    <div className="mb-4 flex justify-start">
      <div className={`${commonBoxStyle} bg-line text-gray-800`}>
        <div className="mb-[9px] flex items-center gap-3">
          <UserImg name={name} size={"s"} showTeamBadge={false} />
          <p className="text-text text-sm font-semibold">{name}</p>
        </div>
        <div className="flex flex-col gap-1">
          <p className={`${commonTextStyle}`}>{content}</p>
          <p className={`${commonTimeStyle} text-subtext`}>{time}</p>
        </div>
      </div>
    </div>
  )
}

export default MessageBox
