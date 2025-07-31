function SideBar() {
	return (
    <aside className="fixed top-[64px] left-0 h-[calc(100vh-64px)] w-[200px] bg-background flex flex-col">
        <nav>
            <ul className="space-y-2">
                <li><a href="/">통계 대시보드</a></li>
                <li><a href="/myteam">내 팀 관리</a></li>
                <li><a href="/teamlist">팀 목록</a></li>
                <li><a href="/studentlist">교육생 목록</a></li>
                <li><a href="/mypage">마이페이지</a></li>
            </ul>
        </nav>
        <div>채팅</div>
    </aside>
	)
}

export default SideBar