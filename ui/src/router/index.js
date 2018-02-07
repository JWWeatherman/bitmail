import Vue from 'vue'
import Router from 'vue-router'
import VueResource from 'vue-resource'

import Home from '../components/Home.vue'
import Sender from '../components/Sender.vue'
import Recipient from '../components/Recipient.vue'
import Status from '../components/Status.vue'

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
      path: '/recipient/:publicKeyAddress',
      name: 'Recipient',
      component: Recipient
    },
    {
      path: '/status/:publicKeyAddress',
      name: 'Status',
      component: Status
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
