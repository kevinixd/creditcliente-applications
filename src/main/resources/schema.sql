-- Script para inicializar la base de datos con H2
-- Compatible con H2 Database

-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    correo_electronico VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    fecha_registro DATE NOT NULL DEFAULT CURRENT_DATE()
);

CREATE INDEX IF NOT EXISTS idx_cliente_numero_id ON clientes (numero_identificacion);
CREATE INDEX IF NOT EXISTS idx_cliente_correo ON clientes (correo_electronico);

-- Tabla de Solicitudes de Préstamo
CREATE TABLE IF NOT EXISTS prestamo_solicitudes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    monto_solicitado DECIMAL(18,2) NOT NULL,
    plazo_dias INT NOT NULL,
    detalles VARCHAR(500) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'EN_PROCESO',
    fecha_solicitud DATE NOT NULL DEFAULT CURRENT_DATE(),
    fecha_revision DATE NULL,
    motivo_rechazo VARCHAR(500) NULL,
    CONSTRAINT fk_prestamo_solicitud_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_prestamo_solicitud_cliente ON prestamo_solicitudes (cliente_id);
CREATE INDEX IF NOT EXISTS idx_prestamo_solicitud_estado ON prestamo_solicitudes (estado);

-- Tabla de Préstamos Aprobados
CREATE TABLE IF NOT EXISTS prestamos_aprobados (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    monto_aprobado DECIMAL(18,2) NOT NULL,
    plazo_dias INT NOT NULL,
    tasa_interes DECIMAL(5,2) NOT NULL,
    fecha_aprobacion DATE NOT NULL DEFAULT CURRENT_DATE(),
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_prestamo_aprobado_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_prestamo_aprobado_cliente ON prestamos_aprobados (cliente_id);
CREATE INDEX IF NOT EXISTS idx_prestamo_aprobado_estado ON prestamos_aprobados (estado);

-- Tabla de Pagos
CREATE TABLE IF NOT EXISTS pagos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    prestamo_aprobado_id BIGINT NOT NULL,
    monto DECIMAL(18,2) NOT NULL,
    fecha_pago DATE NOT NULL DEFAULT CURRENT_DATE(),
    referencia VARCHAR(100) NULL,
    CONSTRAINT fk_pago_prestamo FOREIGN KEY (prestamo_aprobado_id) REFERENCES prestamos_aprobados(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_pago_prestamo ON pagos (prestamo_aprobado_id);
