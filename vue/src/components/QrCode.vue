<template>
  <div id="qr-code">
    <canvas id="qr"></canvas>
  </div>
</template>
<script>
  import QRious from 'qrious'
  export default {
    name: 'Qr Code',
    mounted () {
      const hash = this.hash
      const qr = new QRious({
        element: document.getElementById('qr'),
        backgroundAlpha: 0.8,
        foregroundAlpha: 0.8,
        level: 'H',
        padding: 25,
        size: 250,
        value: hash
      })
    },
    props: ['hash']
  }
</script>