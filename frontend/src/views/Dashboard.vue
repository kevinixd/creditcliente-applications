<template>
  <div class="dashboard">

    <!-- Acciones Rápidas -->
    <div class="card">
      <div class="card-header">
        <h2>Acciones Rápidas</h2>
      </div>
      <div class="card-body">
        <div class="actions-grid">
          <router-link to="/clientes/crear" class="action-card">
            <div class="action-text">
              <h4>Agregar Nuevo Cliente</h4>
              <p>Registrar un cliente en el sistema</p>
            </div>
          </router-link>

          <router-link to="/solicitudes/crear" class="action-card">
            <div class="action-text">
              <h4>Nueva Solicitud de Préstamo</h4>
              <p>Crear una nueva solicitud</p>
            </div>
          </router-link>

          <router-link to="/solicitudes" class="action-card">
            <div class="action-text">
              <h4>Revisar Solicitudes Pendientes</h4>
              <p>Ver todas las solicitudes en proceso</p>
            </div>
          </router-link>

          <router-link to="/prestamos" class="action-card">
            <div class="action-text">
              <h4>Ver Préstamos</h4>
              <p>Administrar todos los préstamos activos</p>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/api'

export default {
  name: 'Dashboard',
  data() {
    return {
      totalClientes: 0,
      solicitudesPendientes: 0,
      prestamosActivos: 0,
      loading: true
    }
  },
  mounted() {
    this.cargarDatos()
  },
  methods: {
    async cargarDatos() {
      try {
        const [clientes, solicitudes, prestamos] = await Promise.all([
          apiService.obtenerTodosLosClientes(),
          apiService.obtenerSolicitudesPendientes(),
          apiService.obtenerPrestamosActivos()
        ])

        this.totalClientes = clientes.data.length
        this.solicitudesPendientes = solicitudes.data.length
        this.prestamosActivos = prestamos.data.length
      } catch (error) {
        console.error('Error al cargar datos del dashboard:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.dashboard {
  width: 100%;
}

.dashboard-header {
  margin-bottom: 3rem;
  text-align: center;
}

.dashboard-header h1 {
  font-size: 2.5rem;
  color: var(--primary);
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.subtitle {
  font-size: 1.1rem;
  color: var(--gray);
  margin: 0;
}

/* Stat Cards */
.stat-card {
  background: white;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  padding: 2rem;
  display: flex;
  gap: 1.5rem;
  align-items: flex-start;
  transition: all 0.3s ease;
  border-top: 4px solid var(--primary);
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, var(--secondary) 0%, transparent 100%);
  opacity: 0.1;
  border-radius: 50%;
  transform: translate(50%, -50%);
}

.stat-card:hover {
  box-shadow: var(--box-shadow-hover);
  transform: translateY(-5px);
}

.stat-icon {
  font-size: 3rem;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-content h3 {
  margin: 0 0 0.5rem 0;
  color: var(--primary);
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--primary);
  margin: 0.5rem 0;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-trend {
  margin: 0.5rem 0 0 0;
  font-size: 0.9rem;
  color: var(--gray);
}

/* Actions Grid */
.actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

.action-card {
  background: linear-gradient(135deg, rgba(0, 31, 63, 0.05) 0%, rgba(212, 175, 55, 0.05) 100%);
  border-radius: var(--border-radius);
  padding: 2rem;
  display: flex;
  gap: 1.5rem;
  align-items: flex-start;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid transparent;
  text-decoration: none;
  color: inherit;
}

.action-card:hover {
  border-color: var(--secondary);
  background: linear-gradient(135deg, rgba(0, 31, 63, 0.1) 0%, rgba(212, 175, 55, 0.1) 100%);
  transform: translateX(5px);
}

.action-icon {
  font-size: 2.5rem;
  flex-shrink: 0;
}

.action-text h4 {
  margin: 0 0 0.5rem 0;
  color: var(--primary);
  font-size: 1.1rem;
  font-weight: 600;
}

.action-text p {
  margin: 0;
  color: var(--gray);
  font-size: 0.9rem;
}

@media (max-width: 1024px) {
  .grid-cols-3 {
    grid-template-columns: repeat(2, 1fr);
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }

  .dashboard-header h1 {
    font-size: 2rem;
  }
}

@media (max-width: 768px) {
  .grid-cols-3 {
    grid-template-columns: 1fr;
  }

  .stat-card {
    flex-direction: column;
    text-align: center;
  }

  .stat-icon {
    font-size: 2.5rem;
    margin: 0 auto;
  }

  .stat-value {
    justify-content: center;
  }

  .dashboard-header h1 {
    font-size: 1.8rem;
  }

  .action-card {
    flex-direction: column;
    text-align: center;
  }

  .action-icon {
    margin: 0 auto;
  }

  .action-text h4 {
    font-size: 1rem;
  }
}

/* Banking UI refresh */
.dashboard-header { padding: 2.2rem; text-align: left; border-radius: 20px; color: white; background: linear-gradient(120deg, #061b34, #0c3561); box-shadow: 0 18px 45px rgba(6,27,52,.18); position: relative; overflow: hidden; }
.dashboard-header::after { content: ''; position: absolute; width: 240px; height: 240px; right: -70px; top: -120px; border: 1px solid rgba(211,174,76,.32); border-radius: 50%; box-shadow: 0 0 0 42px rgba(211,174,76,.06); }
.dashboard-header h1 { color: #fff; margin: .35rem 0; font-size: clamp(1.8rem, 4vw, 2.55rem); }
.dashboard-header .subtitle { color: #b9c7d5; }
.eyebrow { color: #e3c46d; font-size: .72rem; font-weight: 800; text-transform: uppercase; letter-spacing: .16em; }
.grid-cols-3 { gap: 1.2rem; }
.stat-card { border: 1px solid #e4eaf0; border-top: 0; border-radius: 16px; box-shadow: 0 8px 24px rgba(12,37,64,.07); }
.stat-card::after { content: ''; position: absolute; inset: 0 auto 0 0; width: 4px; background: #d3ae4c; }
.stat-icon, .action-icon { width: 48px; height: 48px; display: grid; place-items: center; border-radius: 13px; background: #edf3f8; font-size: 1.4rem; }
.stat-value { color: #092743; }
.card { border: 1px solid #e3e9ef; border-radius: 18px; box-shadow: 0 10px 30px rgba(12,37,64,.07); }
.card-header { background: #fff; color: #092743; border-bottom: 1px solid #e6ebf0; }
.card-header h2 { font-size: 1.15rem; }
.action-card { border: 1px solid #e2e8ee; background: #f8fafc; }
.action-card:hover { border-color: #d3ae4c; background: #fffdf7; transform: translateY(-3px); }

@media (max-width: 768px) { .dashboard-header { padding: 1.5rem; } }
</style>
