#!/bin/bash
cd "$(dirname "$0")/../bin/" || exit 1

# Load environment variables from .env file
if [ -f "../.env" ]; then
    set -a
    # shellcheck disable=SC1091
    . ../.env
    set +a
else
    echo ".env file not found."
    exit 1
fi

# Delete previous jars if they exist
[ -n "$MY_PATH" ] && [ -f "$MY_PATH" ] && rm "$MY_PATH"
[ -n "$MY_TEST" ] && [ -f "$MY_TEST" ] && rm "$MY_TEST"

jar cf "$MY_PATH" *
jar cf "$MY_TEST" *
