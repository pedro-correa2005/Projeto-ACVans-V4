function PlacaInput({
  value,
  onChange,
  required = false,
  id = "placa",
  label = "Placa",
  placeholder = "ABC1234 ou ABC1D23"
}) {

  function handleChange(e) {

    let value = e.target.value.toUpperCase();

    value = value.replace(/[^A-Z0-9]/g, "");

    value = value.slice(0, 7);

    onChange(value);
  }

  return (
    <div className="form-group">

      <label htmlFor={id}>
        {label}
      </label>

      <input
        type="text"
        id={id}
        className="form-control"
        placeholder={placeholder}
        autoComplete="off"
        required={required}
        value={value}
        onChange={handleChange}
      />

    </div>
  );
}

export default PlacaInput;