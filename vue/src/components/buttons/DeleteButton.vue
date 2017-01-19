<template>
  <button type="button" class="button is-danger" @click="emitDelete">
    <span class="icon">
      <i class="fa fa-times"></i>
    </span>
    <span>Удалить</span>
  </button>
</template>

<script>
  export default {
    methods: {
      emitDelete() {
        this.$emit('delete');
      }
    }
  }
</script>
