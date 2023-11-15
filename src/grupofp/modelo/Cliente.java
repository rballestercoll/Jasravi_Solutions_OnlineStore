package grupofp.modelo;

public abstract class Cliente {
    private int id;
    private String nombre;
    private String domicilio;
    private String nif;
    private String email;


    public Cliente(int id, String nombre, String domicilio, String nif, String email) {
        this.id = id;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                "nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", nif='" + nif + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    /*implementar métodos abstractos:
    - public abstract String tipoCliente();

    - public abstract float calcAnual(); //no entiendo este método

    - public abstract float descuentoEnv();
    Las clases hijas implementan métodos abstractos

     */
    public abstract float calcAnual();
    public abstract String tipoCliente();

    public abstract float descuentoEnv();

}
