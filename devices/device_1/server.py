import flask
from flask import Flask
from flask import request
from OpenSSL import SSL
from os import path
import requests
import string
import random
from werkzeug import serving

import ssl
import sys


HTTPS_ENABLED = True
VERIFY_USER = True

API_HOST = "0.0.0.0"
API_PORT = 5000
API_CRT = "root.crt"
API_KEY = "root.key"
API_CA_T = "../ca.crt"

app = Flask(__name__)

messages = list()


@app.route('/', methods=['GET'])
def show_messages():
    ret_val = ''
    for message in messages:
        ret_val += f'<p>{message}</p>'

    ret_val += '<br/><a href="/send"><button>Send message</button></a>'
    
    return ret_val


@app.route('/receive',  methods=['POST'])
def receive():
    messages.append(f'hospital: {request.data}')

    status_code = flask.Response(status=200)
    return status_code


@app.route('/send',  methods=['GET'])
def send():
    letters = string.ascii_letters
    message = ''.join(random.choice(letters) for i in range(10))
    messages.append(f'device: {message}')
    status_code = 200
    try:
        resp = requests.post('https://localhost:8081/api/receive', data=message, cert=(API_CRT, API_KEY))

        status_code = flask.Response(status=resp.status_code)
    except Exception as ex:
        print(ex)

    return "sss"


if __name__ == '__main__':
    context = None
    if HTTPS_ENABLED:
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
        '0.0.0.0', API_PORT, app, ssl_context=context)