export function hasRole(user, role){
    if(!user){
        return false;
    }

    return user.roles.includes(
        role
    );
}

export function hasAnyRole(user, roles){
    if(!user){
        return false;
    }

    return user.roles.some(role => 
        user.roles.includes(role)
    );
}

export function getHomeByRole(user){
    if(!user){
        return "/";
    }

    if(hasRole(user, "ADMIN")){
        return "/admin/oficinas";
    }

    if(hasRole(user, "GERENTE") || hasRole(user, "FUNCIONARIO")){
        return "/cadastros";
    }

    return "/";
}