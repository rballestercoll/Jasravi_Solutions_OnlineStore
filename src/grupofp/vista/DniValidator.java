package grupofp.vista;
public class DniValidator {
    public void validateDni(String dni) throws DniLengthException {
        if (dni.length() > 9) {
            throw new DniLengthException("El DNI no puede tener más de 9 caracteres.");
        }
    }
}
