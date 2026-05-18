# API Documentation

## Health 

### GET /health

Check server status

Response 200

```json
"Server is running healthy!"
```

### GET /db-health

Check db status

Response 200

```json
"Database is running healthy!"
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

### GET /api/posts

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

### GET /api/posts/{postId}

Post detail query

Response 200

```json
{
  "id": 1,
  "title": "First post",
  "content": "Hello Spring JDBC",
  "authorName": "Admin",
  "createdAt": "2026-05-17T13:00:00"
}
```

### POST /api/posts/{postId}

Create post

Request
```json
{
  "userId": 1,
  "title": "New Post",
  "content": "New Content"
}
```

Response 201
```text
No body
```
Validation
```text
userId: required
title: required, max 200
content: required
```
Error
```text
404 Not Found: user does not exist
```

### PUT /api/posts/{postId}

Update post

Request
```json
{
  "title": "Updated title",
  "content": "Updated content"
}
```
Response 204
```text
No body
```
Validation
```text
title: required, max 200
content: required
```
Error
```text
404 Not Found: post does not exist
```

### DELETE /api/posts/{postId}

Delete post

Request 
```text
No body
```
Response 204 
```text
No body
```

Error
```text
404 Not Found: post does not exist
```

Note
```text 
Soft delete: the post is not physically deleted. deleted_at is set, and deleted posts are excluded from normal queries.
```