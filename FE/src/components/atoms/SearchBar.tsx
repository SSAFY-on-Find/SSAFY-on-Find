import { useState } from "react"
import { Search } from "lucide-react"

import { ConfirmModal } from "../templates"

interface ISearchBar {
  onSearch: (searchValue: string) => void
}

function SearchBar({ onSearch }: ISearchBar) {
  const [searchValue, setSearchValue] = useState("")
  const [isModalOpen, setIsModalOpen] = useState(false)

  const handleSearch = () => {
    const trimedSearchValue = searchValue.trim()
    if (trimedSearchValue === "") {
      setIsModalOpen(true)
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

  const handleModalClose = () => {
    setIsModalOpen(false)
  }

  return (
    <>
      <div className="relative">
        <Search className="text-subtext absolute top-1/2 left-3 h-5 w-5 -translate-y-1/2 transform" />
        <input
          className="border-subtext/30 placeholder:text-subtext text-text focus:border-main h-[45px] w-full rounded-full border-1 py-2 pr-4 pl-9 text-sm focus:outline-none"
          placeholder="이름으로 검색..."
          value={searchValue}
          onChange={handleInputChange}
          onKeyDown={handleEnderPress}
        />
      </div>

      <ConfirmModal
        isOpen={isModalOpen}
        title="알림"
        message="내용을 입력하세요"
        confirmText="확인"
        onConfirm={handleModalClose}
        onCancel={handleModalClose}
        isSingleBtn={true}
      />
    </>
  )
}

export default SearchBar
