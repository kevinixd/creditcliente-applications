<template>
  <div class="solicitudes-container">
    <div class="page-header">
      <div>
        <span class="eyebrow">Mesa de evaluación</span>
        <h1>Solicitudes de Préstamo</h1>
        <p class="subtitle">Revisa, aprueba y administra las solicitudes de crédito.</p>
      </div>
      <router-link to="/solicitudes/crear" class="btn btn-primary btn-lg">
        + Nueva solicitud
      </router-link>
    </div>

    <div v-if="mensaje" :class="['alert', 'alert-' + tipoMensaje]">
      <span>{{ tipoMensaje === 'success' ? '✓' : '!' }}</span>
      {{ mensaje }}
    </div>

    <section class="card">
      <div class="filter-section">
        <span class="filter-title">Estado de solicitud</span>
        <div class="filter-group">
          <button @click="filtro = 'todas'" :class="['btn btn-sm', filtro === 'todas' ? 'btn-primary' : 'btn-secondary']">Todas</button>
          <button @click="filtro = 'EN_PROCESO'" :class="['btn btn-sm', filtro === 'EN_PROCESO' ? 'btn-primary' : 'btn-secondary']">En proceso</button>
          <button @click="filtro = 'APROBADO'" :class="['btn btn-sm', filtro === 'APROBADO' ? 'btn-primary' : 'btn-secondary']">Aprobadas</button>
          <button @click="filtro = 'RECHAZADO'" :class="['btn btn-sm', filtro === 'RECHAZADO' ? 'btn-primary' : 'btn-secondary']">Rechazadas</button>
        </div>
      </div>

      <div class="card-body">
        <div v-if="loading" class="loading">
          <span class="spinner"></span>
          <small>Consultando solicitudes...</small>
        </div>

        <div v-else-if="solicitudesFiltradas.length === 0" class="empty-state">
          <span class="empty-icon">—</span>
          <h3>No hay solicitudes para mostrar</h3>
          <p>Cambia los filtros o crea una nueva solicitud.</p>
        </div>

        <div v-else class="table-responsive">
          <table>
            <thead>
              <tr><th>ID</th><th>Cliente</th><th>Monto</th><th>Plazo</th><th>Estado</th><th>Fecha</th><th>Acciones</th></tr>
            </thead>
            <tbody>
              <tr v-for="solicitud in solicitudesFiltradas" :key="solicitud.id">
                <td class="table-id">#{{ solicitud.id }}</td>
                <td class="client-cell">{{ obtenerNombreCliente(solicitud.clienteId) }}</td>
                <td class="table-amount">Q{{ solicitud.montoSolicitado.toFixed(2) }}</td>
                <td>{{ solicitud.plazoDias }} días</td>
                <td><span :class="['badge', 'badge-' + obtenerBadgeEstado(solicitud.estado)]">{{ solicitud.estado }}</span></td>
                <td>{{ formatearFecha(solicitud.fechaSolicitud) }}</td>
                <td class="table-actions">
                  <button v-if="solicitud.estado === 'EN_PROCESO'" @click="abrirAprobacion(solicitud)" class="btn btn-sm btn-success" title="Aprobar solicitud">Aprobar</button>
                  <button v-if="solicitud.estado === 'EN_PROCESO'" @click="abrirRechazarModal(solicitud)" class="btn btn-sm btn-danger" title="Rechazar solicitud">Rechazar</button>
                  <span v-if="solicitud.estado !== 'EN_PROCESO'" class="resolved">Resuelta</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

    <div :class="['modal', { active: mostrarModalRechazo }]">
      <div class="modal-content">
        <div class="modal-header">
          <div><small>Decisión de crédito</small><h2>Rechazar solicitud #{{ solicitudSeleccionada?.id }}</h2></div>
          <button type="button" class="close" @click="mostrarModalRechazo = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="motivoRechazo">Motivo del rechazo</label>
            <textarea id="motivoRechazo" v-model="motivoRechazo" class="form-control" placeholder="Explica el motivo del rechazo..." required></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="mostrarModalRechazo = false" class="btn btn-secondary">Cancelar</button>
          <button @click="confirmarRechazo" class="btn btn-danger">Rechazar solicitud</button>
        </div>
      </div>
    </div>

    <div :class="['modal', { active: mostrarModalAprobacion }]">
      <div class="modal-content">
        <div class="modal-header">
          <div><small>Decisión de crédito</small><h2>Aprobar solicitud #{{ solicitudSeleccionada?.id }}</h2></div>
          <button type="button" class="close" @click="mostrarModalAprobacion = false">×</button>
        </div>
        <div class="modal-body">
          <div class="info-group">
            <p><span>Cliente</span><strong>{{ obtenerNombreCliente(solicitudSeleccionada?.clienteId) }}</strong></p>
            <p><span>Monto</span><strong>Q{{ solicitudSeleccionada?.montoSolicitado.toFixed(2) }}</strong></p>
            <p><span>Plazo</span><strong>{{ solicitudSeleccionada?.plazoDias }} días</strong></p>
          </div>
          <div class="form-group">
            <label for="tasaInteres">Tasa de interés (%)</label>
            <input id="tasaInteres" v-model.number="tasaInteres" type="number" step="0.01" min="0" max="100" class="form-control" required>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="mostrarModalAprobacion = false" class="btn btn-secondary">Cancelar</button>
          <button @click="confirmarAprobacion" class="btn btn-success">Aprobar solicitud</button>
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
  name: 'Solicitudes',
  data() {
    return {
      solicitudes: [],
      clientes: [],
      loading: true,
      mensaje: '',
      tipoMensaje: 'success',
      filtro: 'todas',
      mostrarModalRechazo: false,
      mostrarModalAprobacion: false,
      solicitudSeleccionada: null,
      motivoRechazo: '',
      tasaInteres: 5.0
    }
  },
  computed: {
    solicitudesFiltradas() {
      if (this.filtro === 'todas') {
        return this.solicitudes
      }
      return this.solicitudes.filter(s => s.estado === this.filtro)
    }
  },
  mounted() {
    this.cargarDatos()
  },
  methods: {
    async cargarDatos() {
      try {
        this.loading = true
        const [solicitudesResp, clientesResp] = await Promise.all([
          apiService.obtenerTodasLasSolicitudes(),
          apiService.obtenerTodosLosClientes()
        ])
        this.solicitudes = solicitudesResp.data
        this.clientes = clientesResp.data
      } catch (error) {
        console.error('Error al cargar solicitudes:', error)
        this.mensaje = 'Error al cargar las solicitudes'
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
        'EN_PROCESO': 'warning',
        'APROBADO': 'success',
        'RECHAZADO': 'danger'
      }
      return badges[estado] || 'secondary'
    },
    formatearFecha(fecha) {
      if (!fecha) return ''
      return format(new Date(fecha), 'dd/MM/yyyy', { locale: es })
    },
    abrirRechazarModal(solicitud) {
      this.solicitudSeleccionada = solicitud
      this.motivoRechazo = ''
      this.mostrarModalRechazo = true
    },
    abrirAprobacion(solicitud) {
      this.solicitudSeleccionada = solicitud
      this.tasaInteres = 5.0
      this.mostrarModalAprobacion = true
    },
    async confirmarRechazo() {
      if (!this.motivoRechazo.trim()) {
        this.mensaje = 'Por favor, ingresa un motivo para el rechazo'
        this.tipoMensaje = 'danger'
        return
      }
      try {
        await apiService.rechazarSolicitud(this.solicitudSeleccionada.id, this.motivoRechazo)
        this.mensaje = 'Solicitud rechazada correctamente'
        this.tipoMensaje = 'success'
        this.mostrarModalRechazo = false
        this.cargarDatos()
      } catch (error) {
        console.error('Error al rechazar solicitud:', error)
        this.mensaje = 'Error al rechazar la solicitud'
        this.tipoMensaje = 'danger'
      }
    },
    async confirmarAprobacion() {
      if (this.tasaInteres < 0 || this.tasaInteres > 100) {
        this.mensaje = 'La tasa de interés debe estar entre 0 y 100'
        this.tipoMensaje = 'danger'
        return
      }
      try {
        await apiService.aprobarSolicitud(this.solicitudSeleccionada.id, this.tasaInteres)
        this.mensaje = 'Solicitud aprobada correctamente'
        this.tipoMensaje = 'success'
        this.mostrarModalAprobacion = false
        this.cargarDatos()
      } catch (error) {
        console.error('Error al aprobar solicitud:', error)
        this.mensaje = 'Error al aprobar la solicitud'
        this.tipoMensaje = 'danger'
      }
    }
  }
}
</script>

