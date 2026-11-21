export default defineNuxtRouteMiddleware(async () => {
  try {
    await $fetch("/api/me");
  } catch {
    return navigateTo("/login");
  }
});
