<template>
  <div class="container">
    <h2>Registro</h2>
    <SignUpForm />
  </div>
</template>

<script setup>
import SignUpForm from '../components/SignUpForm.vue';
</script>

<style scoped>
.container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  background: white;
  text-align: center;
}
</style>
