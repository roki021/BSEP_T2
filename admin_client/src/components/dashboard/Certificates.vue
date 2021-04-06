<template>
  <div>
    <vs-row>
      <vs-col offset="2" w="8">
        <div class="container">
          <vs-table>
            <template #thead>
              <vs-tr>
                <vs-th> Serial number </vs-th>
                <vs-th> Common name </vs-th>
                <vs-th> Start date </vs-th>
                <vs-th> End date </vs-th>
                <vs-th> Revoked </vs-th>
                <vs-th> &nbsp; </vs-th>
              </vs-tr>
            </template>
            <template #tbody>
              <vs-tr :key="i" v-for="(tr, i) in certificates" :data="tr">
                <vs-td>
                  {{ tr.serialNumber }}
                </vs-td>
                <vs-td>
                  {{ tr.commonName }}
                </vs-td>
                <vs-td>
                  {{ toDate(tr.startDate) }} 
                </vs-td>
                <vs-td>
                  {{ toDate(tr.endDate) }}
                </vs-td>
                <vs-td class="revoke-status">
                  <i v-if="tr.revoked" style="font-size: 20px" class='bx bx-minus-circle' />
                </vs-td>
                <vs-td>
                  <vs-button transparent @click="activate(tr.serialNumber)">Details</vs-button>
                </vs-td>
              </vs-tr>
            </template>
          </vs-table>
        </div>
      </vs-col>
    </vs-row>
    <vs-dialog v-model="active">
      <template #header>
        <h4 class="not-margin">Certificate</h4>
      </template>

      <div class="con-form">
        <div class="center grid">
          
        </div>
      </div>

      <template #footer>
        <div class="footer-dialog">
          <vs-button block> Dugmetina </vs-button>
        </div>
      </template>
    </vs-dialog>
  </div>
</template>
<script>
import axios from 'axios'

export default {
  data: () => ({
    certificates: [],
    active: false
  }),
  methods: {
    activate(serialNumber) {
      console.log(serialNumber)
      this.active = true
    },
    toDate(unixtime) {
      let date = new Date(unixtime)
      let hour = date.getHours()
      let min = date.getMinutes()
      let day = date.getDay()
      let month = date.getMonth()

      if (hour < 10) hour = '0' + hour
      if (min < 10) min = '0' + min
      if (day < 10) day = '0' + day
      if (month < 10) month = '0' + month

      return hour + ':' 
      + min
      + ' ' 
      + day 
      + '.' 
      + month
      + '.' 
      + date.getFullYear()
    }
  },
  mounted() {
    axios
      .get('http://localhost:8080/api/digital-certificates')
      .then((response) => {
        this.certificates = response.data;
    });
  }
};
</script>
<style>

.revoke-status {
  text-align: center;
}

</style>