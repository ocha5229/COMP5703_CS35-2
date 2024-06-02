import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note:
 *
 * hidden: true                     //  true  401，login，/edit/1
 * alwaysShow: true                 //  children 1，--
 *                                  // ，--
 *                                  //  children
 *                                  //  alwaysShow: true，，
 * redirect: noRedirect             //  noRedirect
 * name:'router-name'               // ，<keep-alive>
 * query: '{"id": 1, "name": "ry"}' //
 * roles: ['admin', 'common']       //
 * permissions: ['a:a:a', 'b:b:b']  //
 * meta : {
    noCache: true                   // true， <keep-alive> ( false)
    title: 'title'                  //
    icon: 'svg-name'                // ，src/assets/icons/svg
    breadcrumb: false               // false，breadcrumb
    activeMenu: '/system/user'      // ，。
  }
 */

//
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: 'Home', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: 'Individual center', icon: 'user' }
      }
    ]
  }
]

// ，
export const dynamicRoutes = [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole'),
        name: 'AuthRole',
        meta: { title: 'Assign roles', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser'),
        name: 'AuthUser',
        meta: { title: 'Assigned user', activeMenu: '/system/role' }
      }
    ]
  }
]

//
let routerPush = Router.prototype.push;
let routerReplace = Router.prototype.replace;
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // url#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
