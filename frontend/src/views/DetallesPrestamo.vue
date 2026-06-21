<template>
  <div class="detalles-prestamo-container">
    <div v-if="loading" class="loading">
      <span class="spinner"></span>
    </div>

    <div v-else>
      <div class="card">
        <div class="card-header">
          <h2>Detalles del Préstamo #{{ prestamo.id }}</h2>
        </div>

        <div v-if="mensaje" :class="['alert', 'alert-' + tipoMensaje]">
          {{ mensaje }}
        </div>

        <div class="card-body">
          <div class="grid grid-cols-2">
            <div>
              <h3>Información del Préstamo</h3>
              <p><strong>Cliente:</strong> {{ obtenerNombreCliente(prestamo.clienteId) }}</p>
              <p><strong>Monto Aprobado:</strong> Q{{ prestamo.montoAprobado.toFixed(2) }}</p>
              <p><strong>Tasa de Interés:</strong> {{ prestamo.tasaInteres }}%</p>
              <p><strong>Interés Total:</strong> Q{{ calcularInteres().toFixed(2) }}</p>
              <p><strong>Total a Pagar:</strong> Q{{ (prestamo.montoAprobado + calcularInteres()).toFixed(2) }}</p>
              <p><strong>Plazo:</strong> {{ prestamo.plazoDias }} días</p>
              <p><strong>Fecha de Aprobación:</strong> {{ formatearFecha(prestamo.fechaAprobacion) }}</p>
              <p><strong>Fecha de Vencimiento:</strong> {{ formatearFecha(prestamo.fechaVencimiento) }}</p>
              <p>
                <strong>Estado:</strong>
                <span :class="['badge', 'badge-' + obtenerBadgeEstado(prestamo.estado)]">
                  {{ prestamo.estado }}
                </span>
              </p>
            </div>

            <div>
              <h3>Estado de Pago</h3>
              <p><strong>Saldo Pendiente:</strong> <span style="font-size: 1.5rem; color: #dc3545;">Q{{ prestamo.saldoPendiente.toFixed(2) }}</span></p>
              <p><strong>Total Pagado:</strong> Q{{ totalPagado.toFixed(2) }}</p>
              
              <div style="margin-top: 1.5rem;">
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: porcentajePagado + '%' }"></div>
                </div>
                <p style="text-align: center; margin-top: 0.5rem;">{{ porcentajePagado }}% Pagado</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h2>Registrar Pago</h2>
        </div>

        <div class="card-body">
          <form @submit.prevent="registrarPago">
            <div class="grid grid-cols-2">
              <div class="form-group">
                <label for="monto">Monto a Pagar (Q):</label>
                <input 
                  type="number" 
                  id="monto" 
                  v-model.number="nuevoPago.monto" 
                  step="0.01"
                  max=""
                  required 
                  class="form-control"
                >
                <small>Máximo: Q{{ prestamo.saldoPendiente.toFixed(2) }}</small>
              </div>

              <div class="form-group">
                <label for="referencia">Referencia de Pago:</label>
                <input 
                  type="text" 
                  id="referencia" 
                  v-model="nuevoPago.referencia" 
                  class="form-control"
                >
              </div>
            </div>

            <div class="card-footer">
              <button 
                type="submit" 
                class="btn btn-primary" 
                :disabled="guardandoPago || prestamo.saldoPendiente <= 0"
              >
                <span v-if="!guardandoPago">Registrar Pago</span>
                <span v-else>Registrando...</span>
              </button>
            </div>
          </form>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h2>Historial de Pagos</h2>
        </div>

        <div class="card-body">
          <div v-if="pagosLoading" class="loading">
            <span class="spinner"></span>
          </div>

          <div v-else-if="pagos.length === 0" class="alert alert-info">
            No hay pagos registrados para este préstamo.
          </div>

          <div v-else>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Monto</th>
                  <th>Fecha de Pago</th>
                  <th>Referencia</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="pago in pagos" :key="pago.id">
                  <td>{{ pago.id }}</td>
                  <td>Q{{ pago.monto.toFixed(2) }}</td>
                  <td>{{ formatearFecha(pago.fechaPago) }}</td>
                  <td>{{ pago.referencia || '-' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div class="card-footer">
        <router-link to="/prestamos" class="btn btn-secondary">Volver</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/api'
import { format } from 'date-fns'
import { es } from 'date-fns/locale'

export default {
  name: 'DetallesPrestamo',
  data() {
    return {
      prestamo: null,
      pagos: [],
      totalPagado: 0,
      loading: true,
      pagosLoading: false,
      mensaje: '',
      tipoMensaje: 'success',
      guardandoPago: false,
      nuevoPago: {
        prestamoAprobadoId: null,
        monto: 0,
        referencia: ''
      },
      clientes: []
    }
  },
  computed: {
    porcentajePagado() {
      if (!this.prestamo) return 0
      const total = this.prestamo.montoAprobado + (this.prestamo.montoAprobado * this.prestamo.tasaInteres / 100)
      return Math.round((this.totalPagado / total) * 100)
    }
  },
  mounted() {
    this.cargarDetalles()
  },
  methods: {
    async cargarDetalles() {
      try {
        const prestamoId = this.$route.params.id
        const [prestamoResp, pagosResp, clientesResp] = await Promise.all([
          apiService.obtenerPrestamoPorId(prestamoId),
          apiService.obtenerPagosDelPrestamo(prestamoId),
          apiService.obtenerTodosLosClientes()
        ])

        this.prestamo = prestamoResp.data
        this.pagos = pagosResp.data
        this.clientes = clientesResp.data
        this.nuevoPago.prestamoAprobadoId = prestamoId

        // Calcular total pagado
        this.totalPagado = this.pagos.reduce((sum, pago) => sum + pago.monto, 0)
      } catch (error) {
        console.error('Error al cargar detalles del préstamo:', error)
        this.mensaje = 'Error al cargar los detalles del préstamo'
        this.tipoMensaje = 'danger'
      } finally {
        this.loading = false
      }
    },
    async registrarPago() {
      if (this.nuevoPago.monto <= 0) {
        this.mensaje = 'El monto debe ser mayor a cero'
        this.tipoMensaje = 'danger'
        return
      }

      if (this.nuevoPago.monto > this.prestamo.saldoPendiente) {
        this.mensaje = `El monto no puede exceder el saldo pendiente (Q${this.prestamo.saldoPendiente.toFixed(2)})`
        this.tipoMensaje = 'danger'
        return
      }

      try {
        this.guardandoPago = true
        await apiService.registrarPago(this.nuevoPago)
        this.mensaje = 'Pago registrado correctamente'
        this.tipoMensaje = 'success'
        this.nuevoPago.monto = 0
        this.nuevoPago.referencia = ''
        this.cargarDetalles()
      } catch (error) {
        console.error('Error al registrar pago:', error)
        this.mensaje = 'Error al registrar el pago'
        this.tipoMensaje = 'danger'
      } finally {
        this.guardandoPago = false
      }
    },
    obtenerNombreCliente(clienteId) {
      const cliente = this.clientes.find(c => c.id === clienteId)
      return cliente ? `${cliente.nombre} ${cliente.apellido}` : 'Desconocido'
    },
    obtenerBadgeEstado(estado) {
      const badges = {
        'ACTIVO': 'info',
        'PAGADO': 'success',
        'CANCELADO': 'danger'
      }
      return badges[estado] || 'secondary'
    },
    formatearFecha(fecha) {
      if (!fecha) return ''
      return format(new Date(fecha), 'dd/MM/yyyy', { locale: es })
    },
    calcularInteres() {
      if (!this.prestamo) return 0
      return this.prestamo.montoAprobado * (this.prestamo.tasaInteres / 100)
    }
  }
}
</script>

<style scoped>
.detalles-prestamo-container {
  width: 100%;
}

.card-footer {
  margin-top: 1.5rem;
  display: flex;
  gap: 1rem;
  justify-content: flex-start;
}

h3 {
  margin-top: 0;
  margin-bottom: 1rem;
  color: #333;
}

p {
  margin-bottom: 0.75rem;
}

small {
  display: block;
  color: #999;
  font-size: 0.8rem;
  margin-top: 0.25rem;
}

.progress-bar {
  width: 100%;
  height: 30px;
  background-color: #e9ecef;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #28a745, #20c997);
  transition: width 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 0.875rem;
}

@media (max-width: 768px) {
  .grid-cols-2 {
    grid-template-columns: 1fr;
  }
}

.detalles-prestamo-container > div > .card { border: 1px solid #e1e8ee; border-radius: 18px; box-shadow: 0 12px 34px rgba(12,37,64,.08); }
.card-header { padding: 1.45rem 2rem; color: #fff; background: linear-gradient(120deg, #061b34, #0d3c69); border-bottom: 3px solid #d3ae4c; }
.card-header h2 { font-size: 1.25rem; }
.card-body { padding: 2rem; }
.grid { gap: 1.5rem; margin-bottom: 0; }
.grid > div { padding: 1.3rem; border: 1px solid #e3e9ef; border-radius: 14px; background: #f9fbfc; }
h3 { color: #092743; padding-bottom: .75rem; border-bottom: 1px solid #dce4eb; font-size: 1rem; }
p { display: flex; justify-content: space-between; gap: 1rem; color: #42576a; }
p strong { color: #173b5c; }
.progress-bar { height: 12px; border-radius: 999px; background: #e4eaf0; }
.progress-fill { border-radius: 999px; background: linear-gradient(90deg, #0d3c69, #d3ae4c); }
.form-control { min-height: 46px; border: 1px solid #ced8e1; border-radius: 10px; background: #fff; }
.form-control:focus { border-color: #a98424; box-shadow: 0 0 0 3px rgba(211,174,76,.16); }
.btn { border-radius: 10px; }
.btn-primary { background: #092743; }
.btn-secondary { background: #fff; border: 1px solid #ccd6df; }
.card-footer { padding: 1.2rem 2rem; background: #f7f9fb; border-top: 1px solid #e3e9ef; }
.card-body > div:last-child { overflow-x: auto; }
table { min-width: 620px; }
thead { background: #092743; }
th { border-bottom-color: #d3ae4c; font-size: .72rem; text-transform: uppercase; letter-spacing: .06em; }
.alert { border-radius: 12px; }

@media (max-width: 768px) {
  .card-header, .card-body { padding-left: 1rem; padding-right: 1rem; }
  .grid > div { padding: 1rem; }
  p { align-items: flex-start; flex-direction: column; gap: .2rem; }
}
</style>
