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
