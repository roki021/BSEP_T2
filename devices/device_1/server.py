import flask
import ssl
import sys
import atexit
import random
from apscheduler.schedulers.background import BackgroundScheduler
from werkzeug import serving
from flask import Flask
from os import path
from datetime import datetime
from cryptography.hazmat.primitives import serialization as crypto_serialization
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.backends import default_backend as crypto_default_backend
from cryptography import x509
from cryptography.x509.oid import NameOID
from cryptography.hazmat.primitives import hashes
from request import send_request
from ocsp_check import is_cert_revoked
from dotenv import dotenv_values
from measures import Scale, pulse_state_machine, pressure_low_state_machine, pressure_high_state_machine, temp_state_machine


config = dotenv_values(".env")
VERIFY_USER=bool(config["VERIFY_USER"])
API_HOST=config["API_HOST"]
API_PORT=int(config["API_PORT"])
API_CRT=config["API_CRT"]
API_KEY=config["API_KEY"]
API_CA_T=config["API_CA_T"]
OCSP_RESPONDER=config["OCSP_RESPONDER"]
HOSPITAL_HOST=config["HOSPITAL_HOST"]
HOSPITAL_PORT=int(config["HOSPITAL_PORT"])
ENDPOINT=config["ENDPOINT"]
MESSAGE_FORMAT=config["MESSAGE_FORMAT"]
NAME=config["NAME"]
# openssl s_client -connect localhost:5001 -status
ocsp_revoked = False

PULSE_STATE = Scale.MEDIUM
PRESSURE_STATE = Scale.MEDIUM
TEMP_STATE = Scale.MEDIUM
TOKEN = None

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


def message_generator(pulse_state, pressure_state, temp_state):
    now = datetime.now()

    P = pulse_state_machine(pulse_state)
    UBP = pressure_high_state_machine(pressure_state)
    LBP = pressure_low_state_machine(pressure_state)
    BT = temp_state_machine(temp_state)
    params = f"IP {API_HOST} P {P} UBP {UBP} LBP {LBP} BT {BT}"
    return MESSAGE_FORMAT.format(
        now.strftime("%Y-%m-%d %H:%M:%S"),
        NAME,
        params,
        ""
    )


def monitoring():
    msg = message_generator(PULSE_STATE, PRESSURE_STATE, TEMP_STATE)
    if not ocsp_revoked:
        send_message(msg)


def change_states():
    global PULSE_STATE, PRESSURE_STATE, TEMP_STATE
    PULSE_STATE = Scale(random.randint(Scale.ZERO.value, Scale.HIGH.value))
    PRESSURE_STATE = Scale(random.randint(Scale.LOW.value, Scale.HIGH.value))
    TEMP_STATE = Scale(random.randint(Scale.LOW.value, Scale.HIGH.value))
    print(PULSE_STATE, PRESSURE_STATE, TEMP_STATE)


def check_ocsp():
    global ocsp_revoked
    try:
        cert = None
        issuer_cert = None
        with open(API_CRT, 'r') as cert_file:
            cert = x509.load_pem_x509_certificate(bytes(cert_file.read(), encoding='ascii'))

        with open(API_CA_T, 'r') as cert_file:
            issuer_cert = x509.load_pem_x509_certificate(bytes(cert_file.read(), encoding='ascii'))
        ocsp_revoked = is_cert_revoked(OCSP_RESPONDER, cert, issuer_cert)
    except Exception as ex:
        print(ex)


def send_message(message):
    try:
        send_request(HOSPITAL_HOST, HOSPITAL_PORT, ENDPOINT, 'POST', TOKEN, message, API_CRT, API_KEY, API_CA_T)
    except Exception as ex:
        print(ex)


app = Flask(__name__)


@app.route('/csr',  methods=['POST'])
def send_public_key():
    global TOKEN
    json_data = flask.request.json
    TOKEN = flask.request.headers.get('Token')
    with open("token.txt", "w") as token_file:
        token_file.write(TOKEN)
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
        with open("token.txt", "r") as token_file:
            TOKEN = token_file.read()
        if VERIFY_USER:
            context.verify_mode = ssl.CERT_OPTIONAL
            context.load_verify_locations(API_CA_T)
        try:
            context.load_cert_chain(API_CRT, API_KEY)

            scheduler = BackgroundScheduler()
            scheduler.add_job(func=monitoring, trigger="cron", second='*/5')
            scheduler.add_job(func=check_ocsp, trigger="cron",  minute='*/1', second='30')
            scheduler.add_job(func=change_states, trigger="cron", minute='*/2')
            scheduler.start()

            atexit.register(lambda: scheduler.shutdown())
        except Exception as e:
            sys.exit("Error starting flask server. " +
                "Missing cert or key. Details: {}"
                .format(e))

    serving.run_simple(
        API_HOST, API_PORT, app, ssl_context=context)
    