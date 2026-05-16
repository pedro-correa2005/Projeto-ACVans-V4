import { Link } from "react-router-dom";

import Header from "../components/Header";
import Footer from "../components/Footer";

function Inicio() {

  return (
    <div>

      <Header />

      <main>

        <div
          className="
            min-h-screen
            flex
            justify-center
            items-center
          "
        >

          <div className="text-center">

            <h1 className="text-5xl font-bold mb-8">
              Bem-vindo ao Sistema
            </h1>

            <div className="flex gap-4 justify-center flex-wrap">

              <Link
                to="/consultar-servico"
                className="btn btn-primary"
              >
                Consultar Serviço
              </Link>

              <Link
                to="/cadastros"
                className="btn btn-secondary"
              >
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