<style scoped>
.solicitudes-container { width: 100%; }
.page-header { display: flex; justify-content: space-between; align-items: center; gap: 2rem; margin-bottom: 1.8rem; }
.eyebrow { color: #a17d1e; font-size: .7rem; font-weight: 800; letter-spacing: .14em; text-transform: uppercase; }
.page-header h1 { margin: .25rem 0; color: #092743; font-size: clamp(1.65rem, 3vw, 2.15rem); }
.subtitle { margin: 0; color: #6d7d8c; }
.btn { border-radius: 10px; }
.btn-primary { background: #092743; box-shadow: 0 8px 18px rgba(9,39,67,.16); }
.card { overflow: hidden; border: 1px solid #e1e8ee; border-radius: 18px; box-shadow: 0 12px 34px rgba(12,37,64,.08); }
.filter-section { display: flex; justify-content: space-between; align-items: center; gap: 1rem; padding: 1.25rem 1.5rem; border-bottom: 1px solid #e3e9ef; background: #f8fafc; }
.filter-title { color: #173b5c; font-size: .83rem; font-weight: 700; }
.filter-group { display: flex; gap: .45rem; margin: 0; }
.filter-group .btn { border-radius: 999px; box-shadow: none; }
.filter-group .btn-secondary { background: #fff; border: 1px solid #d9e1e8; }
.card-body { padding: 0; }
.table-responsive { overflow-x: auto; }
table { min-width: 900px; }
thead { background: #092743; }
th { padding: 1rem; border-bottom-color: #d3ae4c; font-size: .7rem; text-transform: uppercase; letter-spacing: .06em; }
td { padding: 1rem; color: #46596a; }
tbody tr:hover { background: #f7f9fb; }
.table-id { color: #a17d1e; font-weight: 700; }
.client-cell, .table-amount { color: #173b5c; font-weight: 700; }
.table-actions { display: flex; gap: .4rem; }
.badge { min-width: 94px; padding: .38rem .7rem; border: 0; border-radius: 999px; font-size: .68rem; letter-spacing: .04em; }
.resolved { color: #8a98a5; font-size: .78rem; }
.loading { min-height: 320px; flex-direction: column; gap: 1rem; }
.empty-state { min-height: 340px; display: grid; place-items: center; align-content: center; text-align: center; }
.empty-icon { width: 58px; height: 58px; display: grid; place-items: center; border-radius: 16px; color: #a17d1e; background: #f7f1df; font-size: 1.6rem; }
.empty-state h3 { margin: 1rem 0 .25rem; color: #092743; }
.empty-state p { margin: 0; color: #758593; }
.alert { border-radius: 12px; box-shadow: 0 6px 18px rgba(12,37,64,.06); }
.modal { backdrop-filter: blur(5px); }
.modal-content { overflow: hidden; border: 1px solid rgba(211,174,76,.4); border-radius: 18px; }
.modal-header { background: #092743; border-bottom: 2px solid #d3ae4c; }
.modal-header small { color: #e3c46d; text-transform: uppercase; letter-spacing: .1em; }
.modal-header h2 { margin-top: .25rem; }
.modal-body { padding: 1.6rem 2rem; }
.modal-footer { padding: 1.2rem 2rem; }
.info-group { padding: 1rem 1.1rem; margin-bottom: 1.3rem; border: 1px solid #e1e8ee; border-radius: 12px; background: #f8fafc; }
.info-group p { display: flex; justify-content: space-between; margin: .45rem 0; color: #637584; }
.info-group strong { color: #173b5c; }
.form-control { border: 1px solid #ced8e1; border-radius: 10px; }
.form-control:focus { border-color: #a98424; box-shadow: 0 0 0 3px rgba(211,174,76,.16); }

@media (max-width: 800px) {
  .page-header, .filter-section { align-items: stretch; flex-direction: column; }
  .page-header .btn { width: 100%; }
  .filter-group { overflow-x: auto; padding-bottom: .3rem; }
  .filter-group .btn { white-space: nowrap; }
}
</style>
