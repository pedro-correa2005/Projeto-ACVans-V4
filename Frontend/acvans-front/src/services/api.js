import axios from "axios";

import { getCookie } from "../utils/cookies";
import { logout } from "./authService";

//Criação da instância axios
const api = axios.create({
  baseURL: "http://localhost:8080/api",
  withCredentials: true //Envia cookies automaticamente
});

//Intercepta todas as requests antes de saírem
api.interceptors.request.use(
  //Objeto config: url, headers, method, body
  (config) => {
    const csrfToken = getCookie("csrf_token");
    //Adiciona csrfToken ao Header automaticamente
    if (csrfToken) {
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

//Intercepta todas as respostas da api
api.interceptors.response.use(
  //Sucesso
  (response) => response,
  //Erro
  async (error) => {
    
    const originalRequest = error.config; //Request original
    //Erro de rede
    if (!error.response) {
      return Promise.reject(error);
    }

    //Pega url da request
    const url = originalRequest.url;
    //Seguintes urls da api apenas retornam erro
    const ignoredRoutes = [
      "/auth/login",
      "/auth/refresh",
      "/auth/mudar-senha",
      "/auth/esqueci-a-senha",
      "/auth/2fa/verificar",
      "/auth/redefinir-senha/validar-token",
    ];
    const shouldIgnore = ignoredRoutes.some(route => url?.includes(route));
    if (shouldIgnore) {
      return Promise.reject(error);
    }
    // Força alteração de senha no primeiro login
    if (error.response?.status === 403 && error.response?.data?.code === "PASSWORD_CHANGE_REQUIRED") {
      if (window.location.pathname !== "/alterar-senha") {
        window.location.href = "/alterar-senha";
      }
    }
    //Para qualquer outra url tenta refresh (access token expirado)
    if (error.response?.status === 401 && error.response?.data.error === "TOKEN_EXPIRED" && !originalRequest._retry) {
      //Evitar loop infinito
      originalRequest._retry = true;

      //Tenta refresh
      try {
        const response = await api.post("/auth/refresh");
        //Refresh sucesso: Faz novamente a requisição original
        return api(originalRequest);
      } catch (refreshError) {
        //Erro: Sessão expirada
        if(!(window.location.pathname === "/login")){
          window.location.href = "/login";
        }
        console.log(refreshError.response)
        return Promise.reject(refreshError);
      }
    }
    //Qualquer outro erro
    return Promise.reject(error);
  }
);

export default api;