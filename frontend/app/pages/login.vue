<script setup lang="ts">
definePageMeta({ middleware: "guest" });

const email = ref("");
const password = ref("");
const error = ref("");
const loading = ref(false);

async function submit() {
  error.value = "";
  loading.value = true;
  try {
    await $fetch("/api/auth/login", {
      method: "POST",
      body: { email: email.value, password: password.value },
    });
    await navigateTo("/dashboard");
  } catch (caught: any) {
    error.value =
      caught?.data?.message ||
      caught?.data?.statusMessage ||
      "Unable to log in.";
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <main class="auth-page">
    <section class="auth-card">
      <div class="brand">
        <span class="brand-mark">C</span>
        <span>Campus Events</span>
      </div>
      <h1>Welcome back</h1>
      <p class="subtitle">Sign in to manage or discover campus events.</p>

      <form @submit.prevent="submit">
        <label>
          Email
          <input v-model="email" type="email" autocomplete="email" placeholder="Enter your email" required />
        </label>
        <label>
          Password
          <input v-model="password" type="password" autocomplete="current-password" placeholder="Enter your password" required />
        </label>
        <p v-if="error" class="error">{{ error }}</p>
        <button type="submit" :disabled="loading">{{ loading ? "Signing in..." : "Sign in" }}</button>
      </form>

      <p class="switch">No account? <NuxtLink to="/register">Create one</NuxtLink></p>
    </section>
  </main>
</template>
