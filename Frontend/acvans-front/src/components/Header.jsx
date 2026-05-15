import { Link } from "react-router-dom";

function Header() {
    return (
        <header className="bg-white shadow-md">
            <div className="max-w-7x1! mx-auto px-6 py-4 flex itmes-center justify-between">
                {/* Logo */}
                <Link to="/" className="flex itmes-center gap-3">
                    <img src="/logo_floco_de_neve.png" alt="Logo AC Vans" className="w-14 h-14 object-contain"/>
                    <div className="flex flex-col">
                        <span className="text-2x1 front-bold text-sky-700">
                            AC VANS
                        </span>

                        <span className="text-xs text-gray-500">
                            AR-CONDICIONADO PARA UTILITÁRIOS
                        </span>
                    </div>
                </Link>

                {/* Menu */}
                <nav>
                    <ul className="flex itmes-center gap-6">
                        <li>
                            <Link to="/" className="hover:text-sky-600 transition">
                                Home
                            </Link>
                        </li>
                        <li>
                            <Link to="/login" className="hover:text-sky-600 transition">
                                Login
                            </Link>
                        </li>
                    </ul>
                </nav>
            </div>
        </header>
    );
}

export default Header;