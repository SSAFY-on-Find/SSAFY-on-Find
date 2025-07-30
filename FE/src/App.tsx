import { MainTag } from "./components/atoms/MainTag";
import { PositionTag } from "./components/atoms/PositionTag";

const test1 = ["자바", "비전공"];
const test2 = [
  ["프론트", "POS001"],
  ["백엔드", "POS001"],
  ["풀스택", "POS001"],
  ["임베디드", "POS001"],
  ["모바일", "POS001"],
  ["AI", "POS001"],
];
function App() {
  return (
    <>
      {test1.map((t) => (
        <MainTag tagContent={t} />
      ))}
      {test2.map((t) => (
        <PositionTag positionName={t[0]} />
      ))}
    </>
  );
}

export default App;
