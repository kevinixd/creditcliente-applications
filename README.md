# Sistema de Gestión de Préstamos - Crédito Hipotecario Nacional

Sistema web completo para gestionar préstamos bancarios desarrollado con Spring Boot, VueJS y H2 Database.

## Características

### Gestión de Clientes
- Crear nuevos clientes con información completa
- Editar información de clientes existentes
- Eliminar clientes y sus solicitudes asociadas
- Listar todos los clientes registrados

### Solicitud de Préstamos
- Crear nuevas solicitudes de préstamo
- Ver todas las solicitudes pendientes
- Aprobar solicitudes y crear préstamos
- Rechazar solicitudes con motivos documentados
- Filtrar por estado de solicitud

### Gestión de Préstamos Aprobados
- Ver préstamos activos y pagados
- Calcular automáticamente intereses
- Visualizar saldo pendiente
- Detalles completos del préstamo

### Registro de Pagos
- Registrar pagos en efectivo
- Generar historial de pagos
- Calcular saldo pendiente automáticamente
- Actualizar estado del préstamo cuando está completamente pagado

### Dashboard
- Acciones rápidas para operaciones comunes

## Instalación y Ejecución con Docker Compose

### 1. Clonar el repositorio
```bash
git clone https://github.com/kevinixd/creditcliente-applications.git
cd creditcliente-applications
```

### 2. Construir e iniciar los contenedores
```bash
docker-compose up --build
```

### 4. Acceder a la aplicación

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/creditcliente-api/api
- **H2 Console**: http://localhost:8080/creditcliente-api/h2-console
  - Usuario: `sa`
  - Contraseña: (dejar vacío)

### 5. Detener los contenedores
```bash
docker-compose down
```

## Instalación Local

### Backend

1. **Compilar y ejecutar**
   ```bash
   # Opción 1: Con Maven instalado globalmente
   mvn clean install
   mvn spring-boot:run
   
   # Opción 2: Con Maven Wrapper (sin necesidad de Maven instalado)
   # En Windows:
   mvnw.cmd clean install
   mvnw.cmd spring-boot:run
   
   # En Linux/Mac:
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   
   El backend estará disponible en: `http://localhost:8080/creditcliente-api/api`

### Frontend

1. **Instalar dependencias**
   ```bash
   cd frontend
   npm install
   ```

3. **Ejecutar servidor de desarrollo**
   ```bash
   npm run dev
   ```
   
   El frontend estará disponible en: `http://localhost:5173`

4. **Construir para producción**
   ```bash
   npm run build
   ```

## API Endpoints

### Clientes
- `GET /api/clientes` - Obtener todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `POST /api/clientes` - Crear nuevo cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

### Solicitudes de Préstamo
- `GET /api/prestamo-solicitudes` - Obtener todas las solicitudes
- `GET /api/prestamo-solicitudes/{id}` - Obtener solicitud por ID
- `GET /api/prestamo-solicitudes/cliente/{clienteId}` - Solicitudes de un cliente
- `GET /api/prestamo-solicitudes/pendientes` - Obtener solicitudes pendientes
- `POST /api/prestamo-solicitudes` - Crear nueva solicitud
- `POST /api/prestamo-solicitudes/{id}/aprobar?tasaInteres={tasa}` - Aprobar solicitud
- `POST /api/prestamo-solicitudes/{id}/rechazar?motivo={motivo}` - Rechazar solicitud

### Préstamos Aprobados
- `GET /api/prestamos-aprobados` - Obtener todos los préstamos
- `GET /api/prestamos-aprobados/{id}` - Obtener préstamo por ID
- `GET /api/prestamos-aprobados/cliente/{clienteId}` - Préstamos de un cliente
- `GET /api/prestamos-aprobados/activos` - Obtener préstamos activos
- `GET /api/prestamos-aprobados/{id}/saldo` - Obtener saldo pendiente
- `GET /api/prestamos-aprobados/{id}/interes` - Calcular interés

### Pagos
- `GET /api/pagos` - Obtener todos los pagos
- `GET /api/pagos/{id}` - Obtener pago por ID
- `GET /api/pagos/prestamo/{prestamoId}` - Pagos de un préstamo
- `POST /api/pagos` - Registrar nuevo pago
- `GET /api/pagos/prestamo/{prestamoId}/total-pagado` - Total pagado

## Base de Datos

### Tablas Principales

**clientes**
- id (PK)
- nombre
- apellido
- numero_identificacion (UNIQUE)
- fecha_nacimiento
- direccion
- correo_electronico (UNIQUE)
- telefono
- fecha_registro

**prestamo_solicitudes**
- id (PK)
- cliente_id (FK)
- monto_solicitado
- plazo_dias
- detalles
- estado (EN_PROCESO, APROBADO, RECHAZADO)
- fecha_solicitud
- fecha_revision
- motivo_rechazo

**prestamos_aprobados**
- id (PK)
- cliente_id (FK)
- monto_aprobado
- plazo_dias
- tasa_interes
- fecha_aprobacion
- fecha_vencimiento
- estado (ACTIVO, PAGADO, CANCELADO)

**pagos**
- id (PK)
- prestamo_aprobado_id (FK)
- monto
- fecha_pago
- referencia

## Flujo de Negocio

1. **Crear Cliente** → Registrar datos personales y contacto
2. **Solicitar Préstamo** → Cliente solicita un monto con plazo
3. **Revisar Solicitud** → Aprobar o rechazar con motivo si aplica
4. **Crear Préstamo** → Generar préstamo aprobado con tasa de interés
5. **Registrar Pagos** → Cliente realiza pagos hacia el préstamo
6. **Calcular Saldo** → Sistema calcula automáticamente el saldo pendiente
7. **Marcar Pagado** → Cuando saldo llega a cero, préstamo se marca como PAGADO