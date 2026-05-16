export function validarPlaca(placa) {

  const antiga =
    /^[A-Z]{3}[0-9]{4}$/;

  const mercosul =
    /^[A-Z]{3}[0-9][A-Z][0-9]{2}$/;

  return (
    antiga.test(placa) ||
    mercosul.test(placa)
  );
}