<template>
  <div class="clientes-container">
    <div class="page-header">
      <div>
        <span class="eyebrow">Directorio institucional</span>
        <h1>Gestión de Clientes</h1>
        <p class="subtitle">Administra los clientes del sistema</p>
      </div>
      <router-link to="/clientes/crear" class="btn btn-primary btn-lg">
        ➕ Agregar Cliente
      </router-link>
    </div>

    <div v-if="mensaje" :class="['alert', 'alert-' + tipoMensaje]">
      <span v-if="tipoMensaje === 'success'">✓</span>
      <span v-else>⚠</span>
      {{ mensaje }}
    </div>

    <div class="card">
      <div v-if="loading" class="loading">
        <span class="spinner"></span>
      </div>

      <div v-else-if="clientes.length === 0" class="empty-state">
        <div class="empty-icon">👤</div>
        <h3>No hay clientes registrados</h3>
        <p>Comienza agregando tu primer cliente al sistema</p>
        <router-link to="/clientes/crear" class="btn btn-primary">
          Crear Primer Cliente
        </router-link>
      </div>

      <div v-else class="card-body table-responsive">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre Completo</th>
              <th>Identificación</th>
              <th>Correo Electrónico</th>
              <th>Teléfono</th>
              <th>Fecha Registro</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="cliente in clientes" :key="cliente.id" class="table-row">
              <td class="table-id">#{{ cliente.id }}</td>
              <td class="table-name">
                <div class="client-name">{{ cliente.nombre }} {{ cliente.apellido }}</div>
              </td>
              <td>{{ cliente.numeroIdentificacion }}</td>
              <td class="table-email">{{ cliente.correoElectronico }}</td>
              <td>{{ cliente.telefono }}</td>
              <td>{{ formatearFecha(cliente.fechaRegistro) }}</td>
              <td class="table-actions">
                <router-link 
                  :to="`/clientes/${cliente.id}/editar`" 
                  class="btn btn-sm btn-secondary"
                  title="Editar cliente"
                >
                  ✏️
                </router-link>
                <button 
                  class="btn btn-sm btn-danger" 
                  @click="eliminarCliente(cliente.id)"
                  title="Eliminar cliente"
                >
                  🗑️
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/api'
import { format } from 'date-fns'
import { es } from 'date-fns/locale'

export default {
  name: 'Clientes',
  data() {
    return {
      clientes: [],
      loading: true,
      mensaje: '',
      tipoMensaje: 'success'
    }
  },
  mounted() {
    this.cargarClientes()
  },
  methods: {
    async cargarClientes() {
      try {
        this.loading = true
        const response = await apiService.obtenerTodosLosClientes()
        this.clientes = response.data
      } catch (error) {
        console.error('Error al cargar clientes:', error)
        this.mensaje = 'Error al cargar los clientes'
        this.tipoMensaje = 'danger'
      } finally {
        this.loading = false
      }
    },
    async eliminarCliente(id) {
      if (confirm('¿Está seguro de que desea eliminar este cliente? Se eliminarán también todas sus solicitudes de préstamo.')) {
        try {
          await apiService.eliminarCliente(id)
          this.mensaje = 'Cliente eliminado correctamente'
          this.tipoMensaje = 'success'
          this.cargarClientes()
        } catch (error) {
          console.error('Error al eliminar cliente:', error)
          this.mensaje = 'Error al eliminar el cliente'
          this.tipoMensaje = 'danger'
        }
      }
    },
    formatearFecha(fecha) {
      if (!fecha) return ''
      return format(new Date(fecha), 'dd/MM/yyyy', { locale: es })
    }
  }
}
</script>

<style scoped>
.clientes-container {
  width: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  gap: 2rem;
}

.page-header h1 {
  font-size: 2rem;
  color: var(--primary);
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.subtitle {
  color: var(--gray);
  margin: 0;
  font-size: 1rem;
}

.empty-state {
  padding: 3rem 2rem;
  text-align: center;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.empty-state h3 {
  color: var(--primary);
  margin-bottom: 0.5rem;
}

.empty-state p {
  color: var(--gray);
  margin-bottom: 1.5rem;
}

.table-responsive {
  overflow-x: auto;
}

.table-id {
  color: var(--primary);
  font-weight: 600;
}

.client-name {
  font-weight: 600;
  color: var(--primary);
}

.table-email {
  color: var(--primary);
}

.table-row {
  transition: all 0.2s ease;
}

.table-row:hover {
  background-color: rgba(0, 31, 63, 0.02) !important;
}

.table-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-sm {
  padding: 0.4rem 0.8rem;
  font-size: 0.8rem;
  border-radius: 6px;
}

@media (max-width: 1024px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .page-header .btn {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 1.5rem;
  }

  table {
    font-size: 0.85rem;
  }

  th, td {
    padding: 0.75rem 0.5rem;
  }

  .table-actions {
    flex-direction: column;
    gap: 0.25rem;
  }

  .table-actions .btn {
    width: 100%;
  }
}

/* Banking UI refresh */
.page-header { padding: .6rem 0; }
.eyebrow { color: #a17d1e; font-size: .7rem; font-weight: 800; letter-spacing: .14em; text-transform: uppercase; }
.page-header h1 { color: #092743; margin: .25rem 0; font-size: clamp(1.65rem, 3vw, 2.15rem); }
.btn { border-radius: 10px; }
.btn-primary { background: #092743; border: 1px solid #092743; box-shadow: 0 8px 18px rgba(9,39,67,.16); }
.btn-primary:hover { background: #0d3c69; }
.card { border: 1px solid #e2e8ee; border-radius: 18px; box-shadow: 0 10px 30px rgba(12,37,64,.07); }
.card-body { padding: 0; }
table { min-width: 940px; }
thead { background: #092743; }
th { padding: 1rem 1.15rem; border-bottom-color: #d3ae4c; font-size: .72rem; text-transform: uppercase; letter-spacing: .06em; }
td { padding: 1rem 1.15rem; color: #46596a; }
.table-row:hover { background: #f7f9fb !important; }
.table-id { color: #a17d1e; }
.client-name { color: #092743; }
.table-actions .btn { min-width: 38px; box-shadow: none; }
.loading { min-height: 320px; }
.empty-state { min-height: 360px; display: grid; place-items: center; align-content: center; }
.empty-icon { width: 64px; height: 64px; display: grid; place-items: center; margin-inline: auto; border-radius: 18px; background: #edf3f8; }
.alert { border-radius: 12px; box-shadow: 0 6px 18px rgba(12,37,64,.06); }
</style>
