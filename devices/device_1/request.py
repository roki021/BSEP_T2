from OpenSSL.SSL import Context, TLSv1_2_METHOD, Connection
from cryptography.x509.ocsp import load_der_ocsp_response
from cryptography.x509.ocsp import OCSPResponseStatus, OCSPCertStatus
import socket

API_CRT = "keystore/device.crt"
API_KEY = "keystore/device.key"
API_CA_T = "truststore/client.crt"

def send_request(
    host,
    port,
    endpoint,
    method,
    data=None,
    cert=None,
    key=None,
    ca_cert=None,
    check_ocsp=True,
):
    def callback(conn, encoded_ocsp, data):
        ocsp_resp = load_der_ocsp_response(encoded_ocsp)
        return ocsp_resp.response_status == OCSPResponseStatus.SUCCESSFUL and ocsp_resp.certificate_status == OCSPCertStatus.GOOD

    context = Context(TLSv1_2_METHOD)
    context.load_client_ca(bytes(ca_cert, encoding="utf8"))
    context.use_certificate_file(cert)
    context.use_privatekey_file(key)
    context.set_ocsp_client_callback(callback)

    conn = Connection(context, socket.socket(socket.AF_INET, socket.SOCK_STREAM))
    conn.connect((host, port))
    conn.request_ocsp()
    conn.send(bytes(
        f"{method} {endpoint} HTTP/1.1\nHost: {host}:{port}\n" + 
        f"Content-Type: text/plain\nContent-Length: {len(data)}\n\n{data}", encoding="utf8"
    ))
    response = conn.read(2048)
    conn.close()


