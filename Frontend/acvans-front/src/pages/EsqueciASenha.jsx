import { useState } from "react"; 

import { toast } from "react-toastify";

import Header from "../components/Header";
import Footer from "../components/Footer";

import { forgotPassword } from "../services/authService";

function EsqueciASenha(){
    const [email, setEmail] = useState("");

    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {
        e.preventDefault();

        try{
            setLoading(true);
            const response = await forgotPassword(email);
            toast.success(response);
            setEmail("");
        }catch(error){
            console.error(error);
            toast.error("Erro ao solicitar redefinição de senha.");
        }finally{
            setLoading(false);
        }
    }

    return (
        <>
            <Header />
            <main className="container mt-4">
                <div className="form-wrapper">
                    <h1>Esqueci a Senha</h1>
                    <p className="form-description">Insira seu Email para receber link de redefinição de senha.</p>
                    <form className="form" onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input type="email" name="email" id="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} />
                        </div>
                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary" disabled={loading}>{loading?"Eviando...":"Enviar"}</button>
                        </div>
                    </form>
                </div>
            </main>
            <Footer />
        </>
    );
}

export default EsqueciASenha;