import { CheckTag } from "@/components/atoms"
import type { ISubcode } from "@/types/common"

interface IFilterBox {
  positions: ISubcode[]
  tracks: ISubcode[]
}

export function FilterBox({ positions, tracks }: IFilterBox) {
  return (
    <div className="border-line flex w-full flex-col gap-5 rounded-lg border bg-white px-8 py-5">
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 text-base font-semibold">포지션</div>
        <div className="flex flex-row gap-2">
          {positions.map((ele) => (
            <CheckTag key={ele.subcode} tagContent={ele.subcodeName} onToggle={() => {}} />
          ))}
        </div>
      </div>
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 text-base font-semibold">전공</div>
        <div className="flex flex-row gap-2">
          <CheckTag key={"전공"} tagContent={"전공"} onToggle={() => {}} />
          <CheckTag key={"비전공"} tagContent={"비전공"} onToggle={() => {}} />
        </div>
      </div>
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 text-base font-semibold">트랙</div>
        <div className="flex flex-row gap-2">
          {tracks.map((ele) => (
            <CheckTag key={ele.subcode} tagContent={ele.subcodeName} onToggle={() => {}} />
          ))}
        </div>
      </div>
      <div className="flex flex-row gap-5">
        <div className="text-text w-15 text-base font-semibold">팀</div>
        <div className="flex flex-row gap-2">
          <CheckTag key={"팀에 소속됨"} tagContent={"팀에 소속됨"} onToggle={() => {}} />
          <CheckTag key={"팀에 소속되지 않음"} tagContent={"팀에 소속되지 않음"} onToggle={() => {}} />
        </div>
      </div>
    </div>
  )
}

export default FilterBox
