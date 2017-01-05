<template>
  <button type="button" class="btn btn-danger" @click="emitDelete">
    <span class="glyphicon glyphicon-trash"></span>
    Удалить
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
