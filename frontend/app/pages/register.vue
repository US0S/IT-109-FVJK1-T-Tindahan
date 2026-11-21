<script setup lang="ts">
definePageMeta({ middleware: "guest" });

const username = ref("");
const email = ref("");
const password = ref("");
const role = ref<"ATTENDEE" | "EVENT_ORGANIZER">("ATTENDEE");
const error = ref("");
const loading = ref(false);
const usernameRegex = /^[A-Za-z0-9_]{3,20}$/;
const passwordRegex =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&#]{8,64}$/;

async function submit() {
  error.value = "";
  if (!usernameRegex.test(username.value.trim())) {
    error.value =
      "Username must be 3-20 characters using only letters, numbers, or underscores.";
    return;
  }
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
    error.value = "Enter a valid email address.";
    return;
  }
  if (!passwordRegex.test(password.value)) {
    error.value =
      "Password must be 8-64 characters with uppercase, lowercase, and a number.";
    return;
  }
  loading.value = true;
  try {
    await $fetch("/api/auth/register", {
      method: "POST",
      body: {
        username: username.value,
        email: email.value,
        password: password.value,
        role: role.value,
      },
    });
    await navigateTo("/login");
  } catch (caught: any) {
    error.value =
      caught?.data?.message ||
      caught?.data?.statusMessage ||
      (Array.isArray(caught?.data) ? caught.data.join(" ") : "") ||
      "Unable to create account.";
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
      <h1>Create your account</h1>
      <p class="subtitle">Join the campus event community.</p>

      <form @submit.prevent="submit">
        <label>
          Username
          <input
            v-model="username"
            autocomplete="username"
            placeholder="Username"
            required
          />
        </label>
        <label>
          Email
          <input
            v-model="email"
            type="email"
            autocomplete="email"
            placeholder="Email"
            required
          />
        </label>
        <label>
          Password
          <input
            v-model="password"
            type="password"
            autocomplete="new-password"
            placeholder="Passowrd"
            required
          />
        </label>
        <label>
          Account role
          <select v-model="role">
            <option value="ATTENDEE">Attendee</option>
            <option value="EVENT_ORGANIZER">Event Organizer</option>
          </select>
        </label>
        <p v-if="error" class="error">{{ error }}</p>
        <button type="submit" :disabled="loading">
          {{ loading ? "Creating..." : "Create account" }}
        </button>
      </form>

      <p class="switch">
        Already registered? <NuxtLink to="/login">Sign in</NuxtLink>
      </p>
    </section>
  </main>
</template>
