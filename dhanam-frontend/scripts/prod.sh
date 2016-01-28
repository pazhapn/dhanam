#!/bin/bash

export CLASSPATH=/opt/nridream/lib/*:

echo "classpath" $CLASSPATH

java -server -Djava.awt.headless=true -DLOG_DIR=/opt/nridream/log -Dlogback.configurationFile=/opt/nridream/prod-logback.xml -cp "$CLASSPATH" dhan.frontend.server.App /opt/nridream/prod.conf &