module Jasravi.Solutions.OnlineStore {
    requires javafx.controls;
   // requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires jakarta.persistence;
    requires java.naming;

    opens grupofp.vista to javafx.fxml;
    opens grupofp.modelo to org.hibernate.orm.core;

    exports grupofp.vista;
}