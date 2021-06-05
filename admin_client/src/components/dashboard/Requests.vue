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
                <vs-th> Type </vs-th>
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
                  {{ tr.title }}
                </vs-td>
                <vs-td>
                  <vs-button transparent @click="activate(i)"
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
        <div class="center grid">
          <vs-row v-if="activeRequest.commonName">
            <vs-col w="6"><div class="wrapper"><b>Common name</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.commonName }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.surname">
            <vs-col w="6"><div class="wrapper"><b>Surname</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.surname }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.givenName">
            <vs-col w="6"><div class="wrapper"><b>Given name</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.givenName }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.organization">
            <vs-col w="6"><div class="wrapper"><b>Organization</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.organization }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.organizationUnit">
            <vs-col w="6"><div class="wrapper"><b>Organization unit</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.organizationUnit }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.country">
            <vs-col w="6"><div class="wrapper"><b>Country</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.country }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.email">
            <vs-col w="6"><div class="wrapper"><b>Email</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.email }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.uniqueIdentifier">
            <vs-col w="6"><div class="wrapper"><b>Unique identifier</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.uniqueIdentifier }}</div></vs-col>
          </vs-row>
          <vs-row v-if="activeRequest.title">
            <vs-col w="6"><div class="wrapper"><b>CSR Type</b></div></vs-col>
            <vs-col w="6"><div class="wrapper">{{ activeRequest.title }}</div></vs-col>
          </vs-row>
          <vs-row align="center" justify="center">
            <vs-col w="6"><div class="wrapper-template-input"><b>Template</b></div></vs-col>
            <vs-col w="6">
              <div class="wrapper-template-input">
                <vs-select placeholder="Select" v-model="template">
            <vs-option label="INTERMEDIATE" value="intermediate">
              INTERMEDIATE
            </vs-option>
            <vs-option label="LEAF_HOSPITAL" value="LEAF_HOSPITAL">
              LEAF_HOSPITAL
            </vs-option>
          </vs-select>
              </div>
            </vs-col>
          </vs-row>
        </div>
      </div>

      <template #footer>
        <div class="footer-dialog">
          <vs-button :loading="waitingResponse" block v-on:click="issueCertificate()"> Issue certificate </vs-button>
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
    activeRequest: {},
    activeRequestIndex: 0,
    waitingResponse: false,
    template: 'LEAF_HOSPITAL'
  }),
  methods: {
    activate (index) {
      this.active = true
      this.activeRequestIndex = index
      this.activeRequest = this.requests[index]
    },
    issueCertificate () {
      this.waitingResponse = true;

      axios
      .post(`${process.env.VUE_APP_ADMIN_API}/issue-certificate/${this.activeRequest.id}/${this.template}`)
      .then(() => {
        this.waitingResponse = false;
        this.active = false;

        // success, remove from 
        this.requests.splice(this.activeRequestIndex, 1)

        this.$vs.notification({
            color: 'success',
            position: null,
            title: 'Successfully issued',
            text: 'New CER successfully created and distributed to subject.'
        })
      })
    }
  },
  mounted() {
    axios
      .get(`${process.env.VUE_APP_ADMIN_API}/certificate-signing-requests`)
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

.wrapper {
  padding-top: 15px;
}

.wrapper-template-input, .footer-dialog {
  padding-top: 30px;
}
</style>