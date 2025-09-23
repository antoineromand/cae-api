#!/bin/sh

cd ..

export GPG_TTY=$(tty)

helm install pea-cache-subchart k8s/charts/click-and-eat-cache/.

helm secrets install click-and-eat-db-subchart k8s/charts/click-and-eat-db/. \
  -f k8s/charts/click-and-eat-db/values.yaml \
  -f k8s/charts/click-and-eat-db/values.secret.yaml

helm secrets install click-and-eat-api-subchart k8s/charts/click-and-eat-api/. \
  -f k8s/charts/click-and-eat-api/values.yaml \
  -f k8s/charts/click-and-eat-api/values.secret.yaml