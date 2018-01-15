<template>
  <b-jumbotron id="sender-form" header="Bitcoin Snail Mail" lead="Load up the wallet, we'll take of the rest!" >
    <b-card class="text-center">
      <div class="pk-element">
        <qr-code :hash="publicKeyAddress"></qr-code>
        <div>Public Key Address: {{ publicKeyAddress }}</div>
      </div>
    </b-card>
    <b-button @click="onReset($event)" variant="primary">Done</b-button>
  </b-jumbotron>
</template>
<script>
  import QrCode from './QrCode.vue'
  export default {
    name: 'Sender Send',
    props: ['publicKeyAddress', 'onReset'],
    components: {
      QrCode
    }
  }
</script>
<style>
  #sender-form {

  }
</style>