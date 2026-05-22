import { useState, useEffect } from "react";

import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

import { FaChartPie } from "react-icons/fa";

import Header from "../../components/Header";
import Footer from "../../components/Footer";

import { getMediaEtapas } from "../../services/relatoriosService"
import { toast } from "react-toastify";

function Relatorios() {
    const [ano, setAno] = useState(new Date().getFullYear());
    const [mes, setMes] = useState(new Date().getMonth() + 1);
    const [mediaEtapas, setMediaEtapas] = useState(null);
    const [loading, setLoading] = useState(false);

    async function carregar() {
        setLoading(true);
        try {
            const data = await getMediaEtapas(mes, ano);
            setMediaEtapas(data);
        } catch (error) {
            console.error(error);
            toast.error("Erro ao  buscar dados");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        carregar();
    }, [mes, ano]);

    return (
        <>
            <Header />
            <main className="container mt-4">
                <h1>Relatórios Gerenciais</h1>
                <p>Análise de performance de etapas dos serviços da oficina</p>
                <div className="row mb-3">
                    <div className="col-md-3">
                        <label htmlFor="mesSelect" className="form-label">Mês</label>
                        <select id="mesSelect" className="form-select"
                            value={mes} onChange={e => setMes(e.target.value)}>
                            <option value="1">Janeiro</option>
                            <option value="2">Fevereiro</option>
                            <option value="3">Março</option>
                            <option value="4">Abril</option>
                            <option value="5">Maio</option>
                            <option value="6">Junho</option>
                            <option value="7">Julho</option>
                            <option value="8">Agosto</option>
                            <option value="9">Setembro</option>
                            <option value="10">Outubro</option>
                            <option value="11">Novembro</option>
                            <option value="12">Dezembro</option>
                        </select>
                    </div>
                    <div className="col-md-3">
                        <label htmlFor="anoInput" className="form-label">Ano</label>
                        <div className="input-group">
                            <button
                                className="btn btn-outline-secondary"
                                type="button"
                                onClick={() => setAno(ano - 1)}
                                disabled={ano <= 2000}
                            >
                                -
                            </button>

                            {/* Input Controlado */}
                            <input
                                type="number"
                                id="anoInput"
                                className="form-control text-center"
                                min={2000}
                                max={new Date().getFullYear()}
                                value={ano}
                                disabled
                            />

                            {/* Botão de Adicionar */}
                            <button
                                className="btn btn-outline-secondary"
                                type="button"
                                onClick={() => setAno(ano + 1)}
                                disabled={ano >= new Date().getFullYear()}
                            >
                                +
                            </button>
                        </div>
                    </div>
                </div>
                <div className="w-100">
                    {(loading) && (<p>Carregando dados...</p>)}
                    {(mediaEtapas != null && !loading) && (
                        <>
                            <h2>Relatórios de tempo</h2>
                            {
                                mediaEtapas.map((tipoServico, i) => (
                                    (tipoServico.etapas.length > 0) && (
                                    <div className="card shadow-sm mb-4" key={tipoServico?.tipoServicoDescricao}>
                                        <div className="card-body">
                                            <h3 className="h5 mb-4"><FaChartPie className="me-2" />{tipoServico?.tipoServicoDescricao}: Tempo em minutos</h3>
                                            <div style={{width: "100%", height: 300}}>
                                                <ResponsiveContainer style={{width: "100%", height: 300}}>
                                                    <BarChart data={tipoServico?.etapas} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                                                        <CartesianGrid strokeDasharray="3 3"/>
                                                        <XAxis dataKey="etapaTitulo"/>
                                                        <YAxis dataKey="mediaMinutos" label={{value: "Tempo (Minutos)", angle: -90, position: "insideLeft"}}/>
                                                        <Tooltip/>
                                                        <Legend/>
                                                        <Bar dataKey="mediaMinutos" name={`${tipoServico.tipoServicoDescricao}: Média (minutos)`}  fill="#0052cc" stroke="#0747a6"/>
                                                    </BarChart>
                                                </ResponsiveContainer>
                                            </div>
                                        </div>
                                    </div>) || (
                                        <p key={tipoServico?.tipoServicoDescricao}>Dados insuficientes para {tipoServico?.tipoServicoDescricao}.</p>
                                    )
                                ))
                            }
                        </>
                    )}
                </div>
            </main>
            <Footer />
        </>
    )
}

export default Relatorios;