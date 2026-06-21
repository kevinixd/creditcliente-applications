<template>
  <div id="app" class="app">
    <header class="navbar">
      <div class="navbar-container">
        <router-link to="/" class="navbar-brand">
          <span class="brand-icon">CHN</span>
          <span class="brand-text">
            <strong>Banco CHN</strong>
            <small>Gestión de crédito</small>
          </span>
        </router-link>

        <nav class="navbar-nav" aria-label="Navegación principal">
          <router-link to="/" class="nav-link" active-class="active">
            <span>01</span> Dashboard
          </router-link>
          <router-link to="/clientes" class="nav-link" active-class="active">
            <span>02</span> Clientes
          </router-link>
          <router-link to="/solicitudes" class="nav-link" active-class="active">
            <span>03</span> Solicitudes
          </router-link>
          <router-link to="/prestamos" class="nav-link" active-class="active">
            <span>04</span> Préstamos
          </router-link>
        </nav>
      </div>
    </header>

    <main class="main-content">
      <router-view></router-view>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <p>Crédito Hipotecario Nacional</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style scoped>
.app { min-height: 100vh; display: flex; flex-direction: column; background: #f3f6fa; }
.navbar { position: sticky; top: 0; z-index: 100; color: #fff; background: rgba(5, 25, 49, .98); border-bottom: 1px solid rgba(211, 174, 76, .65); box-shadow: 0 10px 30px rgba(3, 19, 37, .18); backdrop-filter: blur(16px); }
.navbar-container { width: min(1400px, 100%); margin: auto; padding: .9rem 2rem; display: flex; align-items: center; justify-content: space-between; gap: 2rem; }
.navbar-brand { display: flex; align-items: center; gap: .85rem; color: #fff; text-decoration: none; }
.brand-icon { width: 46px; height: 46px; display: grid; place-items: center; border: 1px solid #d3ae4c; border-radius: 12px; color: #f1d47d; font-size: .78rem; font-weight: 800; letter-spacing: .08em; }
.brand-text { display: grid; line-height: 1.2; }
.brand-text strong { font-size: 1.03rem; letter-spacing: .02em; }
.brand-text small { margin-top: .25rem; color: #aebdcd; font-size: .65rem; text-transform: uppercase; letter-spacing: .12em; }
.navbar-nav { display: flex; gap: .35rem; }
.nav-link { display: flex; align-items: center; gap: .45rem; padding: .7rem 1rem; border-radius: 10px; color: #e5edf5; font-size: .9rem; font-weight: 600; text-decoration: none; transition: .2s ease; }
.nav-link span { color: #d3ae4c; font-size: .62rem; font-weight: 800; }
.nav-link:hover { color: #fff; background: rgba(255,255,255,.08); }
.nav-link.active { color: #f6db8a; background: rgba(211,174,76,.15); }
.main-content { flex: 1; width: min(1400px, 100%); margin: auto; padding: 2.25rem 2rem; }
.footer { margin-top: 2rem; padding: 1.3rem 2rem; color: #fff; background: #051931; border-top: 1px solid rgba(211,174,76,.45); }
.footer-content { width: min(1400px, 100%); margin: auto; display: flex; justify-content: space-between; gap: 1rem; }
.footer p { margin: 0; font-size: .88rem; font-weight: 600; }
.footer span { color: #9eb0c2; font-size: .82rem; }

@media (max-width: 900px) {
  .navbar-container { align-items: flex-start; flex-direction: column; gap: .75rem; }
  .navbar-nav { width: 100%; overflow-x: auto; }
  .nav-link { white-space: nowrap; }
}
@media (max-width: 600px) {
  .navbar-container, .main-content { padding-left: 1rem; padding-right: 1rem; }
  .brand-icon { width: 40px; height: 40px; }
  .nav-link { padding: .6rem .75rem; font-size: .82rem; }
  .footer-content { flex-direction: column; text-align: center; }
}
</style>
