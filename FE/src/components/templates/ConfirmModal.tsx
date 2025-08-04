import { Button } from "@/components/atoms"

import Modal from "./Modal"

interface IConfirmModal {
  isOpen: boolean
  title?: string
  message: string
  confirmText?: string
  cancelText?: string
  onConfirm: () => void
  onCancel: () => void
  isDestructive?: boolean
}
function ConfirmModal({
  isOpen,
  title = "확인",
  message,
  confirmText = "확인",
  cancelText = "취소",
  onConfirm,
  onCancel,
  isDestructive = false,
}: IConfirmModal) {
  return (
    <Modal isOpen={isOpen} onClose={onCancel} size={"s"}>
      <div className="p-5">
        <h3 className="text-text mb-3 text-xl font-bold">{title}</h3>
        <p className="text-subtext text-sm">{message}</p>
        <div className="mt-4 flex justify-end gap-3">
          <Button size={"m"} isIcon={false} text={cancelText} onClick={onCancel} variant="text" />
          <Button
            size={"m"}
            isIcon={false}
            text={confirmText}
            onClick={onConfirm}
            variant={`${isDestructive ? "danger" : "primary"}`}
          />
        </div>
      </div>
    </Modal>
  )
}
export default ConfirmModal
