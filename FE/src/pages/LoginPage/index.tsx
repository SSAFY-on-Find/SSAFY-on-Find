import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { useQueryClient } from "@tanstack/react-query"
import { User } from "lucide-react"

import { Button, InputBox } from "@/components/atoms"
import { useStudentLogin } from "@/hooks/useStudent"
import { useUserStore } from "@/stores/userStore"
import type { IStudentSignin } from "@/types/student"

export default function LoginPage() {
  const qc = useQueryClient()
  const [inputBoxValue, setInputBoxValue] = useState("")
  const navigate = useNavigate()
  const { mutate: login, isPending, isError, error } = useStudentLogin()
  const { setUser } = useUserStore()
  const handleFreePass = () => {
    login("1300001", {
      onSuccess: (user) => {
        setUser(user)
        qc.setQueryData<IStudentSignin>(["user-auth"], user)
        navigate("/")
        toast.success("로그인에 성공했습니다.")
      },
    })
  }
  const handleLogin = () => {
    const studentId = inputBoxValue.trim()
    const regex = /^\d{7}$/

    if (!regex.test(inputBoxValue.trim())) {
      toast.warn("학번 7자리를 정확히 입력하세요.")
      return
    }

    login(studentId, {
      onSuccess: (user) => {
        setUser(user)
        qc.setQueryData<IStudentSignin>(["user-auth"], user)
        navigate("/")
        toast.success("로그인에 성공했습니다.")
      },
    })
  }

  useEffect(() => {
    if (isError) {
      toast.error("로그인에 실패했습니다.")
    }
  }, [isError, error])

  if (isPending) {
    return (
      <div className="bg-background flex min-h-screen items-center justify-center">
        <div className="text-main text-center text-xl font-semibold">로그인 중입니다...</div>
      </div>
    )
  }

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

      {/* 로그인 박스 */}
      <div className="z-10 flex w-[450px] flex-col items-center justify-center rounded-2xl bg-white p-15 shadow-2xl">
        <h2 className="text-main mb-2 text-3xl font-extrabold">SSAFY on Find</h2>
        <p className="text-subtext mb-6 text-base font-normal">학번을 입력하여 계정에 접속하세요.</p>

        {/* Input + 로그인 버튼 */}
        <div className="flex w-full flex-col gap-3">
          <InputBox
            text={inputBoxValue}
            size={"s"}
            placeholder={"학번을 입력해주세요"}
            onChange={setInputBoxValue}
            onKeyDown={(e) => {
              if (e.key === "Enter") handleLogin()
            }}
          />
          <Button size={"l"} isIcon={false} text="로그인" onClick={handleLogin} />
          <Button size={"l"} isIcon={false} text="Free Pass" onClick={handleFreePass} />
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
          onClick={() => {
            toast.info("SSAFY 로그인은 준비중입니다.")
          }}
        />
      </div>
    </div>
  )
}
