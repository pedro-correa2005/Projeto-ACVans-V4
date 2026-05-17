import {
    createContext,
    useContext,
    useEffect,
    useState
} from "react";

import {
    login as loginService,
    logout as logoutService,
    me
} from "../services/authService";

const AuthContext = createContext();

export function AuthProvider({
    children
}){
    const [user, setUser] = useState(null);

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function carregarUsuario(){
            try{
                const usuairo = await me();
                sutUser(usuario);
            } catch (error) {
                setUser(null);
            } finally { 
                setLoading(false);
            }
        }
        carregarUsuario();
    }, []);

    async function login(email, senha){
        const data = await loginService(email, senha);
        setUser(data.usuario);
        return data;
    }

    async function logout(){
        await logoutSercice();
        setUser(null);
    }

    const authenticated = !!user;

    return (
        <AuthContext.Provider
            value={{
                user,
                loading,
                authenticated,
                login,
                logout
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth(){
    return useContext(
        AuthContext
    );
}