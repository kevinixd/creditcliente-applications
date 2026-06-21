import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/creditcliente-api/api'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default {
  // Cliente API
  obtenerTodosLosClientes() {
    return apiClient.get('/clientes')
  },
  obtenerClientePorId(id) {
    return apiClient.get(`/clientes/${id}`)
  },
  crearCliente(cliente) {
    return apiClient.post('/clientes', cliente)
  },
  actualizarCliente(id, cliente) {
    return apiClient.put(`/clientes/${id}`, cliente)
  },
  eliminarCliente(id) {
    return apiClient.delete(`/clientes/${id}`)
  },

  // Solicitud de Préstamo API
  obtenerTodasLasSolicitudes() {
    return apiClient.get('/prestamo-solicitudes')
  },
  obtenerSolicitudPorId(id) {
    return apiClient.get(`/prestamo-solicitudes/${id}`)
  },
  obtenerSolicitudesDelCliente(clienteId) {
    return apiClient.get(`/prestamo-solicitudes/cliente/${clienteId}`)
  },
  obtenerSolicitudesPendientes() {
    return apiClient.get('/prestamo-solicitudes/pendientes')
  },
  crearSolicitud(solicitud) {
    return apiClient.post('/prestamo-solicitudes', solicitud)
  },
  aprobarSolicitud(id, tasaInteres) {
    return apiClient.post(`/prestamo-solicitudes/${id}/aprobar?tasaInteres=${tasaInteres}`)
  },
  rechazarSolicitud(id, motivo) {
    return apiClient.post(`/prestamo-solicitudes/${id}/rechazar?motivo=${encodeURIComponent(motivo)}`)
  },

  // Préstamo Aprobado API
  obtenerTodosLosPrestamos() {
    return apiClient.get('/prestamos-aprobados')
  },
  obtenerPrestamoPorId(id) {
    return apiClient.get(`/prestamos-aprobados/${id}`)
  },
  obtenerPrestamosDelCliente(clienteId) {
    return apiClient.get(`/prestamos-aprobados/cliente/${clienteId}`)
  },
  obtenerPrestamosActivos() {
    return apiClient.get('/prestamos-aprobados/activos')
  },
  obtenerSaldoPendiente(prestamoId) {
    return apiClient.get(`/prestamos-aprobados/${prestamoId}/saldo`)
  },
  calcularInteres(prestamoId) {
    return apiClient.get(`/prestamos-aprobados/${prestamoId}/interes`)
  },

  // Pago API
  obtenerTodosLosPagos() {
    return apiClient.get('/pagos')
  },
  obtenerPagoPorId(id) {
    return apiClient.get(`/pagos/${id}`)
  },
  obtenerPagosDelPrestamo(prestamoId) {
    return apiClient.get(`/pagos/prestamo/${prestamoId}`)
  },
  registrarPago(pago) {
    return apiClient.post('/pagos', pago)
  },
  obtenerTotalPagado(prestamoId) {
    return apiClient.get(`/pagos/prestamo/${prestamoId}/total-pagado`)
  }
}
