import api from "./api";

export async function getAuditoria(pagina, tamanho = 20, sort){
    const response = await api.get("/auditoria", {params: {page: pagina, size: tamanho, sort: sort}});
    return response.data;
}