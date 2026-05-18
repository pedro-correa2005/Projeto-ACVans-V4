import { useState } from "react";
import {
    useLocation,
    useNavigate,
    Navigate
} from "react-router-dom";

import {
    toast
} from "react-toastify";

import Header from "../components/Header";
import Footer from "../components/Footer";

import {
    verificar2FA,
    me
} from "../services/authService";

import {
    useAuth
} from "../context/authContext";

function TwoFactor(){
    const navigate = useNavigate();

    const location = useLocation();

    const {
        authenticated
    } = useAuth();

    const tempToken = location.state?.tempToken;

    const [code, setCode] = useState("");

    const [loading, setLoading] = useState(false);

    if(authenticated || !tempToken){
        return (
            <Navigate to="/login"/>
        );
    }

    async function handleSubmit(e){
        e.preventDefault();

        try{
            setLoading(true);

            await verificar2FA(tempToken, code);

            const usuario = await me();

            window.location.href="/cadastros";
        } catch (error){
            console.error(error);
            toast.error(error.response?.data?.message || "Código inválido");
        }finally{
            setLoading(false);
        }
    }

    function handleOnChange(e){
        let value = e.target.value;
        value = value.replace(/\D/g, "");
        value = value.slice(0,6);
        setCode(value);
    }

    return(
        <>
            <Header />
            <main className="container mt-4">
                <div className="form-wrapper">
                    <h1>Verificação em Dois Fatores</h1>
                    <p className="form-description">
                        Insira o código enviado para o seu Email.
                    </p>
                    <form className="form" onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="code">Código</label>
                            <input type="text" name="code" id="code" className="form-control" maxLength={6} value={code} onChange={handleOnChange}/>
                        </div>
                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary" disabled={loading}>{loading?"Verificando...":"Verificar"}</button>
                        </div>
                    </form>
                </div>
            </main>
            <Footer />
        </>
    );
}

export default TwoFactor;