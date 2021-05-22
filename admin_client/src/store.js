import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

export const store = new Vuex.Store({
    state: {
        token: localStorage.getItem('user-token') || '',
    },
    actions: {
        requestAuth: ({commit}, user) => {
            return new Promise((resolve, reject) => {
                axios
                .post(`${process.env.VUE_APP_ADMIN_API}/auth/login`, user)
                .then(response => {
                    var token = response.data.accessToken
                    localStorage.setItem('user-token', token)
                    axios.defaults.headers.common['Authorization'] = 'Bearer ' + token
                    commit('login', token)
                    resolve(response.data)
                })
                .catch(err => {
                    localStorage.removeItem('user-token')
                    reject(err)
                })
            })
        },
        requestLogout: ({commit}) => {
            return new Promise((resolve) => {
                commit('logout')
                localStorage.removeItem('user-token')
                // remove the axios default header
                delete axios.defaults.headers.common['Authorization']
                resolve()
            })
        }
    },
    mutations: {
        login(state, token) {
            state.token = token
        },
        logout(state) {
            state.token = ''
        }
    },
    getters: {
        isAuthenticated: state => !!state.token,
    }
})