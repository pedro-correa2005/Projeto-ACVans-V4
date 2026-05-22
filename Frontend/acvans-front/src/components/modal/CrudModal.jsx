function CrudModal({
    title,
    show,
    onClose,
    children,
    footer,
    size = "md"
}){
    if(!show){
        return null;
    }
     const sizeClass = {
        sm: "modal-sm",
        md: "",
        lg: "modal-lg",
        xl: "modal-xl"
    };

    return (
        <div className="modal fade show" style={{display:"block"}}>
            <div className={`modal-dialog ${sizeClass[size]}`}>
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">{title}</h5>
                        <button type="button" className="btn-close" onClick={onClose}/>
                    </div>
                    <div className="modal-body">{children}</div>
                    {
                        footer && (
                            <div className="modal-footer">{footer}</div>
                        )
                    }
                </div>
            </div>
        </div>
    );
}

export default CrudModal;