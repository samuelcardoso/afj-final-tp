#!/usr/bin/env sh

echo "creating mongodb secrets"

awslocal secretsmanager --endpoint-url=http://localhost:4566 create-secret --name ProductsDatabase --secret-string "pass"

echo "password successfully created"