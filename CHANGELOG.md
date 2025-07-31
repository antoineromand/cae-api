# [](https://github.com/Pick-And-Eat-Organization/api/compare/v0.2.0...v) (2025-07-31)


### Bug Fixes

* (do not commit on main) disable checks for build-and-push jobs ([dee30eb](https://github.com/Pick-And-Eat-Organization/api/commit/dee30ebc94e56dd77dad8c1f8e811965982cec30))
* (do not commit on main) disable checks for build-and-push jobs ([a08c56a](https://github.com/Pick-And-Eat-Organization/api/commit/a08c56a244e539c2192c15a02f306cdc77460469))
* Include flyway in github actions for functional tests (an oversight). ([489d160](https://github.com/Pick-And-Eat-Organization/api/commit/489d16091bb9ad0ef0ebc3745447cddf4bef5760))


### Features

* Create deployment, pvc and service for dragonfly/redis database (dev environment). ([a2369fb](https://github.com/Pick-And-Eat-Organization/api/commit/a2369fbd320ee7f5bc54dfd429ce993442395a7a))
* Create deployment, secret, pvc and service for postgres database (dev environment). ([4f80292](https://github.com/Pick-And-Eat-Organization/api/commit/4f802921204b7be0e14e434d4ca1f01043990da7))
* Create deployment, secret, service, ingress, configmap for api. Create github-registry-secret to pull image from ghrc.io. ([4ce9a0f](https://github.com/Pick-And-Eat-Organization/api/commit/4ce9a0fa518e095e2f3e737564549d378aff6229))
* Create first helm chart (for cache). ([d4099fa](https://github.com/Pick-And-Eat-Organization/api/commit/d4099fa54bfcefe885ac4e5b9e7a12e4a5da4d58))
* Create helm chart for api (use helm secrets to encrypt secret values (pub key in .sops.yaml). ([cc6eb3c](https://github.com/Pick-And-Eat-Organization/api/commit/cc6eb3cd4d773323e9792eb8194d4af7e853fa6d))
* Create helm chart for postgres db (use helm secrets to encrypt secret values (pub key in .sops.yaml). ([971e781](https://github.com/Pick-And-Eat-Organization/api/commit/971e781e06ec61bf9e476608ddad0762dfb6d791))
* Create migrations module (to store and share migrations with test environment from each module and to anticipate a bash script executed in my helm chart to handle migration with flyway). Fixed redis configuration in authentication test. ([e87cf43](https://github.com/Pick-And-Eat-Organization/api/commit/e87cf43d94011c00a364f161e31fabf5b90eb099))
* Include flyway in github actions. ([6f16721](https://github.com/Pick-And-Eat-Organization/api/commit/6f167213a7608ce70034c3ad35d4995bd01f4b13))



