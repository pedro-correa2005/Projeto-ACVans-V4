import axios from "axios";

import { getCookie } from "../utils/cookies";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
    withCredentials: true
});

api.interceptors.request.use(
    (config) => {
        const csrfToken = getCookie("csrf_token");
        if(csrfToken){
            config.headers[
                "X-CSRF-TOKEN"
            ] = csrfToken;
        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const originalRequest = error.config;

    if (originalRequest.url?.includes("/auth/refresh")) {
      sessionStorage.removeItem("csrf_token");
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {

      originalRequest._retry = true;

      try {
        const response = await api.post("/auth/refresh");
        const novoCsrf = response.data.csrfToken;
        
        if (novoCsrf) {
          sessionStorage.setItem("csrf_token", novoCsrf);
        }
        return api(originalRequest);

      } catch (refreshError) {
        sessionStorage.removeItem(
          "csrf_token"
        );
        return Promise.reject(
          refreshError
        );
      }
    }
    return Promise.reject(error);
  }
);

export default api;