function CelularInput({
    value, onChange, id = "celular", label = "Celular", placeholder = "(##) 9########"
}) {
    function handleChange(e) {
        let value = e.target.value;
        value = value.replace(/\D/g, "");
        value = value.slice(0, 11);
        if (value.length > 0) {
            value = value.replace(/^(\d{0,2})(\d{0,9}).*/, (_, ddd, numero) => {
                if (!numero) return `(${ddd}`;
                return `(${ddd}) ${numero}`;
            });
        }
        onChange(value);
    }

    return(
        <div className="form-group">
            <label htmlFor={id} className="form-label d-block">{label}</label>
            <input
                type="text"
                id={id}
                className="form-control"
                placeholder={placeholder}
                autoComplete="off"
                value={value}
                onChange={handleChange}
            />
        </div>
    )
}

export default CelularInput;