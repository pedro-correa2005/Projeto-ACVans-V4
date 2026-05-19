import api from "./api";

export async function listarOficinas({
    termo = ""
}){
    const response = await api.get("/admin/oficinas", {params: {termo}});
    return response.data;
}