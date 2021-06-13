import Vue from 'vue'
import Vuex from 'vuex'
import { postman } from "./postman.js";

Vue.use(Vuex)

export const store = new Vuex.Store({
    state: {
        token: localStorage.getItem('user-token') || '',
        devices: []
    },
    actions: {
        requestAuth: ({commit}, user) => {
            return new Promise((resolve, reject) => {
                postman
                .post(`${process.env.VUE_APP_HOSPITAL_API}/auth/login`, user, {withCredentials: true})
                .then(response => {
                    console.log(response.headers)

                    var token = response.data.accessToken
                    localStorage.setItem('user-token', token)
                    console.log('set', 'Bearer ' + token)
                    postman.defaults.headers.common['Authorization'] = 'Bearer ' + token
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
                delete postman.defaults.headers.common['Authorization']
                resolve()
            })
        },
        getAllDevices: ({commit}) => {
            return new Promise((resolve, reject) => {
                postman
                .get(`${process.env.VUE_APP_HOSPITAL_API}/devices`, {withCredentials: true})
                .then(response => {
                    commit('setDevices', response.data)
                    resolve(response.data);
                })
                .catch(err => {
                    localStorage.removeItem('user-token')
                    reject(err)
                })
            })
        },
        // eslint-disable-next-line no-unused-vars
        addNewDevice: ({commit}, device) => {
            console.log(device);
            return new Promise((resolve, reject) => {
                postman
                .post(`${process.env.VUE_APP_HOSPITAL_API}/devices`, device, {withCredentials: true})
                .then(response => {
                    resolve(response.data);
                })
                .catch(err => {
                    reject(err)
                })
            })
        }
    },
    mutations: {
        login(state, token) {
            state.token = token
        },
        logout(state) {
            state.token = ''
        },
        setDevices(state, devices) {
            state.devices = devices;
        }
    },
    getters: {
        isAuthenticated: state => !!state.token,
    }
})