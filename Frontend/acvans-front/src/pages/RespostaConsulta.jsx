import { useEffect, useState } from "react";

import {
    Link,
    useSearchParams
} from "react-router-dom";

import { toast } from "react-toastify";

import api from "../services/api"

import Header from "../components/Header";
import Footer from "../components/Footer";

import StatusTracker from "../components/StatusTracker";

function RespostaConsulta() {
    const [searchParams] = useSearchParams();

    const token = searchParams.get("token");
    const placa = searchParams.get("placa");

    const [servico, setServico] = useState(null);
    const [loading, setLoading] = useState(true);
    const [erro, setErro] = useState(false);

    useEffect(() => {
        async function carregarServico(){
            try{
                setLoading(true);
                const response = await api.get("/consulta", {
                    params: {
                        token,
                        placa
                    }
                });
                setServico(response.data);
            } catch (error) {
                console.error(error);
                setErro(true);
                toast.error("Erro " + error);
            }finally{
                setLoading(false);
            }
        }

        if(token && placa){
            carregarServico();
        }
    }, [token, placa]);

    if(loading){
        return (
            <>
                <Header />

                <main className="container mt-4">

                <div className="text-center">

                    <div
                    className="
                        spinner-border
                        text-primary
                    "
                    />

                    <p className="mt-3">
                    Carregando serviço...
                    </p>

                </div>

                </main>

                <Footer />
            </>
        );
    }

    if(erro || !servico){
        return (
            <>
                <Header />

                <main className="container mt-4">

                <h1>
                    Não Encontrado
                </h1>

                <p>
                    Não foi possível encontrar
                    um serviço com os dados
                    informados.
                </p>

                <div className="form-actions">

                    <Link
                    to="/consultar-servico"
                    className="btn btn-primary"
                    >
                    Fazer Nova Consulta
                    </Link>

                </div>

                </main>

                <Footer />
            </>
        );
    }

    return(
<>

      <Header />

      <main
        id="mainContainer"
        className="container mt-4"
      >

        <h1>
          Status do seu serviço
        </h1>

        <h4 className="mb-4 text-muted">

          {
            servico.tipoServicoDescricao
          }

        </h4>

        <StatusTracker
          etapas={servico.etapas}
          etapaAtualId={
            servico.idEtapaServico
          }
        />

        <div className="form-actions">

          <Link
            to="/consultar-servico"
            className="btn btn-primary"
          >
            Fazer Nova Consulta
          </Link>

        </div>

      </main>

      <Footer />

    </>
    );
}

export default RespostaConsulta;