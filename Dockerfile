FROM payara/micro:5.2021.6-jdk11
COPY --chown=1001:0  target/jakarta-sqlite-rest.war /opt/payara/deployments