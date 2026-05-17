# API Documentation

## Health 

### GET /health

Check server status

Response 200

```json
"Server is running healthy!"
```

## Users

### GET /api/users

User list query

Response 200
```json
[
  {
    "id": 1,
    "username": "admin",
    "displayName": "Admin",
    "createdAt": "2026-05-17T13:00:00"
  }
]
```

### POST /api/users

Create User

Request 
```json
{
  "username": "testuser",
  "password": "password1234",
  "displayName": "Test User"
}
```

Response 201
```text
No body
```

Validation 
```text
username: required, max 50
password: required, min 4, max 255
displayName: required, max 50
```

## Posts

### Get /api/posts

Post list query

Response 200
```json
[
  {
    "id": 1,
    "title": "First post",
    "content": "Hello Spring JDBC",
    "authorName": "Admin",
    "createdAt": "2026-05-17T13:00:00"
  }
]
```