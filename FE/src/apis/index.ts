import axios from "axios"

const BASEURL = import.meta.env.VITE_APP_BASE_URL
const api = axios.create({
  baseURL: BASEURL,
  withCredentials: true,
  // headers: {
  //   "Content-Type": "application/json",
  // },
})

export default api
