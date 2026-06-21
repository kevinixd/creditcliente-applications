# Diagrama Entidad-Relación (ER)

## Descripción General

El sistema de gestión de préstamos está compuesto por 4 entidades principales que mantienen relaciones entre clientes, solicitudes de préstamo, préstamos aprobados y pagos.

## Diagrama ER

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│                        CLIENTES                                 │
│                   ┌──────────────────┐                         │
│                   │  id (PK)         │                         │
│                   │  nombre          │                         │
│                   │  apellido        │                         │
│                   │  numeroIdent. (U)│                         │
│                   │  fechaNacimiento │                         │
│                   │  direccion       │                         │
│                   │  correo (U)      │                         │
│                   │  telefono        │                         │
│                   │  fechaRegistro   │                         │
│                   └──────────────────┘                         │
│                           │                                    │
│          ┌────────────────┼────────────────┐                  │
│          │ 1              │              1 │                  │
│      (1:N)                 │           (1:N)                  │
│          │                 │                │                  │
│          ▼                 ▼                ▼                  │
│ ┌─────────────────┐   ┌──────────────────────────┐           │
│ │ PREST_SOLICITUD │   │  PRESTAMOS_APROBADOS    │           │
│ ├─────────────────┤   ├──────────────────────────┤           │
│ │ id (PK)         │   │ id (PK)                  │           │
│ │ cliente_id (FK) │   │ cliente_id (FK)          │           │
│ │ monto_solicit.  │   │ monto_aprobado           │           │
│ │ plazo_dias      │   │ plazo_dias               │           │
│ │ detalles        │   │ tasa_interes             │           │
│ │ estado          │   │ fecha_aprobacion         │           │
│ │ fecha_solicitud │   │ fecha_vencimiento        │           │
│ │ fecha_revision  │   │ estado                   │           │
│ │ motivo_rechazo  │   │                          │           │
│ └─────────────────┘   └──────────────────────────┘           │
│                               │                               │
│                         (1:N) │                               │
│                               │                               │
│                               ▼                               │
│                        ┌─────────────┐                        │
│                        │    PAGOS    │                        │
│                        ├─────────────┤                        │
│                        │ id (PK)     │                        │
│                        │ prestamo_id │                        │
│                        │    (FK)     │                        │
│                        │ monto       │                        │
│                        │ fechaPago   │                        │
│                        │ referencia  │                        │
│                        └─────────────┘                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

```

## Descripción de Entidades

### 1. CLIENTES
Almacena información de los clientes del banco.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (auto-increment) |
| nombre | NVARCHAR(100) | Nombre del cliente |
| apellido | NVARCHAR(100) | Apellido del cliente |
| numero_identificacion | NVARCHAR(20) | Cédula/Documento (UNIQUE) |
| fecha_nacimiento | DATE | Fecha de nacimiento |
| direccion | NVARCHAR(255) | Dirección del cliente |
| correo_electronico | NVARCHAR(100) | Email (UNIQUE) |
| telefono | NVARCHAR(20) | Número de teléfono |
| fecha_registro | DATE | Fecha de registro en el sistema |

### 2. PRESTAMO_SOLICITUDES
Registra todas las solicitudes de préstamo realizadas por clientes.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (auto-increment) |
| cliente_id | BIGINT | Referencia al cliente (FK) |
| monto_solicitado | DECIMAL(18,2) | Monto solicitado en quetzales |
| plazo_dias | INT | Plazo solicitado en días |
| detalles | NVARCHAR(500) | Descripción adicional |
| estado | NVARCHAR(20) | EN_PROCESO, APROBADO o RECHAZADO |
| fecha_solicitud | DATE | Fecha de creación de la solicitud |
| fecha_revision | DATE | Fecha de revisión/aprobación/rechazo |
| motivo_rechazo | NVARCHAR(500) | Razón del rechazo (si aplica) |

**Relaciones:**
- FK: cliente_id → CLIENTES(id) [CASCADE DELETE]

### 3. PRESTAMOS_APROBADOS
Registra los préstamos que han sido aprobados.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (auto-increment) |
| cliente_id | BIGINT | Referencia al cliente (FK) |
| monto_aprobado | DECIMAL(18,2) | Monto aprobado |
| plazo_dias | INT | Plazo en días |
| tasa_interes | DECIMAL(5,2) | Tasa de interés anual en porcentaje |
| fecha_aprobacion | DATE | Fecha de aprobación |
| fecha_vencimiento | DATE | Fecha de vencimiento del préstamo |
| estado | NVARCHAR(20) | ACTIVO, PAGADO o CANCELADO |

**Relaciones:**
- FK: cliente_id → CLIENTES(id) [CASCADE DELETE]
- Referencia 1:N → PAGOS(prestamo_aprobado_id)

**Cálculos:**
- Interés Total = monto_aprobado × (tasa_interes / 100)
- Total a Pagar = monto_aprobado + Interés Total
- Saldo Pendiente = Total a Pagar - Suma(Pagos)

### 4. PAGOS
Registra todos los pagos realizados para los préstamos.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (auto-increment) |
| prestamo_aprobado_id | BIGINT | Referencia al préstamo (FK) |
| monto | DECIMAL(18,2) | Monto pagado |
| fecha_pago | DATE | Fecha del pago |
| referencia | NVARCHAR(100) | Referencia de transacción |

**Relaciones:**
- FK: prestamo_aprobado_id → PRESTAMOS_APROBADOS(id) [CASCADE DELETE]

## Índices

```sql
-- CLIENTES
CREATE INDEX idx_cliente_numero_id ON clientes(numero_identificacion);
CREATE INDEX idx_cliente_correo ON clientes(correo_electronico);

-- PRESTAMO_SOLICITUDES
CREATE INDEX idx_prestamo_solicitud_cliente ON prestamo_solicitudes(cliente_id);
CREATE INDEX idx_prestamo_solicitud_estado ON prestamo_solicitudes(estado);

-- PRESTAMOS_APROBADOS
CREATE INDEX idx_prestamo_aprobado_cliente ON prestamos_aprobados(cliente_id);
CREATE INDEX idx_prestamo_aprobado_estado ON prestamos_aprobados(estado);

-- PAGOS
CREATE INDEX idx_pago_prestamo ON pagos(prestamo_aprobado_id);
```