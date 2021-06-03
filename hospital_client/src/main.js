import Vue from 'vue'
import App from './App.vue'
import Login from './components/auth/Login.vue'
import Dashboard from './components/dashboard/Dashboard.vue'
import CertificateRequest from './components/dashboard/CertificateRequest.vue'
import Devices from './components/dashboard/Devices.vue'
import VueRouter from 'vue-router'
import Vuesax from 'vuesax'
import 'vuesax/dist/vuesax.css'
import 'boxicons/css/boxicons.min.css'
import { store } from './store.js';

Vue.config.productionTip = false
Vue.use(VueRouter)
Vue.use(Vuesax)

const ifNotAuthenticated = (to, from, next) => {
  if (!store.getters.isAuthenticated) {
    next()
    return
  }
  next('/')
}

const ifAuthenticated = (to, from, next) => {
  if (store.getters.isAuthenticated) {
    next()
    return
  }
  next('/login')
}

const routes = [
  { path: '/', component: Dashboard,
    children: [
      {
        path: 'certificate_request',
        component: CertificateRequest
      },
      {
        path: 'devices',
        component: Devices
      }
    ],
    beforeEnter: ifAuthenticated
  },
  { path: '/login', component: Login, beforeEnter: ifNotAuthenticated }
]

const router = new VueRouter({
  routes
})

new Vue({
  router,
  render: h => h(App),
  store
}).$mount('#app')
