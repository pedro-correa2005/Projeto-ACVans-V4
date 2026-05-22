import api from "./api";

export async function getMediaEtapas(mes, ano){
    const response = await api.get(`/relatorios/media-etapas?ano=${ano}&mes=${mes}`);
    return response.data;
}