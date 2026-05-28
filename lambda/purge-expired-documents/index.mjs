const DEFAULT_TIMEOUT_MS = 15000;

export const handler = async () => {
  const apiBaseUrl = requiredEnv("KBASE_API_BASE_URL").replace(/\/+$/, "");
  const purgeSecret = requiredEnv("KBASE_LAMBDA_PURGE_SECRET");
  const timeoutMs = Number(process.env.KBASE_REQUEST_TIMEOUT_MS || DEFAULT_TIMEOUT_MS);
  const endpoint = `${apiBaseUrl}/api/documents/purge-expired`;

  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), timeoutMs);

  try {
    const response = await fetch(endpoint, {
      method: "DELETE",
      headers: {
        "X-Lambda-Secret": purgeSecret
      },
      signal: controller.signal
    });

    const body = await response.text();
    if (!response.ok) {
      throw new Error(`Purge request failed with ${response.status}: ${body}`);
    }

    return {
      statusCode: 200,
      body
    };
  } finally {
    clearTimeout(timeout);
  }
};

function requiredEnv(name) {
  const value = process.env[name];
  if (!value || value.trim().length === 0) {
    throw new Error(`Missing required environment variable: ${name}`);
  }
  return value.trim();
}
