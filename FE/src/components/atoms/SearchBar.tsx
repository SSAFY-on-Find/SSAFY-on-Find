import { useState } from "react"
import { Search } from "lucide-react"

interface ISearchBar {
  onSearch: (searchValue: string) => void
}

function SearchBar({ onSearch }: ISearchBar) {
  const [searchValue, setSearchValue] = useState("")
  const handleSearch = () => {
    const trimedSearchValue = searchValue.trim()
    if (trimedSearchValue === "") {
      alert("내용을 입력하세요") // toast가 준비되면 바꾸면 좋을 것 같아요!!
      return
    }
    onSearch(trimedSearchValue)
    console.log("검색버튼 클릭! 내용 : ", trimedSearchValue) // 검색 로직 개발 시 여기다 추가하기!!
  }
  const handleEnderPress = (ele: { key: string }) => {
    if (ele.key === "Enter") {
      handleSearch()
    }
  }

  return (
    <div className="relative">
      <Search className="text-subtext absolute top-1/2 left-3 h-5 w-5 -translate-y-1/2 transform" />
      <input
        className="border-subtext/30 placeholder:text-subtext text-text focus:border-main h-[45px] w-full rounded-full border-1 py-2 pr-4 pl-9 text-sm focus:outline-none"
        placeholder="이름으로 검색..."
        value={searchValue}
        onChange={(e) => setSearchValue(e.target.value)}
        onKeyDown={handleEnderPress}
      ></input>
    </div>
  )
}
export default SearchBar
