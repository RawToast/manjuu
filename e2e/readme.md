# E2E

Cypress test suite

# Scripts

- `npm run cypress` - starts cypress
- `npm run cypress:run` - runs cypress test suite
- `npm cypress:run:firefox` - runs the suite in Firefox 🦊
- `npm run format` - formats code with Prettier

## Environment

Run the other services using docker compose to create a test environment.

Note you need to be in the root directory (not this one!)

- Build the containers: `docker-compose -f docker-compose.ci.yml build`
- Start the services: `docker-compose -f docker-compose.ci.yml up -d`
- Stop the services: `docker-compose -f docker-compose.ci.yml down`
- View logs `docker logs <container_id>`
