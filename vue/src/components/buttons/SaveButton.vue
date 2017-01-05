<template>
  <button type="button" class="btn btn-primary" @click="emitSave">
    <span class="glyphicon glyphicon-floppy-disk"></span>
    Сохранить
  </button>
</template>

<script>
  export default {
    methods: {
      emitSave() {
        this.$emit('save');
      }
    }
  }
</script>
