import jks


from MedWeb import settings

def getSSLCertificate():
    keystore = jks.KeyStore.load(settings.KEYSTORE_PATH, settings.KEYSTORE_PASSWORD)
