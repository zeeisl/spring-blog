# blog

## setup dev
```sh
mkdir -p ./data/elasticsearch
sudo chown -R 1000:1000 ./data/elasticsearch/
# create .vscode/launch.json file
# create a .env file from template.env
cp template.env .env

docker compose up -d #dev env
# wait until kibana can reach elasticsearch
curl -XPUT "http://127.0.0.1:9200/articles"
```

### .vscode/launch.json example
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
      "envFile": "${workspaceFolder}/.env"
    }
  ]
}
```

## build and deploy
```sh
./gradlew build

# create a .env file from template.env
cp template.env .env

docker compose -f docker-compose.deploy.yml --env-file .env up --build
# docker compose up -d --no-deps app --build
```
