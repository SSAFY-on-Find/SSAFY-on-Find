let es: EventSource | null = null

export const sseManager = {
  open(factory: () => EventSource) {
    if (es) return es
    es = factory()
    return es
  },
  close() {
    es?.close()
    es = null
  },
  current() {
    return es
  },
}
