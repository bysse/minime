#!/bin/bash

echo ""
echo "Minime Installer v@version@"
echo ""

if [ -z "$1" ]; then
  echo "ERROR: Missing destination directory"
  echo "Syntax: $0 <destination dir>"
  echo ""
  exit 1
fi

DEST="$1"
mkdir -p "${DEST}"

rm "$DEST/minime-*" > /dev/null 2>&1

echo "# "
echo "# Extracting archive"
echo "# "

# Find __ARCHIVE__ maker, read archive content and decompress it
ARCHIVE=$(awk '/^__ARCHIVE__/ {print NR + 1; exit 0; }' "${0}")
tail -n+${ARCHIVE} "${0}" | tar xpJv -C "${DEST}"

echo ""
echo "# "
echo "# Installation to complete."
echo "# "
echo ""
echo "# Add $DEST to your PATH to be able to execute minime-*"
echo "   PATH=\"\$PATH:$DEST\""
echo ""
echo "# To get bash completion please add this line to your .bashrc file"
echo "   source $DEST/minime_completion"
echo ""

exit 0

__ARCHIVE__
