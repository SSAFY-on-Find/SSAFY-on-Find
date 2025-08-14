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
  }
  const handleEnderPress = (ele: { key: string }) => {
    if (ele.key === "Enter") {
      handleSearch()
    }
  }
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value
    setSearchValue(value)
    onSearch(value)
  }

  return (
    <div className="relative">
      <Search className="text-subtext absolute top-1/2 left-3 h-5 w-5 -translate-y-1/2 transform" />
      <input
        className="border-subtext/30 placeholder:text-subtext text-text focus:border-main h-[45px] w-full rounded-full border-1 py-2 pr-4 pl-9 text-sm focus:outline-none"
        placeholder="이름으로 검색..."
        value={searchValue}
        onChange={handleInputChange}
        onKeyDown={handleEnderPress}
      ></input>
    </div>
  )
}
export default SearchBar
