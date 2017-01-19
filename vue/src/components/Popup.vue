<template>
  <transition name="modal">
    <div class="modal is-active">
      <div class="modal-background"></div>
      <div class="modal-card">
        <header class="modal-card-head">
          <p class="modal-card-title">
            <slot name="header">
              default header
            </slot>
          </p>
          <button class="delete" @click="$emit('close')"></button>
        </header>
        <section class="modal-card-body">
          <slot name="body">
            default body
          </slot>
        </section>
        <footer class="modal-card-foot">
          <a class="button is-primary" @click="$emit('okPressed')">
            {{buttonLabel}}
          </a>
        </footer>
      </div>
    </div>
  </transition>
</template>

<script>
  export default {
    props: {
      buttonLabel: {
        default: 'Ok'
      }
    }
  }
</script>

<style>
  .modal-background {
    transition: opacity .3s ease;
  }

  .modal-card {
    transition: all .3s ease;
  }

  .modal-enter {
    opacity: 0;
  }

  .modal-leave-active {
    opacity: 0;
  }

  .modal-enter .modal-card,
  .modal-leave-active .modal-card {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
  }
</style>
