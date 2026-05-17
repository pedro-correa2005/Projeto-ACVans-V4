import api from "./api";

export async function login(email, senha){
    const response = await api.post(
        "/auth/login",
        {email, senha}
    );

    sessionStorage.setItem("csrf_token", response.data.csrfToken);

    return response.data;
}

export async function logout(){
    try{
        await api.post("/auth/logout");
    }finally{
        sessionStorage.removeItem("csrf-token");
    }
}

export async function me() {
    const response = await api.get("/auth/detalhes-usuario");

    return response.data;
}