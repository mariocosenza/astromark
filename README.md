# AstroMark

## Project Origin:

In the bustling corridors of modern education, a group of passionate educators and technologists recognized a critical gap in school management systems. Traditional platforms like Argo and ClasseViva were expensive, rigid, and often disconnected from the real needs of schools and students.

### The Vision

AstroMark was born from a simple yet powerful idea: create an open-source platform that empowers schools with flexible, affordable, and student-centric technology. Our founders, a diverse team of teachers, developers, and education strategists, shared a common belief that technology should simplify, not complicate, the educational experience.

### Key Challenges Addressed

- **Cost Barriers**: Eliminating high licensing fees for schools
- **Technological Accessibility**: Designing an intuitive interface for all users
- **Student Orientation**: Developing a robust guidance and career exploration module
- **Scalability**: Creating a system that grows with school needs

## Prerequisites

### Development Environment
- Java 21
- Maven 3.8+
- PostgreSQL database
- Node.js & npm
- Docker (for running TestContainers)
- React with TypeScript
- React Router

### Frontend Technologies
- React 19
- TypeScript
- React Router
- Vite

## Setup

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/astromark.git
cd astromark
```

### 2. Configure Environment Variables

Create a `.env` file with the following:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/astromark
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export JWT_SECRET=your_secret_key
```

### 3. Build Application

```bash
mvn clean install
```

### 4. Run Tests

**Note**: Running tests with TestContainers requires Docker to be installed and running.

```bash
mvn test
```

### 5. Start Application

```bash
mvn spring-boot:run
```

## Deployment

### Server Requirements

- Ubuntu 20.04+ LTS
- Java 21 Runtime
- Systemd
- Docker (recommended)

## Contributing

1. Fork repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create pull request

## License

This project is licensed under the GNU Affero General Public License v3.0 (AGPLv3).

The AGPLv3 is a copyleft license that requires any modified or extended version of the software to be distributed under the same license, even if the software is run as a network service. This ensures that all improvements and modifications remain open-source and freely available to the community.

## Key Project Dependencies

- Spring Boot 3.4.1
- PostgreSQL Driver
- JWT Authentication
- AWS SDK
- SendGrid for Email
- SpringDoc OpenAPI
- React with TypeScript
- React Router

## Monitoring and Observability

- Spring Boot Actuator integrated
- OpenAPI documentation available
- Comprehensive logging

## Authors

- [@giuseppecavallaro](https://github.com/GiuseppeCava03)
- [@mariocosenza](https://github.com/mariocosenza)
- [@mariofasolino](https://github.com/MarioFas)
- [@gsacrestano](https://github.com/gsacrestano)
