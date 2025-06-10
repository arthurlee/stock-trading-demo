stock-trading
---

# Mongodb Get Started

## Install MongoDB in Docker
```shell
cd ~/devtools/mongodb
mkdir data
docker run --name mongodb -d -p 27017:27017 -v $(pwd)/data:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=123456 mongodb/mongodb-community-server:latest
```

## Install mongosh
```shell
$ brew tap mongodb/brew
$ brew install mongosh
```

## Connect to mongodb
```shell
mongosh "mongodb://admin:123456@localhost:27017/"
```

## insert a record
```shell
db.users.insertOne({name: 'Tom', age: 14})
```

## find records
```shell
db.users.find()
```

