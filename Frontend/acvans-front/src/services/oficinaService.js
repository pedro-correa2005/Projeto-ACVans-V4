import api from "./api";

export async function listarOficinas({
    termo = ""
}){
    const response = await api.get("/admin/oficinas", {params: {termo}});
    return response.data;
}

export async function criarOficina({
    oficina
}){
    const response = await api.post("/admin/oficinas", oficina);
    return response.data;
}