#!/bin/bash

  private_key_passphrase="bahmni"
  header='{"alg": "RS256", "typ": "JWT"}'
  token_file="/tmp/tokens/sms-tokens.txt"

  private_key_tempfile=$(mktemp)
  openssl req -new -x509 -keyout $private_key_tempfile -newkey rsa:2048 -passout pass:$private_key_passphrase -subj "/C=US/ST=State/L=City/O=Organization/OU=Organizational Unit/CN=Common Name" > /dev/null 2>&1
  openssl rsa -pubout -in $private_key_tempfile -out /tmp/public_key.pem -passin pass:$private_key_passphrase > /dev/null 2>&1


  echo "Public key generated successfully"

  mkdir -p /tmp/tokens

  > $token_file
  for ((i=1; i<=10; i++))
  do
      claims='{"user": "bahmni", "token_id": "'$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 10 | head -n 1)'"}'
      encoded_header=$(echo -n $header | base64 | tr '+/' '-_' | tr -d '=')
      encoded_payload=$(echo -n $claims | base64 | tr '+/' '-_' | tr -d '=')

      data_to_sign="${encoded_header}.${encoded_payload}"
      signature=$(echo -n "$data_to_sign" | openssl dgst -sha256 -sign $private_key_tempfile -passin pass:$private_key_passphrase | base64 | tr '+/' '-_' | tr -d '=')

      jwt_token="${data_to_sign}.${signature}"
      jwt_token=$(echo -n "$jwt_token" | tr -d '\n')
      echo "token$i=$jwt_token" >> $token_file
  done

  if [ -f $private_key_tempfile ]; then
      rm $private_key_tempfile
  fi

  if [ -f /tmp/public_key.pem ]; then
      echo "Public key file created successfully."
  else
      echo "Error: Public key file creation failed."
  fi