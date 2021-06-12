import requests
import time
from cryptography import x509
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.hashes import SHA1
from cryptography.x509 import ocsp, OCSPNonce
from cryptography.x509.ocsp import OCSPResponseStatus, OCSPCertStatus


def get_oscp_request(cert, issuer_cert):
    builder = ocsp.OCSPRequestBuilder()
    builder = builder.add_certificate(cert, issuer_cert, SHA1())
    nonce = round(time.time() * 1000)
    builder = builder.add_extension(OCSPNonce(bytes(str(nonce), encoding='ascii')), True)
    req = builder.build()
    return req


def get_ocsp_cert_status(ocsp_responder, cert, issuer_cert):
    encoded_req = get_oscp_request(cert, issuer_cert).public_bytes(serialization.Encoding.DER)
    ocsp_resp = requests.post(ocsp_responder, data=encoded_req)
    if ocsp_resp.ok:
        ocsp_decoded = ocsp.load_der_ocsp_response(ocsp_resp.content)
        if ocsp_decoded.response_status == OCSPResponseStatus.SUCCESSFUL:
            return ocsp_decoded.certificate_status
        else:
            raise Exception(f'Decoding ocsp response failed: {ocsp_decoded.response_status}')
    raise Exception(f'Fetching ocsp cert status failed with response status: {ocsp_resp.status_code}')


def is_cert_revoked(ocsp_responder, cert, issuer_cert):
    return get_ocsp_cert_status(ocsp_responder, cert, issuer_cert) != OCSPCertStatus.GOOD
