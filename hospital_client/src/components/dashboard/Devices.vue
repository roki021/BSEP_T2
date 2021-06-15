<template>
  <div>
    <vs-row>
      <vs-col offset="3" w="6">
        <div class="container">
          <vs-table>
            <template #thead>
              <vs-tr>
                <vs-th> Device ID </vs-th>
                <vs-th> IP address </vs-th>
                <vs-th> Port </vs-th>
                <vs-th> Monitoring </vs-th>
                <vs-th> Connected to </vs-th>
              </vs-tr>
            </template>
            <template #tbody>
              <vs-tr :key="i" v-for="(tr, i) in devices" :data="tr">
                <vs-td>
                  {{ tr.id }}
                </vs-td>
                <vs-td>
                  {{ tr.ipAddress }}
                </vs-td>
                <vs-td>
                  {{ tr.port }}
                </vs-td>
                <vs-td>
                  <vs-button transparent>Preview</vs-button>
                </vs-td>
                <vs-td>
                  <vs-button transparent>Preview</vs-button>
                </vs-td>
              </vs-tr>
            </template>
          </vs-table>
          <vs-col offset="10">
            <vs-button v-if="this.$store.getters.getRole === 'ROLE_ADMIN'" transparent @click="activateAdding()">Add new</vs-button>
          </vs-col>
        </div>
      </vs-col>
    </vs-row>
    <vs-dialog v-model="addingActive" @close="handleCloseAdding">
      <template #header>
        <h4 class="not-margin">Device addition form</h4>
      </template>

      <div class="con-form">
        <div class="center grid">
          <vs-row>
            <vs-col w="12">
              <vs-input
                label-placeholder="Common name"
                v-model="newDevice.commonName"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px">
              <vs-input
                label-placeholder="IP address"
                v-model="newDevice.ipAddress"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px; padding-bottom: 10px">
              <vs-input
                label-placeholder="Port"
                v-model="newDevice.port"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
        </div>
      </div>

      <template #footer>
        <div class="footer-dialog">
          <vs-button block v-on:click="addNewDevice()">
            Add new device
          </vs-button>
        </div>
      </template>
    </vs-dialog>
  </div>
</template>
<script>
export default {
  data: () => ({
    devices: [],
    newDevice: {
      commonName: "",
      ipAddress: "",
      port: "",
    },
    addingActive: false,
  }),
  methods: {
    activateAdding() {
      this.addingActive = true;
    },
    handleCloseAdding() {
      this.addingActive = false;
      this.newDevice = {
        commonName: "",
        ipAddress: "",
        port: "",
      };
    },
    addNewDevice() {
      this.$store
        .dispatch("addNewDevice", this.newDevice)
        .then(() => {
          this.$vs.notification({
            color: "success",
            title: "Request sent",
            text: "Device added successfully.",
          });
          this.$store.dispatch("getAllDevices").then(() => {
            this.devices = this.$store.state.devices;
          });
        })
        .catch(() => {
          this.$vs.notification({
            color: "danger",
            title: "Something went wrong",
            text: "Already added device.",
          });
        });
      this.handleCloseAdding();
    },
  },
  computed: {},
  mounted() {
    this.$store.dispatch("getAllDevices").then(() => {
      this.devices = this.$store.state.devices;
    })
    .catch(err => {
      console.log(err);
      this.$vs.notification({
            color: "danger",
            title: "Something went wrong",
            text: "Already added device.",
          });
    });
  },
};
</script>
<style scoped>
</style>