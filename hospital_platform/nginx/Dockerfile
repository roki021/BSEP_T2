FROM nginx:1.20.0-alpine
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d

RUN apk update

COPY reload-cron /etc/cron.d/reload-cron

RUN chmod 0644 /etc/cron.d/reload-cron
RUN crontab /etc/cron.d/reload-cron
RUN touch /var/log/cron.log