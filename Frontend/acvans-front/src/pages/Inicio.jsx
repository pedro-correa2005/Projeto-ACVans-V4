import { Link } from "react-router-dom";

import Header from "../components/Header";
import Footer from "../components/Footer";

function Inicio() {
    return (
        <div className="min-h-screen flex flex-col bg-gray-50">
            <Header />
            <main className="fex-1">
                <div className="min-h-[80vh] flex items-center justify-center px-6">
                    <div className="text-center">
                        <h1 className="text-5x1 front-bold mb-8 text-gray-800">
                            Bem-vindo ao Sistema
                        </h1>
                        <div className="flex flex-col sm:flex-row gap-4 justify-center">
                            <Link 
                                to="/consultar-servico" 
                                className="bg-sky-600 hover:bg-sky-700 text-white px-8 py-4 rounded-lg text-lg font-semibold transition">
                                Consultar Serviço
                            </Link>
                            <Link
                                to="/cadastros"
                                className="
                                bg-gray-700 
                                hover:bg-gray-800 
                                text-white 
                                px-8 
                                py-4 
                                rounded-lg 
                                text-lg 
                                font-semibold 
                                transition
                                ">
                                    Acessar Sistema
                            </Link>
                        </div>
                    </div>
                </div>
            </main>
            <Footer />
        </div>
    );
}

export default Inicio;