import api from "./api";

export async function login(email, senha){
    const response = await api.post(
        "/auth/login",
        {email, senha}
    );
    if(response.status === 200){
        sessionStorage.setItem("csrf_token", response.data.csrfToken);
    }
    return {
        status: response.status,
        data: response.data
    };
}

export async function verificar2FA(tempToken, code){
    const response = await api.post("/auth/2fa/verificar",
        {
            tempToken,
            code
        }
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
    try{
        const response = await api.get("/detalhes-usuario");
        return response.data;
    }catch(error){
        setUser(null);
    }
}

export async function forgotPassword(email){
    const response = await api.post("/auth/esqueci-a-senha", {email});
    return response.data;
}

export async function resetPassword(token, novaSenha, repetirNovaSenha){
    const response = await api.post("/auth/redefinir-senha", {token, novaSenha, repetirNovaSenha});
    return response.data;
}

export async function validarResetToken(token){
    const response = await api.get("/auth/redefinir-senha/validar-token", {params: {token}});
    return response;
}