import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FaPlus } from "react-icons/fa";
import { toast } from "react-toastify";
import {arrayMove} from "@dnd-kit/sortable";


import TiposServicoCard from "../components/card/TiposServicoCard";
import TipoServicoForm from "../components/forms/TipoServicoForm.jsx"
import EtapaServicoForm from "../components/forms/EtapaServicoForm.jsx"
import CrudModal from "../components/modal/CrudModal";

import Header from "../components/Header";
import Footer from "../components/Footer";

import { listarTiposServico, criarTipoServico, atualizarTipoServico, deletarTipoServico } from "../services/tiposServicoService.js";
import { listarEtapasServico, criarEtapaServico, atualizarEtapaServico, deletarEtapaServico, atualizarOrdemEtapa } from "../services/etapasServicoService.js";

function ConfigurarServicos() {
    const [showModalTipo, setShowModalTipo] = useState(false);
    const [showModalEtapa, setShowModalEtapa] = useState(false);

    const [loadingSave, setLoadingSave] = useState(false);
    const [editingTipo, setEditingTipo] = useState(null);
    const [deletingTipo, setDeletingTipo] = useState(null);
    const [loadingDeleteTipo, setLoadingDeleteTipo] = useState(false);
    const [editingEtapa, setEditingEtapa] = useState(null);
    const [deletingEtapa, setDeletingEtapa] = useState(null);
    const [loadingDeleteEtapa, setLoadingDeleteEtapa] = useState(false);

    const [listaTipos, setListaTipos] = useState([]);

    const [tipoDescricao, setTipoDescricao] = useState("");

    const [etapaTitulo, setEtapaTitulo] = useState("");
    const [etapaOrdem, setEtapaOrdem] = useState(null);
    const [etapaDescricao, setEtapaDescricao] = useState("");
    const [idTipoServico, setIdTipoServico] = useState(null)

    async function carregar() {
        try {
            const dataTipos = await listarTiposServico();
            const tiposComEtapas = await Promise.all(
                dataTipos.map(async (tipo) => {
                    const etapas = await listarEtapasServico(tipo.id);
                    return { ...tipo, etapas };
                })
            );
            setListaTipos(tiposComEtapas);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        carregar();
    }, [])

    async function salvarTipo() {
        try {
            setLoadingSave(true);
            const descricao = tipoDescricao;
            const tipoServico = { descricao };
            if (editingTipo) {
                await atualizarTipoServico(editingTipo.id, tipoServico);
                toast.success("Tipo de serviço atualizado com sucesso");
            } else {
                await criarTipoServico(tipoServico);
                toast.success("Tipo de serviço criado com sucesso");
            }
            setShowModalTipo(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error("Erro ao salvar tipo de serviço: " + error.response?.data?.message);
        } finally {
            setLoadingSave(false);
        }
    }

    async function salvarEtapa() {
        try {
            setLoadingSave(true);
            const titulo = etapaTitulo;
            const descricao = etapaDescricao;
            const etapaServico = { titulo, descricao };
            if (editingEtapa) {
                await atualizarEtapaServico(editingEtapa.id, etapaServico);
                toast.success("Etapa de serviço atualizada com sucesso");
            } else {
                await criarEtapaServico(etapaServico, idTipoServico);
                toast.success("Etapa de serviço criada com sucesso");
            }
            setShowModalEtapa(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error("Erro ao salvar etapa de serviço: " + error.response?.data?.message);
        } finally {
            setLoadingSave(false);
        }
    }

    function abrirModalTipo() {
        setEditingTipo(null);
        setTipoDescricao("");
        setShowModalTipo(true);
    }

    function abrirModalEtapa(tipo) {
        setEditingEtapa(null);
        setEtapaTitulo("");
        setEtapaDescricao("");
        setEtapaOrdem(null);
        setIdTipoServico(tipo.id);
        setShowModalEtapa(true);
    }

    function editarTipo(tipo) {
        setEditingTipo(tipo);
        setTipoDescricao(tipo.descricao);
        setShowModalTipo(true);
    }

    function editarEtapa(etapa) {
        setEditingEtapa(etapa);
        setEtapaTitulo(etapa.titulo);
        setEtapaDescricao(etapa.descricao);
        setShowModalEtapa(true);
    }

    function confirmarDeletarTipo(tipo) {
        setDeletingTipo(tipo);
    }

    function confirmarDeletarEtapa(etapa) {
        setDeletingEtapa(etapa);
    }

    async function deletarTipo() {
        if (!deletingTipo) {
            return;
        }
        try {
            setLoadingDeleteTipo(true);
            await deletarTipoServico(deletingTipo.id);
            toast.success("Tipo de serviço deletado com sucesso");
            setDeletingTipo(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error(error.response?.data?.message || "Erro ao excluir");
        } finally {
            setLoadingDeleteTipo(false);
        }
    }

    async function deletarEtapa() {
        if (!deletingEtapa) {
            return;
        }
        try {
            setLoadingDeleteEtapa(true);
            await deletarEtapaServico(deletingEtapa.id);
            toast.success("Etapa deletada com sucesso");
            setDeletingEtapa(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error(error.response?.data?.message || "Erro ao excluir");
        } finally {
            setLoadingDeleteEtapa(false);
        }
    }

    async function moverEtapa(

        tipoId,

        oldIndex,

        newIndex,

        idEtapa,

        ordemAnterior,

        ordemProxima

    ) {

        // UPDATE OTIMISTA
        setListaTipos(prev =>
            prev.map(tipo => {

                if (tipo.id !== tipoId) {
                    return tipo;
                }

                return {

                    ...tipo,

                    etapas: arrayMove(
                        tipo.etapas,
                        oldIndex,
                        newIndex
                    )
                };
            })
        );

        try {

            await atualizarOrdemEtapa(

                idEtapa,

                {
                    ordemAnterior,
                    ordemProxima
                }
            );

            await carregar();

        } catch (error) {

            console.error(error);

            toast.error(
                "Erro ao mover etapa"
            );

            await carregar();
        }
    }

    return (
        <>
            <Header />
            <main className="container mt-4">
                <h1>Tipos de Serviço e Etapas</h1>
                <div>
                    <TiposServicoCard tipos={listaTipos} editarTipo={editarTipo} deletarTipo={confirmarDeletarTipo} adicionarEtapa={abrirModalEtapa} editarEtapa={editarEtapa} deletarEtapa={confirmarDeletarEtapa} moverEtapa={moverEtapa} />
                </div>
                <button className="btn btn-primary  " onClick={abrirModalTipo}><FaPlus></FaPlus> Adicionr Tipo</button>
                <Link to="/cadastros" className="btn btn-secondary ms-3">Voltar</Link>
                <CrudModal
                    title={editingTipo ? "Editar Tipo" : "Cadastrar Tipo"}
                    show={showModalTipo}
                    onClose={() => setShowModalTipo(false)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setShowModalTipo(false)}>Cancelar</button>
                            <button className="btn btn-primary" onClick={salvarTipo} disabled={loadingSave}>
                                {loadingSave ? "Salvando..." : "Salvar"}
                            </button>
                        </>
                    }>
                    <TipoServicoForm
                        descricao={tipoDescricao}
                        setDescricao={setTipoDescricao}
                    />
                </CrudModal>
                <CrudModal
                    title="Confirmar exclusão"
                    show={!!deletingTipo}
                    onClose={() => setDeletingTipo(null)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setDeletingTipo(null)}>
                                Cancelar
                            </button>
                            <button className="btn btn-danger" onClick={deletarTipo} disabled={loadingDeleteTipo}>
                                {loadingDeleteTipo ? "Deletando..." : "Deletar"}
                            </button>
                        </>
                    }>
                    <p className="mb-0">Tem certeza que deseja deletar <strong>{deletingTipo?.descricao}</strong>? Todos os serviços cadastrados com este tipo serão apagados, bem como suas etapas. Esta ação não pode ser desfeita.</p>
                </CrudModal>
                <CrudModal
                    title={editingEtapa ? "Editar Etapa" : "Cadastrar Etapa"}
                    show={showModalEtapa}
                    onClose={() => setShowModalEtapa(false)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setShowModalEtapa(false)}>Cancelar</button>
                            <button className="btn btn-primary" onClick={salvarEtapa} disabled={loadingSave}>
                                {loadingSave ? "Salvando..." : "Salvar"}
                            </button>
                        </>
                    }>
                    <EtapaServicoForm
                        titulo={etapaTitulo}
                        setTitulo={setEtapaTitulo}
                        descricao={etapaDescricao}
                        setDescricao={setEtapaDescricao}
                    />
                </CrudModal>
                <CrudModal
                    title="Confirmar exclusão"
                    show={!!deletingEtapa}
                    onClose={() => setDeletingEtapa(null)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setDeletingEtapa(null)}>
                                Cancelar
                            </button>
                            <button className="btn btn-danger" onClick={deletarEtapa} disabled={loadingDeleteEtapa}>
                                {loadingDeleteTipo ? "Deletando..." : "Deletar"}
                            </button>
                        </>
                    }>
                    <p className="mb-0">Tem certeza que deseja deletar <strong>{deletingEtapa?.titulo}</strong>? Todos os serviços cadastrados com esta etapa serão apagados. Esta ação não pode ser desfeita.</p>
                </CrudModal>
            </main>
            <Footer />
        </>
    );
}

export default ConfigurarServicos;