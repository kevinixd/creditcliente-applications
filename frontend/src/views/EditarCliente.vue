<template>
  <div class="editar-cliente-container">
    <div class="card">
      <div class="card-header">
        <h2>Editar Cliente</h2>
      </div>

      <div v-if="mensaje" :class="['alert', 'alert-' + tipoMensaje]">
        {{ mensaje }}
      </div>

      <div v-if="loading" class="loading">
        <span class="spinner"></span>
      </div>

      <div v-else class="card-body">
        <form @submit.prevent="guardarCambios">
          <div class="grid grid-cols-2">
            <div class="form-group">
              <label for="nombre">Nombre:</label>
              <input 
                type="text" 
                id="nombre" 
                v-model="cliente.nombre" 
                required 
                class="form-control"
              >
            </div>

            <div class="form-group">
              <label for="apellido">Apellido:</label>
              <input 
                type="text" 
                id="apellido" 
                v-model="cliente.apellido" 
                required 
                class="form-control"
              >
            </div>

            <div class="form-group">
              <label for="numeroIdentificacion">Número de Identificación:</label>
              <input 
                type="text" 
                id="numeroIdentificacion" 
                v-model="cliente.numeroIdentificacion" 
                disabled 
                class="form-control"
              >
              <small>No se puede modificar</small>
            </div>

            <div class="form-group">
              <label for="fechaNacimiento">Fecha de Nacimiento:</label>
              <input 
                type="date" 
                id="fechaNacimiento" 
                v-model="cliente.fechaNacimiento" 
                disabled 
                class="form-control"
              >
              <small>No se puede modificar</small>
            </div>

            <div class="form-group">
              <label for="correoElectronico">Correo Electrónico:</label>
              <input 
                type="email" 
                id="correoElectronico" 
                v-model="cliente.correoElectronico" 
                required 
                class="form-control"
              >
            </div>

            <div class="form-group">
              <label for="telefono">Teléfono:</label>
              <input 
                type="text" 
                id="telefono" 
                v-model="cliente.telefono" 
                required 
                class="form-control"
              >
            </div>
          </div>

          <div class="form-group">
            <label for="direccion">Dirección:</label>
            <textarea 
              id="direccion" 
              v-model="cliente.direccion" 
              required 
              class="form-control"
            ></textarea>
          </div>

          <div class="card-footer">
            <button type="submit" class="btn btn-primary" :disabled="guardando">
              <span v-if="!guardando">Guardar Cambios</span>
              <span v-else>Guardando...</span>
            </button>
            <router-link to="/clientes" class="btn btn-secondary">Cancelar</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/api'

export default {
  name: 'EditarCliente',
  data() {
    return {
      cliente: {
        id: null,
        nombre: '',
        apellido: '',
        numeroIdentificacion: '',
        fechaNacimiento: '',
        correoElectronico: '',
        telefono: '',
        direccion: ''
      },
      mensaje: '',
      tipoMensaje: 'success',
      guardando: false,
      loading: true
    }
  },
  mounted() {
    this.cargarCliente()
  },
  methods: {
    async cargarCliente() {
      try {
        const clienteId = this.$route.params.id
        const response = await apiService.obtenerClientePorId(clienteId)
        this.cliente = response.data
      } catch (error) {
        console.error('Error al cargar cliente:', error)
        this.mensaje = 'Error al cargar los datos del cliente'
        this.tipoMensaje = 'danger'
      } finally {
        this.loading = false
      }
    },
    async guardarCambios() {
      try {
        this.guardando = true
        await apiService.actualizarCliente(this.cliente.id, this.cliente)
        this.mensaje = 'Cliente actualizado correctamente'
        this.tipoMensaje = 'success'
        setTimeout(() => {
          this.$router.push('/clientes')
        }, 1500)
      } catch (error) {
        console.error('Error al actualizar cliente:', error)
        this.mensaje = error.response?.data?.message || 'Error al actualizar el cliente'
        this.tipoMensaje = 'danger'
      } finally {
        this.guardando = false
      }
    }
  }
}
</script>

<style scoped>
.editar-cliente-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.card-footer {
  display: flex;
  gap: 1rem;
}

small {
  display: block;
  color: #999;
  font-size: 0.8rem;
  margin-top: 0.25rem;
}

@media (max-width: 768px) {
  .grid-cols-2 {
    grid-template-columns: 1fr;
  }
}

.editar-cliente-container { max-width: 960px; }
.card { border: 1px solid #e1e8ee; border-radius: 18px; box-shadow: 0 16px 40px rgba(12,37,64,.09); }
.card-header { padding: 1.7rem 2rem; background: linear-gradient(120deg, #061b34, #0d3c69); border-bottom: 3px solid #d3ae4c; }
.card-header h2 { font-size: 1.35rem; }
.card-body { padding: 2rem; }
.grid { gap: 1.3rem 1.5rem; margin-bottom: 0; }
.form-group { margin-bottom: 1.35rem; }
label { color: #173b5c; font-size: .86rem; }
.form-control { min-height: 46px; border: 1px solid #ced8e1; border-radius: 10px; background: #fbfcfd; }
.form-control:focus { border-color: #a98424; box-shadow: 0 0 0 3px rgba(211,174,76,.16); background: #fff; }
.form-control:disabled { color: #71808d; background: #edf1f4; }
textarea.form-control { min-height: 100px; }
.card-footer { margin: 1.8rem -2rem -2rem; padding: 1.25rem 2rem; background: #f7f9fb; border-top: 1px solid #e3e9ef; }
.btn { border-radius: 10px; }
.btn-primary { background: #092743; }
.btn-secondary { background: #fff; border: 1px solid #ccd6df; }
.alert { margin: 1.25rem 2rem 0; border-radius: 10px; }
</style>
