<template>
  <router-link :to="link" class="button is-link">
    <span class="icon is-small"><i class="fa fa-history"></i></span>
    <span>История</span>
  </router-link>
</template>

<script>
  export default {
    props: ['link']
  }
</script>
