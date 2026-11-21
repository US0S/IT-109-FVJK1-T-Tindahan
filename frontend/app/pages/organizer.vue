<script setup lang="ts">
definePageMeta({ middleware: "auth" });

const events = ref<any[]>([]);
const error = ref("");
const ticketUuid = ref("");
const verification = ref("");

async function loadEvents() {
  try {
    events.value = await $fetch("/api/campus-events");
  } catch (caught: any) {
    error.value = caught?.data?.message || "Unable to load events.";
  }
}

async function verifyTicket() {
  verification.value = "";
  try {
    const result = await $fetch("/api/ticket-orders/verify", {
      method: "POST",
      body: { ticketUuid: ticketUuid.value },
    });
    verification.value = `Ticket verified for ${result.attendee?.username}`;
  } catch (caught: any) {
    verification.value = caught?.data?.message || "Ticket verification failed.";
  }
}

await loadEvents();
</script>

<template>
  <main class="page">
    <div class="page-header">
      <h1>Event Organizer Workspace</h1>
      <NuxtLink class="workspace-link" to="/dashboard">Dashboard</NuxtLink>
    </div>
    <p v-if="error" class="error">{{ error }}</p>

    <section class="card">
      <h2>Verify Ticket</h2>
      <form @submit.prevent="verifyTicket">
        <input v-model="ticketUuid" placeholder="Ticket UUID" required />
        <button type="submit">Verify</button>
      </form>
      <p v-if="verification">{{ verification }}</p>
    </section>

    <section class="page-grid">
      <article v-for="event in events" :key="event.id" class="card">
        <h2>{{ event.title }}</h2>
        <p>Status: <strong>{{ event.status }}</strong></p>
        <p>{{ event.venue }}</p>
      </article>
    </section>
  </main>
</template>
