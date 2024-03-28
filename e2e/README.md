# E2E

![e2e](https://github.com/rawtoast/manjuu/actions/workflows/e2e.yml/badge.svg)

Cypress test suite

# Scripts

- `npm run cypress` - starts cypress
- `npm run cypress:run` - runs cypress test suite
- `npm cypress:run:firefox` - runs the suite in Firefox 🦊
- `npm run format` - formats code with Prettier

## Environment

Run the other services using docker compose to create a test environment.

Note you need to be in the root directory (not this one!)

- Build the containers: `docker compose -f compose.ci.yml build`
- Start the services: `docker compose -f compose.ci.yml up -d`
- Stop the services: `docker compose -f compose.ci.yml down`
- View logs `docker logs <container_id>`

After starting the services, the frontend should be available at localhost:3000
