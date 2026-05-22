import { useState, useEffect } from "react";

import DataTable from "../../components/table/DataTable";
import Pagination from "../../components/pagination/Pagination";
import CrudModal from "../../components/modal/CrudModal.jsx";

import { getAuditoria } from "../../services/auditoriaService.js"

import { Link } from "react-router-dom";

import Header from "../../components/Header.jsx";
import Footer from "../../components/Footer.jsx";

function Auditoria() {
    const [page, setPage] = useState(null);
    const [pageNumber, setPageNumber] = useState(0);
    const [loading, setLoading] = useState(false);
    const [sortField, setSortField] = useState("tempo");
    const [sortDirection, setSortDirection] = useState("desc");
    const [showModal, setShowModal] = useState(false);
    const [detalhesAntes, setDetalhesAntes] = useState([]);
    const [detalhesDepois, setDetalhesDepois] = useState([]);
    const [colunasModal, setColunasModal] = useState(null);


    async function carregar() {
        try {
            const data = await getAuditoria(pageNumber, 20, `${sortField},${sortDirection}`);
            setPage(data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        carregar();
    }, [pageNumber, sortField, sortDirection]);

    async function handleSort(field) {
        if (sortField === field) {
            setSortDirection(sortDirection === "asc" ? "desc" : "asc");
        } else {
            setSortField(field);
            setSortDirection("asc");
        }
        setPageNumber(0);
    }

    function verDetalhes(detalhes) {
        setColunasModal(Object.keys(detalhes.antes).map(chave => ({ key: chave, label:chave })));
        setDetalhesAntes([detalhes.antes]);
        setDetalhesDepois([detalhes.depois]);
        setShowModal(true);
    }

    return (
        <>
            <Header />
            <main className="container mt-4">
                <h1 className="mb-4">Auditoria</h1>
                <DataTable
                    page={page}
                    columns={[
                        {
                            key: "acao",
                            label: "Ação"
                        },
                        {
                            key: "usuario",
                            label: "Usuário"
                        },
                        {
                            key: "entidade",
                            label: "Entidade"
                        },
                        {
                            key: "tempo",
                            label: "Data e hora",
                            render: (value) => {
                                const data = new Date(value);
                                return data.toLocaleDateString('pt-BR', {
                                    day: '2-digit',
                                    month: '2-digit',
                                    year: 'numeric',
                                    hour: '2-digit',
                                    minute: '2-digit'
                                });
                            }
                        },
                        {
                            key: "enderecoIp",
                            label: "IP"
                        }
                    ]}
                    actions={(linha) => (
                        (linha.detalhes != null) && (
                            <button className="btn btn-sm btn-secondary" onClick={() => verDetalhes(linha.detalhes)}>Detalhes</button>)
                    )}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    onSort={handleSort}
                />
                <Link to="/cadastros" className="btn btn-secondary mt-3">Voltar</Link>
                <Pagination page={page} onPageChange={setPageNumber} />
                <CrudModal
                    title="Detalhes"
                    show={showModal}
                    onClose={() => setShowModal(false)}
                    size={"xl"}
                >
                    <h3>Antes:</h3>
                    <DataTable
                        page={detalhesAntes}
                        columns={colunasModal}
                        />
                    <h3>Depois:</h3>
                    <DataTable
                        page={detalhesDepois}
                        columns={colunasModal}
                    />
                </CrudModal>
            </main>
            <Footer />
        </>
    )
}

export default Auditoria;