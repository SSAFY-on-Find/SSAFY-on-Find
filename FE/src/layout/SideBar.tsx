import { Nav } from "@/components/molecules"

function SideBar() {
  return (
    <aside className="bg-background fixed top-[64px] left-0 flex h-[calc(100vh-64px)] w-[200px] flex-col">
      <Nav />
      <div> 채팅 목록</div>
    </aside>
  )
}

export default SideBar
