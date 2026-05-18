import { Navigate } from "react-router-dom";
import { useAuth } from "../context/authContext";
import { hasAnyRole } from "../utils/roleUtils";

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

    if(roles.length > 0 && !hasAnyRole(user, roles)){
        return (<Navigate to="/403" replace/>);
    }

    return children;
}

export default PrivateRoute;