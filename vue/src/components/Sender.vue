<template>
    <div>
        <b-form @submit="onSubmit" @reset="onReset" v-if="show">
            <b-form-group id="exampleInputGroup1"
                          label="Recipient email address:"
                          label-for="exampleInput1"
                          description="It is really important that this email is correct.">
                <b-form-input id="exampleInput1"
                              type="email"
                              v-model="form.recipientEmail"
                              required
                              placeholder="Enter email">
                </b-form-input>
            </b-form-group>
            <b-form-group id="exampleInputGroup2"
                          label="Your email address:"
                          label-for="exampleInput2"
                          >
                <b-form-input id="exampleInput2"
                              type="email"
                              v-model="form.senderEmail"
                              placeholder="Enter email"
                                :readonly="form.remainAnonymous">
                </b-form-input>
                <b-form-group id="exampleGroup4">
                    <b-form-checkbox v-model="form.remainAnonymous" value="that">Remain anonymous</b-form-checkbox>
                </b-form-group>
            </b-form-group>
            <b-form-group id="exampleInputGroup3"
                          label="Brief message to recipient:"
                          label-for="exampleInput3">
                <b-form-input id="exampleInput3"
                              type="text"
                              v-model="form.senderMessage"
                              placeholder="Message to recipient">
                </b-form-input>
            </b-form-group>
            <b-button type="submit" variant="primary">Submit</b-button>
            <b-button type="reset" variant="danger">Reset</b-button>
        </b-form>
        <div>{{ serverResponse }}</div>
    </div>
</template>

<script>
  export default {
    data () {
      return {
        form: {
          recipientEmail: '',
          senderEmail: '',
          senderMessage: '',
          remainAnonymous: false
        },
        show: true,
        serverResponse: null
      }
    },
    methods: {
      onSubmit (evt) {
        evt.preventDefault();
        this.$http.post('/createWallet', this.form)
          .then(res => {
            this.serverResponse = res.body
            console.log(JSON.stringify(res.body, null, 4))
          })
          .catch(err => {
            this.serverResponse = err.body
          })
      },
      onReset (evt) {
        evt.preventDefault();
        /* Reset our form values */
        this.form = {
          recipientEmail: '',
          senderEmail: '',
          senderMessage: '',
          remainAnonymous: false
        }
        /* Trick to reset/clear native browser form validation state */
        this.show = false;
        this.$nextTick(() => { this.show = true });
      }
    }
  }
</script>