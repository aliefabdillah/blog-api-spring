# Blog API Spec

## Authentication

### OAuth
Request :
- Method : GET
- Endpoint : `/index.html`

Response :

```json
{
  "token": "string",
  "token_type": "string"
}
```

### Register
Request : 
- Method : POST
- Endpoint : `/api/user/register`
- Header : 
  - Content-Type: application/json
  - Accept: application/json
- Body :
```json
{
    "name":"string",
    "email":"string|email",
    "password":"string|min(6)"
}
```
Response :
```json
{
    "status": "CREATED",
    "code": 201,
    "data": {
        "id": "string",
        "name": "string",
        "email": "string",
        "imgProfile": "string",
        "createdAt": "date",
        "updatedAt": "date"
    }
}
```

### Login
Request :
- Method : POST
- Endpoint : `/api/user/login`
- Header :
    - Content-Type: application/json
    - Accept: application/json
- Body :
```json
{
    "email":"string|email",
    "password":"string|min(6)"
}
```
Response :
```json
{
  "status": "OK",
  "code": 200,
  "data": {
    "token": "string"
  }
}
```

## User
### User Profile
Request :
- Method : GET
- Endpoint : `/api/user/me`
- Header :
  - Authorization : Bearer `JWT_TOKEN`
Response :
```json
{
  "status": "OK",
  "code": 200,
  "data": {
    "id": "string",
    "name": "string",
    "email": "string",
    "imgProfile": "string",
    "createdAt": "date",
    "updatedAt": "date"
  }
}
```

### User Details
Request :
- Method : GET
- Endpoint : `/api/user/me/details`
- Header :
  - Authorization : Bearer `JWT_TOKEN`
Response :
```json
{
  "status": "OK",
  "code": 200,
  "data": {
    "id": "string",
    "name": "string",
    "email": "string",
    "imgProfile": "string",
    "createdAt": "date",
    "updatedAt": "date",
    "articles": [
      {
        "id": "string",
        "title": "string",
        "content": "string",
        "imgCover": "string",
        "authorId": "string",
        "createdAt": "date",
        "updatedAt": "date"
      },
      {
        "id": "string",
        "title": "string",
        "content": "string",
        "imgCover": "string",
        "authorId": "string",
        "createdAt": "date",
        "updatedAt": "date"
      }
    ]
  }
}
```

### Update Profile
Request :
- Method : PATCH
- Endpoint : `/api/user/me`
- Header :
  - Authorization : Bearer `JWT_TOKEN`
  - Content-Type: form-data
  - Accept: form-data
- Body :
```text
  - name : Text
  - files : Single File
```
Response :
```json
{
  "status": "OK",
  "code": 200,
  "data": {
    "id": "string",
    "name": "string",
    "email": "string",
    "imgProfile": "string",
    "createdAt": "date",
    "updatedAt": "date"
  }
}
```

## Article
### Create Article
Request :
- Method : POST
- Endpoint : `/api/article`
- Header :
  - Authorization : Bearer `JWT_TOKEN`
  - Content-Type: form-data
  - Accept: form-data
- Body :
```text
  - title : Text
  - content : Text
  - files : Single File
```
Response :
```json
{
  "status": "CREATED",
  "code": 201,
  "data": {
    "id": "string",
    "title": "string",
    "content": "string",
    "imgCover": "string",
    "authorId": "string",
    "createdAt": "date",
    "updatedAt": "date",
    "author": {
      "id": "string",
      "name": "string",
      "email": "string",
      "imgProfile": "string",
      "createdAt": "date",
      "updatedAt": "date"
    }
  }
}
```

### Get Details Article
Request :
- Method : GET
- Endpoint : `/api/article/{id}`

Response :
```json
{
  "status": "OK",
  "code": 200,
  "data": {
    "id": "string",
    "title": "string",
    "content": "string",
    "imgCover": "string",
    "authorId": "string",
    "createdAt": "date",
    "updatedAt": "date",
    "author": {
      "id": "string",
      "name": "string",
      "email": "string",
      "imgProfile": "string",
      "createdAt": "date",
      "updatedAt": "date"
    }
  }
}
```

### List Article
Request :
- Method : GET
- Endpoint : `/api/article/list?query1=...`
- Query :
  ```text
    - size : number|min(1)
    - page : number|min(0)
    - title : string
    - authorName: string
  ```
Response :

```json
{
  "status": "OK",
  "code": 200,
  "pagination": {
    "current": 1,
    "perPage": 1,
    "lastPage": 1,
    "total": 1
  },
  "data": [
    {
      "id": "string",
      "title": "string",
      "content": "string",
      "imgCover": "string",
      "authorId": "string",
      "createdAt": "date",
      "updatedAt": "date",
      "author": {
        "id": "string",
        "name": "string",
        "email": "string",
        "imgProfile": "string",
        "createdAt": "date",
        "updatedAt": "date"
      }
    }
  ]
}
```

### Update Article
Request :
- Method : PATCH
- Endpoint : `/api/article/{id}`
- Header :
  - Authorization : Bearer `JWT_TOKEN`
  - Content-type : multipart/form-data
  - Accept : /**
- Body :
```text
  - title : Text
  - content : Text
  - files : Single File
```
Response : 
```json
{
  "status": "OK",
  "code": 200,
  "data": {
    "id": "string",
    "title": "string",
    "content": "string",
    "imgCover": "string",
    "authorId": "string",
    "createdAt": "date",
    "updatedAt": "date"
  }
}
```

### Delete Article
Request : 
- Method : DELETE
- Endpoint : `/api/article/{id}`
- Header : 
  - Authorization : Bearer `JWT_TOKEN`

Response :
```json
{
    "status": "OK",
    "code": 200,
    "data": null
}
```
