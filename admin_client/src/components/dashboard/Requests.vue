<template>
  <div>
    <vs-row>
      <vs-col offset="2" w="8">
        <div class="container">
          <vs-table>
            <template #thead>
              <vs-tr>
                <vs-th> ID </vs-th>
                <vs-th> Common name </vs-th>
                <vs-th> &nbsp; </vs-th>
              </vs-tr>
            </template>
            <template #tbody>
              <vs-tr :key="i" v-for="(tr, i) in requests" :data="tr">
                <vs-td>
                  {{ tr.id }}
                </vs-td>
                <vs-td>
                  {{ tr.commonName }}
                </vs-td>
                <vs-td>
                  <vs-button transparent @click="active = !active"
                    >Details</vs-button
                  >
                </vs-td>
              </vs-tr>
            </template>
          </vs-table>
        </div>
      </vs-col>
    </vs-row>
    <vs-dialog v-model="active">
      <template #header>
        <h4 class="not-margin">Certificate Signing Request</h4>
      </template>

      <div class="con-form">
        <div class="center">
          <vs-select placeholder="Select" v-model="template">
            <vs-option label="Vuesax" value="1">
              Vuesax
            </vs-option>
            <vs-option label="Vue" value="2">
              Vue
            </vs-option>
            <vs-option label="Javascript" value="3">
              Javascript
            </vs-option>
            <vs-option disabled label="Sass" value="4">
              Sass
            </vs-option>
            <vs-option label="Typescript" value="5">
              Typescript
            </vs-option>
            <vs-option label="Webpack" value="6">
              Webpack
            </vs-option>
            <vs-option label="Nodejs" value="7">
              Nodejs
            </vs-option>
          </vs-select>
        </div>
      </div>

      <template #footer>
        <div class="footer-dialog">
          <vs-button block> Issue certificate </vs-button>
        </div>
      </template>
    </vs-dialog>
  </div>
</template>
<script>
import axios from "axios";

export default {
  data: () => ({
    requests: [],
    active: false,
    template: ''
  }),
  mounted() {
    axios
      .get("http://localhost:8080/api/certificate-signing-requests")
      .then((response) => {
        this.requests = response.data;
      });
  },
};
</script>
<style scoped>
* {
  font-size: 13px !important;
}
</style>