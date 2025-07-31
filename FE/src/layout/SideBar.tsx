import { Nav } from "@/components/molecules";

function SideBar() {
	return (
    <aside className="fixed top-[64px] left-0 h-[calc(100vh-64px)] w-[200px] bg-background flex flex-col">
      <Nav />
      <div> 채팅 목록</div>
    </aside>
	)
}

export default SideBar