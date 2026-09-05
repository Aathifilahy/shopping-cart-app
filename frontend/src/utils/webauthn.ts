/**
 * Convert base64url to standard base64 (replace URL-safe chars and add padding)
 */
const base64UrlToBase64 = (base64url: string): string => {
  let base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
  while (base64.length % 4 !== 0) {
    base64 += '=';
  }
  return base64;
};

export const bufferToBase64 = (buffer: ArrayBuffer): string =>
  btoa(String.fromCharCode(...new Uint8Array(buffer)));

export const base64ToBuffer = (base64url: string): ArrayBuffer => {
  if (!base64url) {
    return new ArrayBuffer(0);
  }
  const base64 = base64UrlToBase64(base64url);
  const binaryString = atob(base64);
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }
  return bytes.buffer;
};

export const createPasskeyCredential = async (creationOptions: any) => {
  // Build the publicKey object WITHOUT the extensions field
  const publicKey: any = {
    challenge: base64ToBuffer(creationOptions.challenge),
    rp: creationOptions.rp,
    user: {
      id: base64ToBuffer(creationOptions.user.id),
      name: creationOptions.user.name,
      displayName: creationOptions.user.displayName,
    },
    pubKeyCredParams: creationOptions.pubKeyCredParams,
    authenticatorSelection: creationOptions.authenticatorSelection,
    attestation: creationOptions.attestation,
  };

  if (creationOptions.excludeCredentials) {
    publicKey.excludeCredentials = creationOptions.excludeCredentials.map((cred: any) => ({
      ...cred,
      id: base64ToBuffer(cred.id),
    }));
  }

  if (creationOptions.timeout) {
    publicKey.timeout = creationOptions.timeout;
  }

  // 🔥 Intentionally omit extensions to avoid appidExclude error
  // Do NOT add publicKey.extensions

  const credential = await navigator.credentials.create({ publicKey });
  return credential;
};

export const getPasskeyAssertion = async (assertionOptions: any) => {
  const publicKey: any = {
    challenge: base64ToBuffer(assertionOptions.challenge),
    rpId: assertionOptions.rpId,
    userVerification: assertionOptions.userVerification,
  };

  if (assertionOptions.allowCredentials) {
    publicKey.allowCredentials = assertionOptions.allowCredentials.map((cred: any) => ({
      ...cred,
      id: base64ToBuffer(cred.id),
    }));
  }

  if (assertionOptions.timeout) {
    publicKey.timeout = assertionOptions.timeout;
  }

  // Intentionally omit extensions

  const credential = await navigator.credentials.get({ publicKey });
  return credential;
};