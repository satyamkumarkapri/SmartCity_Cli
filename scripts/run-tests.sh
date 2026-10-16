#!/usr/bin/env bash
set -e

# Change directory to project root
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." >/dev/null 2>&1 && pwd )"
cd "$DIR"

echo "=========================================================="
echo " Running SmartCityX Automated JUnit 5 Test Suite"
echo "=========================================================="

mvn clean test

echo "=========================================================="
echo " All tests executed successfully!"
echo "=========================================================="
