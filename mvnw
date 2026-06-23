#!/bin/sh
BASE_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
MAVEN_VERSION="3.9.9"
MAVEN_HOME="$BASE_DIR/.mvn/apache-maven-$MAVEN_VERSION"
MAVEN_CMD="$MAVEN_HOME/bin/mvn"
MAVEN_ZIP="$BASE_DIR/.mvn/apache-maven-$MAVEN_VERSION-bin.zip"

if [ ! -x "$MAVEN_CMD" ]; then
  echo "Baixando Maven $MAVEN_VERSION para .mvn..."
  mkdir -p "$BASE_DIR/.mvn"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip" -o "$MAVEN_ZIP"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip" -O "$MAVEN_ZIP"
  else
    echo "Instale curl ou wget para baixar o Maven automaticamente."
    exit 1
  fi
  unzip -q -o "$MAVEN_ZIP" -d "$BASE_DIR/.mvn"
fi

exec "$MAVEN_CMD" "$@"
