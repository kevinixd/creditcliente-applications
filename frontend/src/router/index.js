import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue')
  },
  {
    path: '/clientes',
    name: 'Clientes',
    component: () => import('../views/Clientes.vue')
  },
  {
    path: '/clientes/crear',
    name: 'CrearCliente',
    component: () => import('../views/CrearCliente.vue')
  },
  {
    path: '/clientes/:id/editar',
    name: 'EditarCliente',
    component: () => import('../views/EditarCliente.vue')
  },
  {
    path: '/solicitudes',
    name: 'Solicitudes',
    component: () => import('../views/Solicitudes.vue')
  },
  {
    path: '/solicitudes/crear',
    name: 'CrearSolicitud',
    component: () => import('../views/CrearSolicitud.vue')
  },
  {
    path: '/prestamos',
    name: 'Prestamos',
    component: () => import('../views/Prestamos.vue')
  },
  {
    path: '/prestamos/:id',
    name: 'DetallesPrestamo',
    component: () => import('../views/DetallesPrestamo.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
