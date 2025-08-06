import axios from "axios"

const BASEURL = "http://localhost:8080/api/v1"
const api = axios.create({
  baseURL: BASEURL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
})

export default api
