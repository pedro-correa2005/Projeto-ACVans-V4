import api from "./api";

export async function listarStatusServico(){
    const response = await api.get("/status-servico");
    return response.data;
}
