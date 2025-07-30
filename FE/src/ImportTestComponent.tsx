// src/components/TestComponent.tsx
import { useState } from "react"
import React from "react"
import { Button } from "@headlessui/react"

import "./Button.css"
import "../../assets/style.css"

import "../utils/helpers"

const TestComponent = () => {
	const [count, setCount] = useState(0)

	return (
		<div>
			<Button onClick={() => setCount(count + 1)}>Count: {count}</Button>
		</div>
	)
}

export default TestComponent
