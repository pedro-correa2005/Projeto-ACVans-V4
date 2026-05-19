import api from "./api";

export async function login(email, senha){
    const response = await api.post(
        "/auth/login",
        {email, senha}
    );
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

    return response.data;
}

export async function logout(){
    await api.post("/auth/logout");
}

export async function me() {
    try{
        const response = await api.get("/auth/me");
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

export async function alterarSenha(senhaAtual, novaSenha, repetirNovaSenha){
    const response = await api.post("/auth/mudar-senha", {
        senhaAtual,
        novaSenha,
        repetirNovaSenha
    });
    return response;
}

export async function ativar2FA(){
    const response = await api.post("/ativar-autenticacao");
    return response.data;
}

export async function desativar2FA(){
    const response = await api.post("/desativar-autenticacao");
    return response.data;
}