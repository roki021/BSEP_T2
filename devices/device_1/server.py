import flask
import ssl
import sys
import requests
from werkzeug import serving
from flask import Flask
from OpenSSL import SSL
from os import path
from cryptography.hazmat.primitives import serialization as crypto_serialization
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.backends import default_backend as crypto_default_backend
from cryptography import x509
from cryptography.x509.oid import NameOID
from cryptography.hazmat.primitives import hashes

VERIFY_USER = True

API_HOST = "0.0.0.0"
API_PORT = 5000
API_CRT = "keystore/device.crt"
API_KEY = "keystore/device.key"
API_CA_T = "truststore/client.crt"

app = Flask(__name__)


def generateKeys():
    return rsa.generate_private_key(
        backend=crypto_default_backend(),
        public_exponent=65537,
        key_size=2048
    )


def create_CSR(data):
    key = generateKeys()

    csr = x509.CertificateSigningRequestBuilder().subject_name(x509.Name([
        # Provide various details about who we are.
        x509.NameAttribute(NameOID.COMMON_NAME, data["commonName"]),
        x509.NameAttribute(NameOID.SURNAME, data["surname"]),
        x509.NameAttribute(NameOID.GIVEN_NAME, data["givenName"]),
        x509.NameAttribute(NameOID.ORGANIZATION_NAME, data["organization"]),
        x509.NameAttribute(NameOID.ORGANIZATIONAL_UNIT_NAME, data["organizationUnit"]),
        x509.NameAttribute(NameOID.COUNTRY_NAME, data["country"]),
        x509.NameAttribute(NameOID.EMAIL_ADDRESS, data["email"]),
        x509.NameAttribute(NameOID.SERIAL_NUMBER, data["serialNumber"]),
        x509.NameAttribute(NameOID.TITLE, data["title"]),
    ])).add_extension(
        x509.SubjectAlternativeName([
            # Describe what sites we want this certificate for.
            x509.DNSName(u"localhost"),
            x509.DNSName(data["commonName"].split(":")[0]),
        ]),
        critical=False,
        # Sign the CSR with our private key.
    ).sign(key, hashes.SHA256())


    with open("keystore/new_key.key", "wb") as prv_key:
        private_key = key.private_bytes(
            crypto_serialization.Encoding.PEM,
            crypto_serialization.PrivateFormat.TraditionalOpenSSL,
            crypto_serialization.NoEncryption())
        prv_key.write(private_key)

    return csr.public_bytes(crypto_serialization.Encoding.PEM)


def check_if_cert_present():
    return path.exists(API_KEY) and path.exists(API_CRT) and path.exists(API_CA_T)


@app.route('/csr',  methods=['POST'])
def send_public_key():
    json_data = flask.request.json
    try:
        return create_CSR(json_data)
    except:
        status_code = flask.Response(status=400)
        return status_code


if __name__ == '__main__':
    context = None
    if len(sys.argv) == 2:
        API_PORT = int(sys.argv[1])
    if check_if_cert_present():
        context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
        if VERIFY_USER:
            context.verify_mode = ssl.CERT_OPTIONAL
            context.load_verify_locations(API_CA_T)
        try:
            context.load_cert_chain(API_CRT, API_KEY)
        except Exception as e:
            sys.exit("Error starting flask server. " +
                "Missing cert or key. Details: {}"
                .format(e))

    serving.run_simple(
        API_HOST, API_PORT, app, ssl_context=context)