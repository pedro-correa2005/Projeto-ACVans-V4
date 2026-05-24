import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";
import { validarTokenAtualizacao, atualizarEtapa } from "../services/servicoService";

import { Link } from "react-router-dom";

import Header from "../components/Header";
import Footer from "../components/Footer";

function AtualizarEtapa(){
    const navigate = useNavigate();
    const [checkingToken, setCheckingToken] = useState(true);
    const [ servico, setServico ] = useState(null);
    const [ etapa, setEtapa ] = useState(null);
    const [ loading, setLoading ] = useState(true);

    const [searchParams] = useSearchParams();

    const token = searchParams.get("token");

    useEffect(() => {
        async function validarToken() {
            if(!token){
                toast.error("Token inválido");
                navigate("/");
                return;
            }
            try{
                const response = await validarTokenAtualizacao(token);
                setServico(response.servico);
                setEtapa(response.proximaEtapa);
                setLoading(false);
            }catch(error){
                console.err(error);
                toast.error(error?.data?.message || "Link inválido");
                navigate("/");
            }finally{
                setCheckingToken(false);
            }
        }
        validarToken();
    }, [token, navigate]);

    async function handleConfirmar(){
        setLoading(true);
        try {
            const response = await atualizarEtapa(token);
            toast.success("Etapa atualizada com sucesso");
            navigate("/");
        } catch (error) {
            console.err(error);
                toast.error(error?.data?.message || "Erro ao atualizar");
                navigate("/");
        }
    }

    return (
        <>
        <Header />
        <main className="container mt-4">
            <h1>Atualizar Etapa de Serviço</h1>
            <p>Confirme nova etapa para o serviço: {servico?.tipo?.descricao}. Veículo: {servico?.veiculo?.marca}, {servico?.veiculo?.modelo}, {servico?.veiculo?.placa}</p>
            <div className="form-group">
                <label htmlFor="etapa">Nova etapa:</label>
                <input type="text" className="form-control" value={etapa != null? (etapa.titulo != null? etapa.titulo: etapa) : ""}
                readOnly/>
            </div>
            <div className="form-actions">
                <button disabled={loading} className="btn btn-primary" onClick={handleConfirmar}>Confirmar Atualização</button>
                <Link to="/"className="btn btn-secondary">Cancelar</Link>
            </div>
        </main>
        <Footer />
        </>
    )
}

export default AtualizarEtapa;