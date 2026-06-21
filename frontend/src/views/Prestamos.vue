<template>
  <div class="prestamos-container">
    <div class="card">
      <div class="card-header">
        <h2>Gestión de Préstamos Aprobados</h2>
      </div>

      <div v-if="mensaje" :class="['alert', 'alert-' + tipoMensaje]">
        {{ mensaje }}
      </div>

      <div class="card-body">
        <div class="filter-group">
          <button 
            @click="filtro = 'todos'" 
            :class="['btn', filtro === 'todos' ? 'btn-primary' : 'btn-secondary']"
          >
            Todos
          </button>
          <button 
            @click="filtro = 'ACTIVO'" 
            :class="['btn', filtro === 'ACTIVO' ? 'btn-primary' : 'btn-secondary']"
          >
            Activos
          </button>
          <button 
            @click="filtro = 'PAGADO'" 
            :class="['btn', filtro === 'PAGADO' ? 'btn-primary' : 'btn-secondary']"
          >
            Pagados
          </button>
        </div>

        <div v-if="loading" class="loading">
          <span class="spinner"></span>
        </div>

        <div v-else-if="prestamosFiltrados.length === 0" class="alert alert-info">
          No hay préstamos para mostrar.
        </div>

        <div v-else>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Cliente</th>
                <th>Monto Aprobado</th>
                <th>Interés (%)</th>
                <th>Saldo Pendiente</th>
                <th>Estado</th>
                <th>Vencimiento</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="prestamo in prestamosFiltrados" :key="prestamo.id">
                <td>{{ prestamo.id }}</td>
                <td>{{ obtenerNombreCliente(prestamo.clienteId) }}</td>
                <td>Q{{ prestamo.montoAprobado.toFixed(2) }}</td>
                <td>{{ prestamo.tasaInteres }}%</td>
                <td>
                  <strong :style="{ color: prestamo.saldoPendiente > 0 ? '#dc3545' : '#28a745' }">
                    Q{{ prestamo.saldoPendiente.toFixed(2) }}
                  </strong>
                </td>
                <td>
                  <span :class="['badge', 'badge-' + obtenerBadgeEstado(prestamo.estado)]">
                    {{ prestamo.estado }}
                  </span>
                </td>
                <td>{{ formatearFecha(prestamo.fechaVencimiento) }}</td>
                <td>
                  <router-link :to="`/prestamos/${prestamo.id}`" class="btn btn-sm btn-primary">
                    Detalles
                  </router-link>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/api'
import { format } from 'date-fns'
import { es } from 'date-fns/locale'

export default {
  name: 'Prestamos',
  data() {
    return {
      prestamos: [],
      clientes: [],
      loading: true,
      mensaje: '',
      tipoMensaje: 'success',
      filtro: 'todos'
    }
  },
  computed: {
    prestamosFiltrados() {
      if (this.filtro === 'todos') {
        return this.prestamos
      }
      return this.prestamos.filter(p => p.estado === this.filtro)
    }
  },
  mounted() {
    this.cargarDatos()
  },
  methods: {
    async cargarDatos() {
      try {
        this.loading = true
        const [prestamosResp, clientesResp] = await Promise.all([
          apiService.obtenerTodosLosPrestamos(),
          apiService.obtenerTodosLosClientes()
        ])
        this.prestamos = prestamosResp.data
        this.clientes = clientesResp.data
      } catch (error) {
        console.error('Error al cargar préstamos:', error)
        this.mensaje = 'Error al cargar los préstamos'
        this.tipoMensaje = 'danger'
      } finally {
        this.loading = false
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
    }
  }
}
</script>

<style scoped>
.prestamos-container {
  width: 100%;
}

.filter-group {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

table {
  margin-top: 1rem;
}

.btn-sm {
  margin: 0 0.25rem;
}

.badge {
  text-transform: uppercase;
  font-size: 0.75rem;
}

.prestamos-container .card { border: 1px solid #e1e8ee; border-radius: 18px; box-shadow: 0 12px 34px rgba(12,37,64,.08); }
.card-header { padding: 1.6rem 2rem; background: linear-gradient(120deg, #061b34, #0d3c69); border-bottom: 3px solid #d3ae4c; }
.card-header h2 { font-size: 1.3rem; }
.card-body { padding: 1.6rem 2rem 2rem; }
.filter-group { gap: .5rem; padding-bottom: 1.25rem; border-bottom: 1px solid #e3e9ef; }
.filter-group .btn { border-radius: 999px; box-shadow: none; }
.filter-group .btn-primary { background: #092743; }
.filter-group .btn-secondary { background: #edf2f6; border: 1px solid #dce4eb; }
.card-body > div:last-child { overflow-x: auto; }
table { min-width: 900px; }
thead { background: #092743; }
th { padding: 1rem; border-bottom-color: #d3ae4c; font-size: .7rem; text-transform: uppercase; letter-spacing: .06em; }
td { padding: 1rem; color: #46596a; }
tbody tr:hover { background: #f7f9fb; }
.badge { min-width: 86px; padding: .38rem .7rem; border: 0; border-radius: 999px; letter-spacing: .04em; }
.btn-sm { border-radius: 9px; }
.alert { border-radius: 12px; }

@media (max-width: 700px) {
  .card-header, .card-body { padding-left: 1rem; padding-right: 1rem; }
  .filter-group { display: grid; grid-template-columns: repeat(3, 1fr); }
}
</style>
  
