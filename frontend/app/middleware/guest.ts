export default defineNuxtRouteMiddleware(async () => {
  try {
    await $fetch("/api/me");
    return navigateTo("/dashboard");
  } catch {
    return;
  }
});
