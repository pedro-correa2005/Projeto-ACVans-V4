import api from "./api";

export async function listarRoles(){
    const response = await api.get("/admin/roles");
    return response.data;
}