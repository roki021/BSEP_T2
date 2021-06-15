import sys
import time
from datetime import datetime
import random

log_path = sys.argv[1]

def normal(speed, f):
    print('NORMAL')
    dtime = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')
    corpus = [
        '[SUCCESS] {} api/login LOGINSUCCESS - username {} ip {}',
        '[WARNING] {} api/login NULLTOKEN - username {} ip {}',
        '[SUCCESS] {} api/login LOGINSUCCESS - username {} ip {}',
        '[WARNING] {} api/login NULLTOKEN - username {} ip {}',
        '[WARNING] {} api/login NULLTOKEN - username {} ip {}',
        '[SUCCESS] {} api/login LOGINSUCCESS - username {} ip {}',
        '[WARNING] {} api/login NULLTOKEN - username {} ip {}',
        '[WARNING] {} api/login NULLTOKEN - username {} ip {}',
        '[SUCCESS] {} api/login LOGINSUCCESS - username {} ip {}',
    ]

    for i in range(len(corpus)):
        dtime = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')
        r = corpus[i]
        print(r.format(dtime, 'marko', '126.4.2.1'))
        f.write(r.format(dtime, 'marko', '126.4.2.1') + '\n')
        f.flush()
        time.sleep(speed)


def dos(f):
    print('DOS')
    corpus = ['[SUCCESS] {} api/login LOGINSUCCESS - username {} ip {}'] * 8 + ['[WARNING] {} api/login NULLTOKEN - username {} ip {}']

    for i in range(65):
        dtime = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')
        r = random.choice(corpus)
        v = r.format(dtime, random.choice(['jojo', 'zozon', 'zika', 'baba', 'sdsfsd', 'asdfa', 'asds']), random.choice(['123.2.1.2']))
        print(v)
        f.write(v + '\n')
        f.flush()
        time.sleep(0.5)


def auth_lock(f):
    print("AUTH LOCK")
    for i in range(3):
        dtime = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')
        print('[WARNING] {} api/login NULLTOKEN - username doctorSteva ip 129.1.1.1'.format(dtime))
        f.write('[WARNING] {} api/login NULLTOKEN - username doctorSteva ip 129.1.1.1\n'.format(dtime))
        f.flush()
        time.sleep(2)

def auth_bf(f):
    print("AUTH_BF")
    for i in range(20):
        dtime = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')
        f.write('[WARNING] {} api/login LOGINLOCKED - username doctorSteva ip 129.1.1.1\n'.format(dtime))
        f.flush()
        print('[WARNING] {} api/login LOGINLOCKED - username doctorSteva ip 129.1.1.1'.format(dtime))
        time.sleep(2)

def error(f):
    print("ERROR")
    for i in range(10):
        dtime = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')
        f.write('[ERROR] {} api/login CSREXCEPTION - username zozon ip {}\n'.format(dtime, random.choice(['124.321.12.3', '124.321.12.4', '124.321.12.5', '124.321.12.6'])))
        print('[ERROR] {} api/login CSREXCEPTION - username zozon ip {}'.format(dtime, random.choice(['124.321.12.3', '124.321.12.4', '124.321.12.5', '124.321.12.6'])))
        f.flush()
        time.sleep(2)

if __name__ == '__main__':
    with open(log_path, 'a+') as f:
        while True:
            normal(2, f)
            dos(f)
            auth_lock(f)
            auth_bf(f)
            error(f)
            time.sleep(1)