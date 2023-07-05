package com.stubhub.domain.account.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.stubhub.newplatform.property.MasterStubHubProperties;

/**
 * @author vpothuru
 * date 11/10/15
 *
 */
@Component("rsaEncryptionUtil")
public class RSAEncryptionUtil {

	private static final String LOG_ERROR_PREFIX = "api_domain=account, api_resource={}, api_method={}, error_message={}";
	private static final String LOG_INFO_PREFIX = "api_domain=account, api_resource={}, api_method={}, input_param={} message={}";
	private static final String ALGORITHM = "RSA";
	private static final String KEYSTORE_TYPE = "PKCS12";
	private static final String KEYSTORE_ALIAS = "taxid";
	private static final String KEYSTORE_PASS = "stubhub123";
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Cipher cipher;

	private final static Logger log = LoggerFactory.getLogger(RSAEncryptionUtil.class);

	public RSAEncryptionUtil() throws Exception {
		String m_keystore_default_file = System.getProperty("java.home") + "/lib/security/server1099.truststore";
		String m_keystore_file = getProperty("1099.keystore.path", m_keystore_default_file);
		String m_keystore_alias = getProperty("1099.keystore.alias", KEYSTORE_ALIAS);
		String m_keystore_password = getProperty("1099.keystore.password", KEYSTORE_PASS);

		log.info(LOG_INFO_PREFIX, "RSAEncryptionUtil", "init method", "", " key store file " + m_keystore_file);

		KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(m_keystore_file.replace('/', File.separatorChar));
			keyStore.load(fis, m_keystore_password.toCharArray());

			if (!keyStore.containsAlias(m_keystore_alias)) {
				log.error(LOG_ERROR_PREFIX, "RSAEncryptionUtil", "RSAEncryptionUtil", "m_keystore_alias is not contained");
			}

			cipher = Cipher.getInstance(ALGORITHM);

			KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(m_keystore_password.toCharArray());
			KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(m_keystore_alias, protectionParameter);

			privateKey = privateKeyEntry.getPrivateKey();
			publicKey = privateKeyEntry.getCertificate().getPublicKey();

			log.info(LOG_INFO_PREFIX, "RSAEncryptionUtil", "init method", "", " encryption key initialized successfully");
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	public byte[] encrypt(String plaintext) throws Exception {
		if (plaintext == null || plaintext.length() == 0) {
			log.error(LOG_ERROR_PREFIX, "RSAEncryptionUtil", "encrypt method", " parameter value is null or empty");
			throw new Exception("null parameter passed to encrypt method");
		}
		byte[] encryptedText;
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		encryptedText = cipher.doFinal(plaintext.getBytes());
		return encryptedText;
	}

	public String decrypt(byte[] textEncrypted) throws Exception {
		if (textEncrypted == null) {
			log.error(LOG_ERROR_PREFIX, "RSAEncryptionUtil", "decrypt method", " parameter value is null or empty");
			throw new Exception("null parameter passed to decrypt method");
		}
		byte[] decryptedText;
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		decryptedText = cipher.doFinal(textEncrypted);
		return new String(decryptedText);
	}

	protected String getProperty(String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}
}
