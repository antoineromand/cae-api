FROM flyway/flyway:latest

COPY migrations/src/main/resources/db/migration /flyway/sql

COPY scripts/flyway-wait-for-db.sh /flyway/wait-for-db.sh
RUN chmod +x /flyway/wait-for-db.sh
ENTRYPOINT ["/flyway/wait-for-db.sh"]