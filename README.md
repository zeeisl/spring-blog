# blog

## .vscode/launch.json example
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Launch BlogApplication",
      "request": "launch",
      "mainClass": "de.zeeisl.blog.BlogApplication",
      "projectName": "blog",
      "env": {
        "DATABASE_HOST": "127.0.0.1",
        "DATABASE_NAME": "blog",
        "DATABASE_USERNAME": "",
        "DATABASE_PASSWORD": "",
        "HIBERNATE_DLL_AUTO": "create", // create-drop, update
        "SMTP_HOST": "",
        "SMTP_PORT": "587",
        "SMTP_USERNAME": "",
        "SMTP_PASSWORD": "",
        "ELASTICSEARCH_HOST": "127.0.0.1"
      }
    }
  ]
}

```

## build and deploy
```sh
# build jar
./gradlew build

# build docker image
cd deployment
cp ../build/libs/blog-0.0.1-SNAPSHOT.jar main.jar
docker build -t blog:latest .

## first start only
#mkdir -p ./data/elasticsearch
#sudo chown -R 1000:1000 ./data/elasticsearch/

docker compose up
```

## elasticsearch tests
```
PUT /articles

DELETE /articles

POST /articles/_doc/2
{
  "id": "2",
  "banner": "/uploads/hd-wallpaper-gf2f4525d7_1920.jpg",
  "teaser": "teasert 123 123 123",
  "text": "...",
  "create_at": "2022-06-09 08:03:01.687000",
  "publish_date": "2022-06-09 08:03:01.687000"
}

GET /articles/_doc/2

PUT /articles/_doc/2
{
  "id": "2",
  "banner": "/uploads/hd-wallpaper-gf2f4525d7_1920.jpg",
  "teaser": "teasert 123 123 123",
  "text": "asdf",
  "create_at": "2022-06-09 08:03:01.687000",
  "publish_date": "2022-06-09 08:03:01.687000"
}

DELETE /articles/_doc/2

GET /articles/_search
{
    "query": {
        "match_all": {}
    }
}

POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "sdf",
      "fields": ["title", "teaser", "text"]
    }
  },
  "fields": ["id"],
  "_source": false
}
```