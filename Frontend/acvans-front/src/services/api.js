import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
    withCredentials: true
});

api.interceptors.request.use(
    (config) => {
        const csrfToken = sessionStorage.getItem("csrf_token");
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

        if(error.response?.status === 403 && !originalRequest._retry){
            originalRequest._retry = true;

            try{
                const resposne = await api.post("/auth/refresh");

                const novoCsrf = response.data.csrf_token;

                if(novoCsrf){
                    sessionStorage.setItem("csrf_token",novoCsrf);
                }

                return api(originalRequest);
            } catch(refreshError){
                sessionStorage.removeItem("csrf_token");

                window.location.href="/login";

                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default api;