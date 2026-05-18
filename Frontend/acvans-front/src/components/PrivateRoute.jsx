import { Navigate } from "react-router-dom";
import { useAuth } from "../context/authContext";

function PrivateRoute({
    children,
    roles = []
}) {
    const {
        authenticated,
        loading,
        user
    } = useAuth();

    if(loading){
        return (
            <div className = "d-flex justify-content-center align-items-center min-vh-100">
                <div className = "spinner-border text-primary"/>
            </div>
        );
    }
    
    if(!authenticated){
        return(
            <Navigate to="/login" replace/>
        );
    }

    if(roles.length > 0){
        const possuiPermissao = roles.some(role =>
            user.roles.includes(role)
        );

        if(!possuiPermissao){
            return (
                <Navigate to="/403" replace/>
            );
        }
    }

    return children;
}

export default PrivateRoute;