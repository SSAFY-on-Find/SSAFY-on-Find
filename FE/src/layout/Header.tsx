import { Mailbox,Search } from 'lucide-react';

function DeadlineNotification() {
  return (
    <div className="flex justify-center items-center gap-3 border border-main px-3.5 py-1.5 rounded-md">
      <div className="text-main text-sm font-semibold">
        팀빌딩 마감시각
      </div>
      <div className="text-text text-base font-semibold">
        2025.07.21 10:00
      </div>
    </div>
  );
}

function AlarmBox() {
  return (
    <div className="flex justify-center items-center rounded-full bg-main h-[120%] aspect-square shadow-2xl"
        style={{
          boxShadow:
            "0 5px 15px -3px rgba(0, 0, 0, 0.10), 0 4px 6px -4px rgba(0, 0, 0, 0.10)",
    }}>
      <Mailbox className="text-white w-5 h-5"/>
    </div>
  )
}

interface IHeaderProps {
  classCode?: string;
}

function Header({ classCode = "7" }: IHeaderProps) {
  return (
    <header className="flex justify-center items-center gap-3 w-full h-[64px] py-4 px-5 bg-white shadow-xs fixed top-0 left-0 z-50">
      <div className="flex justify-start items-center w-1/7 h-full gap-2 text-main">
        <Search/>
        <h1 className="text-l font-bold">SSAFY On Find</h1>
      </div>
      <div className="flex justify-between items-center w-6/7 h-full">
        <h1 className="text-xl font-bold text-text">서울 {classCode}반</h1>
        <div className="flex justify-center items-center gap-3 h-full">
          <DeadlineNotification />
          <AlarmBox />
        </div>
      </div>
    </header>
  );
}

export default Header;
