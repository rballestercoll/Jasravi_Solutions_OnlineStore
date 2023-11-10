DROP DATABASE IF EXISTS onlinestore;
CREATE DATABASE IF NOT EXISTS onlinestore DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE onlinestore;

CREATE TABLE IF NOT EXISTS articulo (
	idArticulo int NOT NULL AUTO_INCREMENT,
	descripcion varchar(40) NOT NULL,
	precio float NOT NULL,
	gastosEnvio float NOT NULL,
	tiempoPreparacion int NOT NULL,
	PRIMARY KEY (idArticulo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS cliente (
    nombre varchar(40) NOT NULL,
    domicilio varchar(40) NOT NULL,
    nif varchar(40) NOT NULL,
    email varchar(40) NOT NULL,
    PRIMARY KEY (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS clienteEstandar (
	nombre varchar(40) NOT NULL,
    domicilio varchar(40) NOT NULL,
    nif varchar(40) NOT NULL,
    email_estandar varchar(40) NOT NULL,
    FOREIGN KEY (email_estandar) REFERENCES cliente (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS clientePremium (
	nombre varchar(40) NOT NULL,
    domicilio varchar(40) NOT NULL,
    nif varchar(40) NOT NULL,
    descuento varchar(40) NOT NULL,
    email_premium varchar(40) NOT NULL,
    FOREIGN KEY (email_premium) REFERENCES cliente (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS pedido (
    num_pedido int NOT NULL,
    cantidad int NOT NULL,
    fecha date NOT NULL,
    fk_cliente_estandar varchar(40) NOT NULL,
    fk_cliente_premium varchar(40) NOT NULL,
    fk_articulo int NOT NULL,
    PRIMARY KEY (num_pedido),
    FOREIGN KEY (fk_cliente_estandar) REFERENCES clienteEstandar (email_estandar),
    FOREIGN KEY (fk_cliente_premium) REFERENCES clientePremium (email_premium),
    FOREIGN KEY (fk_articulo) REFERENCES articulo (idArticulo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;