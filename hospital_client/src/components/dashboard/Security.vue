<template>
    <vs-row>
        <vs-col offset="4" w="4">
            <div class="container">
                <template v-if="token">
                    <h2>Open communication with SuperAdmin</h2>
                    <vs-row>
                        <vs-col vs-type="flex" vs-justify="center" vs-align="center" w="10">
                            <div class="token-field">{{ token }}</div>
                        </vs-col>
                        <vs-col vs-type="flex" vs-justify="center" vs-align="center" w="2">
                            <vs-switch v-model="active" v-on:loading="sendingRequest" v-on:click="changeStatus"/>
                        </vs-col>
                    </vs-row>
                </template>
                <template v-else>
                    <h2>Request certificate to enable this option.</h2>
                </template>
            </div>
        </vs-col>
    </vs-row>
</template>

<script>
import axios from 'axios'
export default {
    data: () => ({
        token: '',
        active: false,
        sendingRequest: false
    }),
    methods: {
        changeStatus() {
            this.sendingRequest = true
            var status = !this.active ? 'activate' : 'deactivate'
            axios
            .post(`${process.env.VUE_APP_HOSPITAL_API}/security/token/${status}`)
            .then(() => {
                this.sendingRequest = false
            })
        }
    },
    mounted() {
        axios
        .get(`${process.env.VUE_APP_HOSPITAL_API}/security/token`)
        .then(response => {
            this.token = response.data.token
            this.active = response.data.active
        })
    }
}
</script>

<style>
    .token-field {
        background-color: rgba(var(--vs-dark), 0.1) !important;
        color: rgba(var(--vs-dark), 1);
        text-align: center;
        padding: 8px;
        border: 1px solid transparent;
        border-radius: 10px;
        margin-right: 10px;
    }
</style>