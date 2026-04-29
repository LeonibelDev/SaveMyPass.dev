/**
 * aes.js — WebCrypto AES-GCM + PBKDF2 module
 * 
 * True Zero-Knowledge Implementation:
 *  - MasterKey: Derived via PBKDF2 (600,000 iterations) for local encryption.
 *  - AuthHash: Derived from MasterKey via HMAC-SHA256 for server authentication.
 *  - AES/GCM/NoPadding, 12-byte random IV, 128-bit auth tag.
 */

const ITERATIONS = 600_000;
const KEY_LENGTH = 256;
const IV_LENGTH = 12;

/**
 * Derives both the Encryption Key and the Authentication Hash.
 * @param {string} password 
 * @param {string} saltBase64 
 * @returns {Promise<{ key: CryptoKey, authHash: string }>}
 */
export async function deriveMasterKeyAndAuthHash(password, saltBase64) {
  const saltBytes = Uint8Array.from(atob(saltBase64), (c) => c.charCodeAt(0));
  const passwordBytes = new TextEncoder().encode(password);

  // 1. Import raw password as key material
  const keyMaterial = await crypto.subtle.importKey(
    'raw',
    passwordBytes,
    'PBKDF2',
    false,
    ['deriveKey']
  );

  // 2. Derive the Master Encryption Key
  const masterKey = await crypto.subtle.deriveKey(
    {
      name: 'PBKDF2',
      salt: saltBytes,
      iterations: ITERATIONS,
      hash: 'SHA-256',
    },
    keyMaterial,
    { name: 'AES-GCM', length: KEY_LENGTH },
    true,
    ['encrypt', 'decrypt']
  );

  // 3. Derive the Auth Hash (using the masterKey as secret for HMAC)
  const rawMasterKey = await crypto.subtle.exportKey('raw', masterKey);
  const hmacKey = await crypto.subtle.importKey(
    'raw',
    rawMasterKey,
    { name: 'HMAC', hash: 'SHA-256' },
    false,
    ['sign']
  );

  const authSignature = await crypto.subtle.sign(
    'HMAC',
    hmacKey,
    new TextEncoder().encode('SaveMyPass-Authentication-v1')
  );

  return {
    key: masterKey,
    authHash: btoa(String.fromCharCode(...new Uint8Array(authSignature)))
  };
}

/**
 * Generate a cryptographically secure random salt.
 */
export function generateRandomSalt() {
  const salt = crypto.getRandomValues(new Uint8Array(16));
  return btoa(String.fromCharCode(...salt));
}

/**
 * Encrypt a plaintext string with AES-GCM.
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
 * Decrypt a Base64 string.
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
 * Export key for sessionStorage.
 */
export async function exportKey(key) {
  const raw = await crypto.subtle.exportKey('raw', key);
  return btoa(String.fromCharCode(...new Uint8Array(raw)));
}

/**
 * Import key from sessionStorage.
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
