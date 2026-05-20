import { useState, useEffect } from "react";

import DataTable from "../../components/table/DataTable";
import SearchBar from "../../components/search/SearchBar";
import CrudModal from "../../components/modal/CrudModal";
import Pagination from "../../components/pagination/Pagination";

import UsuarioForm from "../../components/forms/UsuarioForm";

import { listarUsuarios, criarUsuario, atualizarRoles, deletarUsuario } from "../../services/usuarioService";

import { toast } from "react-toastify";

import { Link, useParams } from "react-router-dom";

import Header from "../../components/Header";
import Footer from "../../components/Footer";

function Usuarios(){
    const { idOficina } = useParams();
    const[termoInput, setTermoInput] = useState("");
    const [termo, setTermo] = useState("");
    const [page, setPage] = useState(null);
    const [pageNumber, setPageNumber] = useState(0);
    const[showModal, setShowModal] = useState(false);
    const [loadingSave, setLoadingSave] = useState(false);
    const [editingUsuario, setEditingUsuario] = useState(null);
    const[deletingUsuario, setDeletingUsuario] = useState(null);
    const[loadingDelete, setLoadingDelete] = useState(false);
    const [sortField, setSortField] = useState("id");
    const [sortDirection,setSortDirection] = useState("desc");
    
    const [email, setEmail] = useState("");
    const [doisFatores, setDoisFatores] = useState(false);
    const [roles, setRoles] = useState([]);

    async function carregar(){
        try{
            const data = await listarUsuarios(idOficina, termo, pageNumber, 10, `${sortField},${sortDirection}`);
            setPage(data);
        }catch(error){
            console.error(error);
        }
    }

    useEffect(() => {
        carregar();
    }, [termo, pageNumber, sortField, sortDirection]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setPageNumber(0);
            setTermo(termoInput);
        }, 500);
        return () => {
            clearTimeout(timeout);
        };
    }, [termoInput]);

    async function handleSort(field){
        if(sortField === field){
            setSortDirection(sortDirection === "asc"?"desc":"asc");
        }else{
            setSortField(field);
            setSortDirection("asc");
        }
        setPageNumber(0);
    }

    function abrirModal(){
        setEditingUsuario(null);
        setEmail("");
        setDoisFatores(false);
        setRoles([]);
        setShowModal(true);
    }

    async function salvar(){
        try{
            setLoadingSave(true);
            const usuario = {email, doisFatores, roles};
            if(editingUsuario){
                await atualizarRoles(editingUsuario.id, usuario);
                toast.success("Usuário atualizado com sucesso");
            }else{
                await criarUsuario(usuario, Number(idOficina));
                toast.success("Usuario criado com sucesso");
            }
            setShowModal(false);
            carregar();
        }catch(error){
            console.error(error);   
            toast.error("Erro ao salvar usuário: " +
            error.response?.data?.message);
        }finally{
            setLoadingSave(false);
        }
    }

    function editar(usuario){
        setEditingUsuario(usuario);
        setEmail(usuario.email);
        setDoisFatores(usuario.doisFatores);
        setRoles(usuario.roles);
        setShowModal(true);
    }

    function confirmarDeletar(usuario){
        setDeletingUsuario(usuario);
    }

    async function deletar(){
        if(!deletingUsuario){
            return;
        }
        try {
            setLoadingDelete(true);
            await deletarUsuario(deletingUsuario.id);
            toast.success("Usuário deletado com sucesso");
            setDeletingUsuario(null);
            carregar();
        } catch (error) {
            console.error(error)
            toast.error(error.response?.data?.message || "Erro ao excluir");
        }finally{
            setLoadingDelete(false);
        }
    }

    return(
        <>
            <Header/>
            <main className="container mt-4">
                <h1 className="mb-4">Usuarios</h1>
                <form action="" className="mb-3">
                    <SearchBar value={termoInput} onChange={setTermoInput} placeholder="Pesquisar por email..."/>
                </form>
                <DataTable
                    page={page}
                    columns={[
                        {
                            key: "email",
                            label:"Email"
                        },
                        {
                            key: "roles",
                            lable:"Permissões"
                        }
                    ]}
                    actions={(usuario) => (
                        <>
                            <button className="btn btn-sm btn-primary" onClick={()=>editar(usuario)}>Editar</button>
                            <button className="btn btn-sm btn-danger" onClick={() => confirmarDeletar(usuario)}>Deletar</button>
                        </>
                    )}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    onSort={handleSort}
                />
                <button className="btn btn-primary" onClick={abrirModal}>Adicionar Usuário</button>
                <Link to="/admin/oficinas" className="btn btn-secondary">Voltar</Link>
                <Pagination page={page} onPageChange={setPageNumber}/>
                <CrudModal
                    title={editingUsuario?"Editar Usuário":"Cadastrar Usuário"}
                    show={showModal}
                    onClose={() => setShowModal(false)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancelar</button>
                            <button className="btn btn-primary" onClick={salvar} disabled={loadingSave}>
                                {loadingSave?"Salvando...":"Salvar"}
                            </button>
                        </>
                    }>
                    <UsuarioForm
                        email={email}
                        setEmail={setEmail}
                        doisFatores={doisFatores}
                        setDoisFatores={setDoisFatores}
                        roles={roles}
                        setRoles={setRoles}
                        editing={editingUsuario}
                    />
                </CrudModal>
                <CrudModal
                    title="Confirmar exclusão"
                    show={!!deletingUsuario}
                    onClose={() => setDeletingUsuario(null)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setDeletingUsuario(null)}>
                                Cancelar
                            </button>
                            <button className="btn btn-danger" onClick={deletar} diabled={loadingDelete}>
                                {loadingDelete?"Deletando...":"Deletar"}
                            </button>
                        </>
                    }>
                    <p className="mb-0">Tem certeza que deseja deletar usuário <strong>{deletingUsuario?.email}</strong>? Esta ação não pode ser desfeita.</p>
                </CrudModal>
            </main>
            <Footer/>
        </>
    )
}

export default Usuarios;