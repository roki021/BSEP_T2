import 'axios'
import axios from 'axios'

export const login = async function (username, password) {
    const response = await axios.post(`${process.env.VUE_APP_HOSPITAL_API}/auth/login`, {
        'username': username,
        'password': password
    })

    response.accessToken

    console.log('ovo', response)
}