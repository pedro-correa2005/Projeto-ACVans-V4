import { Link } from "react-router-dom";

function Header() {
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

        <li>
          <Link to="/cadastros">
            Sistema
          </Link>
        </li>

        <li>
          <Link to="/login">
            Login
          </Link>
        </li>

      </ul>

    </header>
  );
}

export default Header;