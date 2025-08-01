import { useState } from "react"

import { CheckTag, MainTag, MajorTag, NormalTag, PositionTag, WhiteTag } from "./atoms"

export default function ComponentTestPage() {
  const [postion001, setCheck1] = useState(false)
  const [postion002, setCheck2] = useState(false)
  return (
    <div className="">
      <div className="bg-background p-5">
        <NormalTag tagContent={"기본태그"} />
        <MainTag tagContent={"메인태그"}></MainTag>
        <MainTag tagContent={"메인태그"} fillBg={true}></MainTag>
        <PositionTag positionName={"프론트"}></PositionTag>
        <PositionTag positionName={"백엔드"}></PositionTag>
        <PositionTag positionName={"풀스택"}></PositionTag>
        <PositionTag positionName={"모바일"}></PositionTag>
        <CheckTag tagContent={"체크태그"} onToggle={setCheck1} isChecked={postion001}></CheckTag>
        <CheckTag tagContent={"체크태그"} onToggle={setCheck2} isChecked={postion002}></CheckTag>
        <PositionTag positionName={"임베디드"}></PositionTag>
        <PositionTag positionName={"AI"}></PositionTag>
        <MajorTag tagContent={"전공태그"}></MajorTag>
      </div>
      <div className="bg-black p-5">
        <WhiteTag tagContent={"하양"}></WhiteTag>
        <WhiteTag tagContent={"유령"} fillBg={true}></WhiteTag>
      </div>
    </div>
  )
}
