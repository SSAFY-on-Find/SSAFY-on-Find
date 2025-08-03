import { Modal } from "../atoms"

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
        <h3 className="text-text text-xl font-bold">{title}</h3>
        <p className="text-subtext">{message}</p>
        <div className="flex justify-end gap-3">
          <button className="text-text rounded-md bg-gray-300 px-3 py-1 hover:bg-gray-400" onClick={onCancel}>
            {cancelText}
          </button>
          <button
            className={`${isDestructive ? "bg-red-600 hover:bg-red-800" : "bg-blue-600 hover:bg-blue-800"} rounded-md px-3 py-1 text-white`}
            onClick={onConfirm}
          >
            {confirmText}
          </button>
        </div>
      </div>
    </Modal>
  )
}
export default ConfirmModal
