#!/bin/bash

header='{"alg": "RS256", "typ": "JWT"}'
token_dir="/opt/tokens"
private_key_path="/opt/private_key.pkcs8"
public_key_path="/opt/public_key.crt"

if [ ! -f "$private_key_path" ]; then
    private_key_tempfile=$(mktemp)
    openssl genrsa -out $private_key_tempfile 2048
    openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in $private_key_tempfile -out $private_key_path
    rm $private_key_tempfile
fi

if [ ! -f "$public_key_path" ]; then
    openssl rsa -pubout -in $private_key_path -out $public_key_path
fi

mkdir -p $token_dir

token_file="$token_dir/sms-communications-token.txt"
claims='{"user": "bahmni-emr", "token_id": "'$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 10 | head -n 1)'"}'

encoded_header=$(echo -n $header | base64 | tr '+/' '-_' | tr -d '=')
encoded_payload=$(echo -n $claims | base64 | tr '+/' '-_' | tr -d '=')

data_to_sign="${encoded_header}.${encoded_payload}"

signature=$(echo -n "$data_to_sign" | openssl dgst -sha256 -sign $private_key_path | base64 | tr '+/' '-_' | tr -d '=')

jwt_token="${data_to_sign}.${signature}"
jwt_token=$(echo -n "$jwt_token" | tr -d '\n')

echo "$jwt_token" > $token_file
