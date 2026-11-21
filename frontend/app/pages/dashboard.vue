<script setup lang="ts">
definePageMeta({ middleware: "auth" });

const { data, refresh } = await useFetch("/api/me");
const user = computed(() => data.value?.user);
const roleDetails = computed(() => {
  if (user.value?.role === "SUPER_ADMIN") return ["Approve events", "Reject events", "View university-wide sales"];
  if (user.value?.role === "EVENT_ORGANIZER") return ["Create events", "Manage ticket tiers", "Scan ticket QR codes"];
  return ["Browse published events", "Buy tickets", "View purchased tickets"];
});

const workspaceLink = computed(() => {
  if (user.value?.role === "SUPER_ADMIN") return "/admin";
  if (user.value?.role === "EVENT_ORGANIZER") return "/organizer";
  return "/events";
});

async function logout() {
  await $fetch("/api/auth/logout", { method: "POST" });
  await navigateTo("/login");
}
</script>

<template>
  <main class="dashboard">
    <header>
      <div class="brand">
        <span class="brand-mark">C</span>
        <span>Campus Events</span>
      </div>
      <button class="logout" @click="logout">Sign out</button>
    </header>

    <section class="welcome">
      <p class="eyebrow">Campus Event Booking &amp; Ticketing</p>
      <h1>Hello, {{ user?.username }}</h1>
      <p>You are signed in as <strong>{{ user?.role?.toLowerCase() }}</strong>.</p>
    </section>

    <section class="roles">
      <article v-for="permission in roleDetails" :key="permission">
        {{ permission }}
      </article>
    </section>
    <NuxtLink class="workspace-link" :to="workspaceLink">Open workspace</NuxtLink>
  </main>
</template>
