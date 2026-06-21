<template>
  <div class="crear-solicitud-container">
    <div class="card">
      <div class="card-header">
        <h2>Nueva Solicitud de Préstamo</h2>
      </div>

      <div v-if="mensaje" :class="['alert', 'alert-' + tipoMensaje]">
        {{ mensaje }}
      </div>

      <div class="card-body">
        <form @submit.prevent="guardarSolicitud">
          <div class="form-group">
            <label for="cliente">Seleccionar Cliente:</label>
            <select 
              id="cliente" 
              v-model="solicitud.clienteId" 
              required 
              class="form-control"
            >
              <option value="">-- Seleccione un cliente --</option>
              <option v-for="cliente in clientes" :key="cliente.id" :value="cliente.id">
                {{ cliente.nombre }} {{ cliente.apellido }} ({{ cliente.numeroIdentificacion }})
              </option>
            </select>
          </div>

          <div class="grid grid-cols-2">
            <div class="form-group">
              <label for="montoSolicitado">Monto Solicitado (Q):</label>
              <input 
                type="number" 
                id="montoSolicitado" 
                v-model.number="solicitud.montoSolicitado" 
                step="0.01"
                required 
                class="form-control"
              >
              <small v-if="solicitud.errores.montoSolicitado" class="text-danger">
                {{ solicitud.errores.montoSolicitado }}
              </small>
            </div>

            <div class="form-group">
              <label for="plazoDias">Plazo (días):</label>
              <input 
                type="number" 
                id="plazoDias" 
                v-model.number="solicitud.plazoDias" 
                required 
                class="form-control"
              >
              <small v-if="solicitud.errores.plazoDias" class="text-danger">
                {{ solicitud.errores.plazoDias }}
              </small>
            </div>
          </div>

          <div class="form-group">
            <label for="detalles">Detalles de la Solicitud:</label>
            <textarea 
              id="detalles" 
              v-model="solicitud.detalles" 
              class="form-control"
            ></textarea>
            <small v-if="solicitud.errores.detalles" class="text-danger">
              {{ solicitud.errores.detalles }}
            </small>
          </div>

          <div class="card-footer">
            <button type="submit" class="btn btn-primary" :disabled="guardando">
              <span v-if="!guardando">Guardar Solicitud</span>
              <span v-else>Guardando...</span>
            </button>
            <router-link to="/solicitudes" class="btn btn-secondary">Cancelar</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/api'

export default {
  name: 'CrearSolicitud',
  data() {
    return {
      solicitud: {
        clienteId: '',
        montoSolicitado: 0,
        plazoDias: 365,
        detalles: '',
        errores: {}
      },
      clientes: [],
      mensaje: '',
      tipoMensaje: 'success',
      guardando: false
    }
  },
  mounted() {
    this.cargarClientes()
  },
  methods: {
    async cargarClientes() {
      try {
        const response = await apiService.obtenerTodosLosClientes()
        this.clientes = response.data
      } catch (error) {
        console.error('Error al cargar clientes:', error)
        this.mensaje = 'Error al cargar los clientes'
        this.tipoMensaje = 'danger'
      }
    },
    async guardarSolicitud() {
      try {
        this.guardando = true
        await apiService.crearSolicitud(this.solicitud)
        this.mensaje = 'Solicitud creada correctamente'
        this.tipoMensaje = 'success'
        setTimeout(() => {
          this.$router.push('/solicitudes')
        }, 1500)
      } catch (error) {
        const respuesta = error.response?.data
        this.solicitud.errores = respuesta?.errors || {}
        this.mensaje = respuesta?.message || 'Error al crear la solicitud'
        this.tipoMensaje = 'danger'
      } finally {
        this.guardando = false
      }
    }
  }
}
</script>

<style scoped>
.crear-solicitud-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.card-footer {
  display: flex;
  gap: 1rem;
}

@media (max-width: 768px) {
  .grid-cols-2 {
    grid-template-columns: 1fr;
  }
}

.crear-solicitud-container { max-width: 900px; }
.card { border: 1px solid #e1e8ee; border-radius: 18px; box-shadow: 0 16px 40px rgba(12,37,64,.09); }
.card-header { padding: 1.7rem 2rem; background: linear-gradient(120deg, #061b34, #0d3c69); border-bottom: 3px solid #d3ae4c; }
.card-header h2 { font-size: 1.35rem; }
.card-body { padding: 2rem; }
.grid { gap: 1.3rem 1.5rem; margin-bottom: 0; }
.form-group { margin-bottom: 1.35rem; }
label { color: #173b5c; font-size: .86rem; }
.form-control { min-height: 46px; border: 1px solid #ced8e1; border-radius: 10px; background: #fbfcfd; }
.form-control:focus { border-color: #a98424; box-shadow: 0 0 0 3px rgba(211,174,76,.16); background: #fff; }
textarea.form-control { min-height: 120px; }
.card-footer { margin: 1.8rem -2rem -2rem; padding: 1.25rem 2rem; background: #f7f9fb; border-top: 1px solid #e3e9ef; }
.btn { border-radius: 10px; }
.btn-primary { background: #092743; }
.btn-secondary { background: #fff; border: 1px solid #ccd6df; }
.alert { margin: 1.25rem 2rem 0; border-radius: 10px; }
</style>
