#!/bin/bash

TEMPLATE="build/installer.sh"
DEST="build/installer/minime-install.sh"

if [ ! -f "$TEMPLATE" ]; then
    echo "ERROR: install template $TEMPLATE does not exist"
    exit 1
fi

mkdir -p build/installer
cp $TEMPLATE $DEST
echo "* Compressing binary payload"
tar cJf build/minime-@version@.tar.xz -C build/dist .
echo "* Appending payload to $DEST"
cat build/minime-@version@.tar.xz >> $DEST
chmod +x $DEST
