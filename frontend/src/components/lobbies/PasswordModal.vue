<template>
  <form @submit.prevent="submit()">
    <span class="p-float-label mt-2">
      <Password
        id="password"
        v-model="password"
        :feedback="false"
        toggleMask
        input-class="w-full"
        required
      />
      <label for="password">Jelszó</label>
    </span>
    <Button
      type="submit"
      :label="buttonLabel"
      :loading="loading"
      class="p-button-primary mt-4 float-right"
    />
  </form>
</template>

<script setup>
import { ref } from "vue";
import Button from "primevue/button";
import Password from "primevue/password";

const emit = defineEmits(["submit"]);
const props = defineProps({
  buttonLabel: String,
  loading: Boolean,
});

const password = ref();

function submit() {
  emit("submit", password.value);
}
</script>

<style scoped></style>
