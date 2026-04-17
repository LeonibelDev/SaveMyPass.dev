/**
 * aes.js — WebCrypto AES-GCM + PBKDF2 module
 *
 * Mirrors exactly the Java AesCipherConfig + KeyUtil implementation:
 *  - PBKDF2WithHmacSHA256, 100,000 iterations, 256-bit key
 *  - AES/GCM/NoPadding, 12-byte random IV, 128-bit auth tag
 *  - Stored format: Base64(IV[12] + ciphertext)
 */

const ITERATIONS = 600_000;
const KEY_LENGTH = 256;
const IV_LENGTH = 12;

/**
 * Derive an AES-GCM CryptoKey from the user's master password and their salt.
 * @param {string} password  — master password (plaintext)
 * @param {string} saltBase64 — Base64-encoded salt from the server
 * @returns {Promise<CryptoKey>}
 */
export async function deriveKey(password, saltBase64) {
  const saltBytes = Uint8Array.from(atob(saltBase64), (c) => c.charCodeAt(0));

  const keyMaterial = await crypto.subtle.importKey(
    'raw',
    new TextEncoder().encode(password),
    'PBKDF2',
    false,
    ['deriveKey']
  );

  return crypto.subtle.deriveKey(
    {
      name: 'PBKDF2',
      salt: saltBytes,
      iterations: ITERATIONS,
      hash: 'SHA-256',
    },
    keyMaterial,
    { name: 'AES-GCM', length: KEY_LENGTH },
    true,           // extractable=true so we can persist to sessionStorage
    ['encrypt', 'decrypt']
  );
}

/**
 * Encrypt a plaintext string with AES-GCM.
 * Returns Base64(IV[12] + ciphertext) — same format as Java's CipherPassword.
 * @param {CryptoKey} key
 * @param {string} plaintext
 * @returns {Promise<string>} Base64 ciphertext
 */
export async function encrypt(key, plaintext) {
  const iv = crypto.getRandomValues(new Uint8Array(IV_LENGTH));

  const encrypted = await crypto.subtle.encrypt(
    { name: 'AES-GCM', iv, tagLength: 128 },
    key,
    new TextEncoder().encode(plaintext)
  );

  const combined = new Uint8Array(iv.byteLength + encrypted.byteLength);
  combined.set(iv, 0);
  combined.set(new Uint8Array(encrypted), iv.byteLength);

  return btoa(String.fromCharCode(...combined));
}

/**
 * Decrypt a Base64(IV + ciphertext) string produced by Java's CipherPassword or encrypt().
 * @param {CryptoKey} key
 * @param {string} ciphertextBase64
 * @returns {Promise<string>} plaintext
 */
export async function decrypt(key, ciphertextBase64) {
  const data = Uint8Array.from(atob(ciphertextBase64), (c) => c.charCodeAt(0));
  const iv = data.slice(0, IV_LENGTH);
  const encrypted = data.slice(IV_LENGTH);

  const decrypted = await crypto.subtle.decrypt(
    { name: 'AES-GCM', iv, tagLength: 128 },
    key,
    encrypted
  );

  return new TextDecoder().decode(decrypted);
}

/**
 * Export a CryptoKey to a Base64 string for sessionStorage persistence.
 * @param {CryptoKey} key
 * @returns {Promise<string>} Base64
 */
export async function exportKey(key) {
  const raw = await crypto.subtle.exportKey('raw', key);
  return btoa(String.fromCharCode(...new Uint8Array(raw)));
}

/**
 * Import a Base64 string from sessionStorage back into a CryptoKey.
 * @param {string} base64
 * @returns {Promise<CryptoKey>}
 */
export async function importKey(base64) {
  const raw = Uint8Array.from(atob(base64), (c) => c.charCodeAt(0));
  return crypto.subtle.importKey(
    'raw',
    raw,
    { name: 'AES-GCM', length: KEY_LENGTH },
    true,
    ['encrypt', 'decrypt']
  );
}
