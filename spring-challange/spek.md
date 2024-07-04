# User API Spec

## Register User

Endpoint : POST /api/users

Request Body :

```json
{
  "username" : "beneboba",
  "password" : "beneboba123",
  "name" : "Benediktus Satriya" 
}
```

Response Body (Success) :

```json
{
  "data" : "OK"
}
```

Response Body (Failed) :

```json
{
  "errors" : "Username must not blank, ??? bla bla bla"
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body :

```json
{
  "username" : "beneboba",
  "password" : "beneboba123" 
}
```

Response Body (Success) :

```json
{
  "data" : {
    "token" : "blablabla",
    "expiredAt" : 2342342423423 // milliseconds
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Username or password wrong"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : {
    "username" : "beneboba",
    "name" : "Benediktus Satriya"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Unauthorized"
}
```

## Update User

Endpoint : PATCH /api/users/current

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "name" : "Budi Pekerti", // put if only want to update name
  "password" : "budi12345" // put if only want to update password
}
```

Response Body (Success) :

```json
{
  "data" : {
    "username" : "beneboba",
    "name" : "Budi Pekerti"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Unauthorized"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : "OK"
}
```