# artsoft-backend

## Getting Started

### Create postgresql inside docker container

```
docker run --name artsoft-postgres-container -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -d -p 5432:5432 postgres
```

### Restart container
```
docker restart artsoft-postgres-container
```

### Access container
```
docker exec -it artsoft-postgres-container psql -U postgres
```

## Run smtp4dev in docker
```agsl
docker run --rm -it -p 3000:80 -p 2525:25 rnwood/smtp4dev
```

## smtp4dev web interface
```agsl
http://localhost:3000/
```

## swagger ui
```agsl
http://localhost:8080/swagger-ui/index.html
```