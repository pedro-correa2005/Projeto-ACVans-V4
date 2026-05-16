import { Link } from "react-router-dom";

import Header from "../components/Header";
import Footer from "../components/Footer";

function Inicio() {

  return (
    <div>
      <Header />
      <main className="container mt-4">
        <div className="min-vh-100 d-flex justify-content-center align-items-center">
          <div className="text-center">
            <h1 className="display-4 mb-4">Bem-vindo ao Sistema</h1>
            <div className="d-grid gap-3 d-sm-block">
              <Link to="/consultar-servico" className="btn btn-primary btn-lg">
              Consultar Serviço
              </Link>
              <Link to="/cadastros" className="btn btn-secondary btn-lg">
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