import { useState } from "react"
import { User } from "lucide-react"

import { Button, InputBox } from "@/components/atoms"

export default function LoginPage() {
  const [inputBoxValue, setInputBoxValue] = useState("")

  return (
    <div
      className="flex min-h-screen items-center justify-center"
      style={{
        background: "linear-gradient(135deg, #E0E0E0 0%, #6A5ACD 100%), #FFF",
      }}
    >
      {/* 투명 원 */}
      <div className="bg-main/15 absolute top-32 left-110 z-0 h-[180px] w-[180px] rounded-full" />
      <div className="absolute right-100 bottom-20 z-0 h-[240px] w-[240px] rounded-full bg-white/10" />

      <div className="z-10 flex w-[450px] flex-col items-center justify-center rounded-2xl bg-white p-15 shadow-2xl">
        <h2 className="text-main mb-2 text-3xl font-extrabold">SSAFY on Find</h2>
        <p className="text-subtext mb-6 text-base font-normal">학번을 입력하여 계정에 접속하세요.</p>

        {/* Input + 버튼 */}
        <div className="flex w-full flex-col gap-3">
          <InputBox text={inputBoxValue} size={"s"} placeholder={"학번을 입력해주세요"} onChange={setInputBoxValue} />
          <Button size={"l"} isIcon={false} text="로그인" onClick={() => {}} />
        </div>

        {/* Divider */}
        <div className="my-6 flex w-full items-center">
          <div className="border-line flex-1 border-t" />
          <span className="mx-4 text-sm text-gray-400">또는</span>
          <div className="border-line flex-1 border-t" />
        </div>

        {/* SSAFY 로그인 */}
        <Button
          size={"l"}
          isIcon={true}
          Icon={User}
          text="SSAFY 계정으로 로그인"
          variant="outline"
          onClick={() => {}}
        />
      </div>
    </div>
  )
}
