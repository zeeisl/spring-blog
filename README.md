# blog

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