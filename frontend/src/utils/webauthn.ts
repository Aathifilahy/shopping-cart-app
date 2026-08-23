export const bufferToBase64 = (buffer: ArrayBuffer): string =>
  btoa(String.fromCharCode(...new Uint8Array(buffer)));

export const base64ToBuffer = (base64: string): ArrayBuffer => {
  const binaryString = atob(base64);
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }
  return bytes.buffer;
};

export const createPasskeyCredential = async (creationOptions: any) => {
  const publicKey = {
    ...creationOptions,
    challenge: base64ToBuffer(creationOptions.challenge),
    user: {
      ...creationOptions.user,
      id: base64ToBuffer(creationOptions.user.id),
    },
  };
  if (creationOptions.excludeCredentials) {
    publicKey.excludeCredentials = creationOptions.excludeCredentials.map((cred: any) => ({
      ...cred,
      id: base64ToBuffer(cred.id),
    }));
  }
  const credential = await navigator.credentials.create({ publicKey });
  return credential;
};

export const getPasskeyAssertion = async (assertionOptions: any) => {
  const publicKey = {
    ...assertionOptions,
    challenge: base64ToBuffer(assertionOptions.challenge),
  };
  if (assertionOptions.allowCredentials) {
    publicKey.allowCredentials = assertionOptions.allowCredentials.map((cred: any) => ({
      ...cred,
      id: base64ToBuffer(cred.id),
    }));
  }
  const credential = await navigator.credentials.get({ publicKey });
  return credential;
};