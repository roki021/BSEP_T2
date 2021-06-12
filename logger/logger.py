import sys
import time
from datetime import datetime
import random

log_path = sys.argv[1]
i = 0

with open(log_path, 'a+') as f:
    while True:
        dtime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        status = random.choice(['WARN'] * 5 + ['SUCCESS'] + ['ERROR'] * 30)
        fields = {
            'username': random.choice(['jovan', 'boja', 'zoran', 'milan', 'marko']),
            'ip': random.choice(['127.0.0.2', '127.0.0.1', '127.0.0.3', '127.0.0.7', '127.0.0.5', '127.0.7.2'])
        }
        f.write('[{}] {} {} {} - username {} ip {}\n'.format(
            status, dtime, 'api/login', 'ID', fields['username'], fields['ip']))
        f.flush()
        time.sleep(.5)
