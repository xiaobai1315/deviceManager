import Vue from 'vue'
import Router from 'vue-router'
import DeviceManager from '@/components/deviceManager/DeviceManager'
import AutoTest from '@/components/autoTest/AutoTest'
import Terminal from '@/components/terminal/Terminal'
import Logbook from '@/components/terminal/Logbook'
Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/device'
    },
    {
      path: '/device',
      name: 'devicemanager',
      component: DeviceManager
    },
    {
      path: '/autotest',
      name: 'autotest',
      component: AutoTest
    },
    {
      path: '/terminal',
      name: 'terminal',
      component: Terminal
    },
    {
      path: '/logbook',
      name: 'logbook',
      component: Logbook
    }
  ]
})
