<script setup lang="ts">
definePageMeta({ middleware: "auth" });

const events = ref<any[]>([]);
const reports = ref<any[]>([]);
const error = ref("");

async function load() {
  try {
    events.value = await $fetch("/api/campus-events");
    reports.value = await $fetch("/api/campus-events/reports/sales");
  } catch (caught: any) {
    error.value = caught?.data?.message || "Unable to load admin data.";
  }
}

async function approve(id: string) {
  await $fetch(`/api/campus-events/${id}/approve`, { method: "POST" });
  await load();
}

async function reject(id: string) {
  await $fetch(`/api/campus-events/${id}/reject`, { method: "POST" });
  await load();
}

await load();
</script>

<template>
  <main class="page">
    <div class="page-header">
      <h1>Super Admin Workspace</h1>
      <NuxtLink class="workspace-link" to="/dashboard">Dashboard</NuxtLink>
    </div>
    <p v-if="error" class="error">{{ error }}</p>

    <section class="page-grid">
      <article v-for="event in events" :key="event.id" class="card">
        <h2>{{ event.title }}</h2>
        <p>Status: <strong>{{ event.status }}</strong></p>
        <button v-if="event.status !== 'PUBLISHED'" @click="approve(event.id)">Approve</button>
        <button v-if="event.status !== 'REJECTED'" class="danger" @click="reject(event.id)">Reject</button>
      </article>
    </section>

    <h2>Sales Reports</h2>
    <section class="page-grid">
      <article v-for="report in reports" :key="report.eventId" class="card">
        <h3>{{ report.title }}</h3>
        <p>Revenue: ₱{{ (report.revenueInCentavos / 100).toFixed(2) }}</p>
      </article>
    </section>
  </main>
</template>
