import { useAuth } from "../context/authContext";
import { Navigate, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { getRoleLabel } from "../utils/roleUtils";

import Header from "../components/Header";
import Footer from "../components/Footer";

function Perfil(){
    const {
        user, logout
    } = useAuth();

    async function handleLogout() {
        await logout();
    }

    return(
        <>
            <Header />
            <main className="container mt-4">
                <div className="form-wrapper">
                    <h1>Meu Perfil</h1>
                    <p className="form-description">
                        Informações da sua conta.
                    </p>
                    <div className="mt-4">
                        <div className="mb-3">
                            <strong>
                                Email:
                            </strong>
                            <div>
                                {user?.email}
                            </div>
                        </div>
                        <div className="mb-3">
                            <strong>
                                Autenticação de dois fatores:
                            </strong>
                            <div>
                                {user?.doisFatores}
                            </div>
                        </div>
                        <div className="mb-3">
                            <strong>
                                Permissões:
                            </strong>
                            <div>
                                {user?.roles?.map(getRoleLabel).join(", ")}
                            </div>
                        </div>
                    </div>
                    <div className="form-actions mt-4">
                        <button className="btn btn-danger" onClick={handleLogout}>
                            Sair da Conta
                        </button>
                    </div>
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default Perfil;