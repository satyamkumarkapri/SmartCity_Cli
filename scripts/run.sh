#!/usr/bin/env bash
set -e

# Change directory to project root
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." >/dev/null 2>&1 && pwd )"
cd "$DIR"

echo "=========================================================="
echo " Starting SmartCityX — Integrated Smart-City Analytics Platform"
echo "=========================================================="

# Check if target/smartcity.jar exists, if not build it
if [ ! -f "target/smartcity.jar" ]; then
    echo "[INFO] target/smartcity.jar not found. Building project with Maven..."
    mvn package -DskipTests
fi

# Run the executable JAR
java -jar target/smartcity.jar

