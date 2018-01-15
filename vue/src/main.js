import Vue from 'vue'
import App from './App.vue'

import VueRouter from 'vue-router'
import VueResource from 'vue-resource'

import Sender from './components/Sender.vue'
import Recipient from './components/Recipient.vue'
import Main from './components/Main.vue'

import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'bootstrap/dist/css/bootstrap.css'

Vue.use(VueResource)
Vue.use(VueRouter)


const CSRF_TOKEN = '@{play.filters.csrf.CSRF.getToken(request)}'

Vue.http.interceptors.push((request, next) => {
  request.headers.set('Accept', 'application/json')
  request.headers.set('Type', 'application/json')
  next()
})

Vue.use(BootstrapVue)

// Pointing routes to the components they should use
const router = new VueRouter({
  routes: [
    {
      path: '/sender',
      name: 'Sender',
      component: Sender
    },
    {
      path: '/recipient/:email',
      name: 'Recipient',
      component: Recipient
    },
    {
      path: '/',
      name: 'Main',
      component: Main
    },
    {
      path: '*',
      redirect: '/'
    }
  ]
})

new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App}
})
