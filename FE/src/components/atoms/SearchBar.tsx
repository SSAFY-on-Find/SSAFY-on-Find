import { Search } from "lucide-react"

function SearchBar() {
  return (
    <>
      <div className="flex">
        <Search />
        <input className="border-subtext/30 w-full rounded-full border-2" placeholder="이름으로 검색..."></input>
      </div>
    </>
  )
}
export default SearchBar
