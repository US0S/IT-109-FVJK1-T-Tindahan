<script setup lang="ts">
definePageMeta({ middleware: "auth" });

const { data, error } = await useFetch("/api/campus-events/published");
</script>

<template>
  <main class="page">
    <div class="page-header">
      <div class="brand">
        <span class="brand-mark">C</span>
        <span>Published Events</span>
      </div>
      <NuxtLink class="workspace-link" to="/dashboard">Dashboard</NuxtLink>
    </div>

    <p v-if="error" class="error">Unable to load published events.</p>
    <section class="page-grid">
      <article v-for="event in data || []" :key="event.id" class="card">
        <h2>{{ event.title }}</h2>
        <p>{{ event.description }}</p>
        <p><strong>Venue:</strong> {{ event.venue }}</p>
        <p><strong>Starts:</strong> {{ new Date(event.startAt).toLocaleString() }}</p>
        <article v-for="tier in event.tiers" :key="tier.id" class="card">
          {{ tier.name }} - ₱{{ (tier.priceInCentavos / 100).toFixed(2) }} ({{ tier.capacity }} seats)
        </article>
      </article>
    </section>
  </main>
</template>
