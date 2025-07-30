// src/ImportTestComponent.tsx

// 잘못된 import 순서 (ESLint가 자동으로 정렬해야 함)
import "./ImportTestComponent.css" // 내부 파일 (아래로 가야 함)
import { Button } from "@headlessui/react" // 외부 라이브러리 (위로 가야 함)
import axios from "axios" // 외부 라이브러리 (위로 가야 함)
import { useState } from "react" // 외부 라이브러리 (위로 가야 함)
import React from "react" // 외부 라이브러리 (위로 가야 함)

import utils from "../utils/helpers" // 내부 파일 (아래로 가야 함)

import { formatDate } from "./utils" // 내부 파일 (아래로 가야 함)

const ImportTestComponent: React.FC = () => {
	const [count, setCount] = useState(0)

	return (
		<div>
			<h1>Import 순서 테스트</h1>
			<Button onClick={() => setCount(count + 1)}>Count: {count}</Button>
			<p>현재 시간: {formatDate(new Date())}</p>
		</div>
	)
}

export default ImportTestComponent
