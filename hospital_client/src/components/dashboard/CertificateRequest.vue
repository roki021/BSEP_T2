<template>
  <vs-row>
    <vs-col offset="4" w="4">
      <div class="container center content-inputs">
        <div class="input-wrapper">
          <vs-input label="Common name" v-model="requestData.commonName" />
        </div>

        <div class="input-wrapper">
          <vs-row>
            <vs-col w="6">
              <vs-input
                label="Organization"
                v-model="requestData.organization"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
            <vs-col w="6">
              <vs-input
                label="Organization unit"
                v-model="requestData.organizationUnit"
              />
            </vs-col>
          </vs-row>
        </div>

        <div class="input-wrapper">
          <vs-input label="Country" v-model="requestData.country" />
        </div>

        <div class="input-wrapper">
          <vs-row>
            <vs-col w="6">
              <vs-input
                label="Surname"
                v-model="requestData.surname"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
            <vs-col w="6">
              <vs-input label="Given name" v-model="requestData.givenName" />
            </vs-col>
          </vs-row>
        </div>

        <div class="input-wrapper">
          <vs-input label="Email" v-model="requestData.email" />
        </div>
        <div class="form-input">
          <vs-button
            :loading="sending"
            style="width: 100%"
            v-on:click="sendRequest()"
          >
            Send request
          </vs-button>
        </div>
      </div>
    </vs-col>
  </vs-row>
</template>
<script>
import { postman } from "../../postman.js";

export default {
  data: () => ({
    requestData: {
      commonName: "",
      surname: "",
      givenName: "",
      email: "",
      organization: "",
      organizationUnit: "",
      country: "",
      title: "HOSPITAL"
    },
    sending: false,
  }),
  methods: {
    sendRequest() {
      this.sending = true;

      postman
        .post(
          `${process.env.VUE_APP_HOSPITAL_API}/certificate-signing-requests`,
          this.requestData
        )
        .then(() => {
          this.sending = false;

          this.$vs.notification({
            color: "success",
            title: "Request sent",
            text: "Your new CSR is successfully created and sent to CA.",
          });
        })
        .catch(() => {
          this.sending = false;

          this.$vs.notification({
            color: "danger",
            title: "Something went wrong",
            text: "Try again with different common name.",
          });
        });
    },
  },
  computed: {
  },
  mounted() {
    postman
    .get(`${process.env.VUE_APP_HOSPITAL_API}/certificate-signing-requests/autofill-data`, {withCredentials: true}).then(
      response => {
        this.requestData.surname = response.data.surname
        this.requestData.givenName = response.data.givenName
        this.requestData.email = response.data.email
        this.requestData.organization = response.data.organization
        this.requestData.organizationUnit = response.data.organizationUnit
        this.requestData.country = response.data.country
      }
    )
  },
};
</script>
<style scoped>
.input-wrapper {
  padding-top: 25px;
}
</style>