<template>
        <vs-row>
        <vs-col offset="2" w="8">
            <div class="container">
                <div class="l">
                    <vs-table striped>
                        <template #thead>
                        <vs-tr>
                            <vs-th>
                            Logs
                            </vs-th>
                        </vs-tr>
                        </template>
                        <template #tbody>
                        <vs-tr
                            :key="i"
                            v-for="(tr, i) in logs"
                            :data="tr"
                        >
                            <vs-td>
                            {{ tr.raw }}
                            </vs-td>
                        </vs-tr>
                        </template>
                    </vs-table>
                </div>
            </div>
        </vs-col>
        </vs-row>
</template>
<script>
import { postman } from "../../postman.js";

export default {
    data: () => ({
        logs: []
    }),
    mounted() {
        postman
        .get(`${process.env.VUE_APP_HOSPITAL_API}/logs`, {withCredentials: true})
        .then((response) => {
            this.logs = response.data.slice().reverse()
        })
    }
}
</script>
<style scoped>
.container {
    padding: 20px;
    border: 1px solid transparent;
    background: #ffffff;
    border-radius: 8px;
}

.l {
    height: 400px;
    overflow: auto;
}
</style>