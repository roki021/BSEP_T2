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
                  <i
                    v-if="tr.revoked"
                    style="font-size: 20px"
                    class="bx bx-minus-circle"
                  />
                </vs-td>
                <vs-td>
                  <vs-button transparent @click="activate(tr.serialNumber, i)"
                    >Details</vs-button
                  >
                </vs-td>
              </vs-tr>
            </template>
          </vs-table>
        </div>
      </vs-col>
    </vs-row>
    <vs-dialog v-model="active" @close="handleClose">
      <template #header>
        <h4 class="not-margin">Certificate</h4>
      </template>

      <div class="con-form">
        <div v-if="isHidden" class="center grid">
          <vs-row v-if="selectedCertificate.serialNumber">
            <vs-col w="6"
              ><div class="wrapper"><b>Serial number</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ selectedCertificate.serialNumber }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.commonName">
            <vs-col w="6"
              ><div class="wrapper"><b>Common name</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ selectedCertificate.commonName }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.organizationName">
            <vs-col w="6"
              ><div class="wrapper"><b>Organization</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ selectedCertificate.organizationName }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.organizationUnit">
            <vs-col w="6"
              ><div class="wrapper"><b>Organization unit</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ selectedCertificate.organizationUnit }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.country">
            <vs-col w="6"
              ><div class="wrapper"><b>Country</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ selectedCertificate.country }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.email">
            <vs-col w="6"
              ><div class="wrapper"><b>Email</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ selectedCertificate.email }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.startFrom">
            <vs-col w="6"
              ><div class="wrapper"><b>Valid from</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ toDate(selectedCertificate.startFrom) }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.endTo">
            <vs-col w="6"
              ><div class="wrapper"><b>Valid until</b></div></vs-col
            >
            <vs-col w="6"
              ><div class="wrapper">
                {{ toDate(selectedCertificate.endTo) }}
              </div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.keyUsage">
            <vs-col w="6"
              ><div class="wrapper"><b>Key usage</b></div></vs-col
            >
          </vs-row>
          <vs-row v-if="selectedCertificate.keyUsage">
            <vs-col w="6">
              <div class="wrapper" style="pointer-events: none">
                <template>
                  <vs-checkbox
                    v-for="usage in selectedCertificate.keyUsage"
                    :key="usage"
                    v-model="option"
                  >
                    {{ usage }}
                  </vs-checkbox>
                </template>
              </div>
            </vs-col>
          </vs-row>
        </div>
        <div v-if="!isHidden" class="center grid">
          <vs-row>
            <vs-col w="10">
              <div class="wrapper">
                <b>Revocation reason</b>
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="10">
              <div class="wrapper">
                <template v-if="selectedCertificate.revokeReason">
                  <textarea id="revoke-desc" rows="20" cols="54" v-model="selectedCertificate.revokeReason"
                    :disabled="selectedCertificate.revokeReason"></textarea>
                </template>
                <template v-else>
                  <textarea id="revoke-desc" rows="20" cols="54" v-model="revokeRequset.reason"></textarea>
                </template>
              </div>
            </vs-col>
          </vs-row>
        </div>
      </div>

      <template #footer>
        <div class="footer-dialog">
          <vs-button
            v-if="!selectedCertificate.revokeReason && isHidden"
            v-on:click="isHidden = !isHidden"
            block
          >
            Revoke certificate
          </vs-button>
          <vs-button
            v-else-if="selectedCertificate.revokeReason && isHidden"
            v-on:click="isHidden = !isHidden"
            block
          >
            See revocation details
          </vs-button>
          <vs-button
            v-else-if="selectedCertificate.revokeReason && !isHidden"
            v-on:click="isHidden = !isHidden"
            block
          >
            Back
          </vs-button>
          <vs-button
            v-else
            :loading="waitingResponse"
            v-on:click="revokeCertificate()"
            block
            :disabled="revokeRequset.reason.length < 1"
          >
            Revoke
          </vs-button>
        </div>
      </template>
    </vs-dialog>
  </div>
</template>
<script>
import axios from "axios";

export default {
  data: () => ({
    certificates: [],
    selectedCertificate: {},
    selectedIndex: -1,
    waitingResponse: false,
    active: false,
    option: true,
    isHidden: true,
    revokeRequset: {
      reason: '',
      certId: -1
    }
  }),
  methods: {
    activate(serialNumber, selectedIndex) {
      console.log(serialNumber);
      this.selectedIndex = selectedIndex;
      axios
        .get(`${process.env.VUE_APP_ADMIN_API}/digital-certificates/${serialNumber}`)
        .then((response) => {
          this.selectedCertificate = response.data;
          this.revokeRequset.certId = this.selectedCertificate.serialNumber;
        });
      this.active = true;
    },
    handleClose() {
      this.active = false;
      this.isHidden = true;
    },
    toDate(unixtime) {
      console.log(unixtime);
      let date = new Date(unixtime);
      let hour = date.getHours();
      let min = date.getMinutes();
      let day = date.getDate();
      let month = date.getMonth() + 1;

      if (hour < 10) hour = "0" + hour;
      if (min < 10) min = "0" + min;
      if (day < 10) day = "0" + day;
      if (month < 10) month = "0" + month;

      return (
        hour + ":" + min + " " + day + "." + month + "." + date.getFullYear()
      );
    },
    revokeCertificate() {
      this.waitingResponse = true;
      axios
        .post(
          `${process.env.VUE_APP_ADMIN_API}/digital-certificates/revoke`,
          this.revokeRequset
        )
        .then(() => {
          this.waitingResponse = false;
          console.log("sss");
          this.certificates[this.selectedIndex].revoked = true;
          this.$vs.notification({
            color: "success",
            position: null,
            title: "Successfully revoked",
            text: "Certificate is revoked.",
          });
        })
        .catch(() => {
          this.waitingResponse = false;

          this.$vs.notification({
            color: "danger",
            title: "Something went wrong",
            text: "Try it again in a few moments",
          });
        });
    },
  },
  mounted() {
    axios
      .get(`${process.env.VUE_APP_ADMIN_API}/digital-certificates`)
      .then((response) => {
        this.certificates = response.data;
      });
  },
};
</script>
<style>
* {
  font-size: 13px !important;
}

.wrapper {
  padding-top: 15px;
}

.wrapper-template-input,
.footer-dialog {
  padding-top: 30px;
}

.revoke-status {
  text-align: center;
}

#revoke-desc {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  resize: none;
  font-size: 12px;
}

#revoke-desc:focus {
  outline: none;
}

#revoke-desc:disabled {
  color:black;
}
</style>