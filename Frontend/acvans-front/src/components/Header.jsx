import { Link } from "react-router-dom";
import { useAuth } from "../context/authContext";
import { hasRole } from "../utils/roleUtils";

function Header() {
  const {
    authenticated,
    user,
    logout
  } = useAuth();
  return (
    <header className="navbar">

      <div className="navbar-logo">

        <Link to="/">
          <img
            src="/logo_floco_de_neve.png"
            alt="Logo AC Vans"
          />

          <div className="logo-text-wrapper">
            AC VANS

            <span>
              AR CONDICIONADO PARA UTILITÁRIOS
            </span>
          </div>
        </Link>

      </div>

      <ul className="navbar-links">

        <li>
          <Link to="/">
            Home
          </Link>
        </li>
        {
          hasRole(user, "GERENTE") || hasRole(user, "FUNCIONARIO") && (
            <li>
              <Link to="/cadastros">
                Sistema
              </Link>
            </li>
          )
        }
        {
          hasRole(user, "GERENTE") && (
            <li>
              <Link to="/relatorios">
                Relatórios
              </Link>
            </li>
          )
        }
        {
          hasRole(user, "ADMIN") && (
            <li>
              <Link to="/admin/oficinas">
                Admin
              </Link>
            </li>
          )
        }
        {
          !authenticated &&(
            <li>
              <Link to="/login">
                Login
              </Link>
            </li>
          )
        }
        {
          authenticated && (
            <li>
              <Link to="/perfil">
                Usuário
              </Link>
            </li>
          )
        }
      </ul>

    </header>
  );
}

export default Header;