import Vue from 'vue'
import Router from 'vue-router'
import VueResource from 'vue-resource'

import Home from '../components/Home.vue'
import Sender from '../components/Sender.vue'
import Recipient from '../components/Recipient.vue'

Vue.use(Router)
Vue.use(VueResource)

export default new Router({
  routes: [
    {
      path: '/sender',
      name: 'Sender',
      component: Sender
    },
    {
      path: '/recipient/:email?',
      name: 'Recipient',
      component: Recipient
    },
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '*',
      redirect: '/'
    }
  ]
})
