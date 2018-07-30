	* Provide a service to create a parent with zero or more children using an endpoint of http://localhost:8080/api/parents
	* Provide a service to retrieve a parent including their children using an endpoint of http://localhost:8080/api/parents/{id}
	* Using MongoDB embedded as a backing store
	* Provide a service to update a parent using an endpoint of http://localhost:8080/api/parents
	* Provide a service to update a child http://localhost:8080/api/children
  
  
  Using swagger:
  **`http://localhost:8080`**
  
  Connecting mongo in your client (not necessary):
  **`mongodb://localhost:27017/dbFamily`**


Obs.: Do not put made up Ids in parent nor child. The API do it for you.
They only will be used if it already exists. In that case it will be updated instead of saved.
To see the ids you may save some parent e use GET to recover it.
Only parents can be saved. 
Children can't. They only can be updated, because here we are using NoSQL (non relational database). It's only one document.

/api/ was included to use swagger properly.
  
Json Sample how should be saved:

```Javascript
{
  "title": "Mrs",
  "firstName": "Jane",
  "lastName": "Doe",
  "emailAddress": "jane.doe@gohenry.co.uk",
  "dateOfBirth": "1990-06-03",
  "gender": "female",
  "secondName": "",
  "children": [
    {
      "firstName": "Janet",
      "lastName": "Doe",
      "emailAddress": "janet.doe@gohenry.co.uk",
      "dateOfBirth": "2010-05-22",
      "gender": "female",
      "secondName": ""
    },
    {
      "firstName": "Jason",
      "lastName": "Doe",
      "emailAddress": "jason.doe@gohenry.co.uk",
      "dateOfBirth": "2008-12-05",
      "gender": "male",
      "secondName": ""
    }
  ]
}
```
After saving a Parent you will see using GET:

```Javascript
[
  {
    "id": "5b5e9017e1eae37faa95416b",
    "title": "Mrs",
    "firstName": "Jane",
    "lastName": "Doe",
    "emailAddress": "jane.doe@gohenry.co.uk",
    "dateOfBirth": "1990-06-03",
    "gender": "FEMALE",
    "secondName": "",
    "children": [
      {
        "id": "5b5e9017e1eae37faa954169",
        "firstName": "Janet",
        "lastName": "Doe",
        "emailAddress": "janet.doe@gohenry.co.uk",
        "dateOfBirth": "2010-05-22",
        "gender": "FEMALE",
        "secondName": ""
      },
      {
        "id": "5b5e9017e1eae37faa95416a",
        "firstName": "Jason",
        "lastName": "Doe",
        "emailAddress": "jason.doe@gohenry.co.uk",
        "dateOfBirth": "2008-12-05",
        "gender": "FEMALE",
        "secondName": ""
      }
    ]
  }
]
```

Apis:

```Javascript
{
  "swagger": "2.0",
  "info": {
    "version": "1.0",
    "title": "Family Base"
  },
  "host": "localhost:8080",
  "basePath": "/",
  "tags": [
    {
      "name": "children-controller",
      "description": "Operations pertaining to children"
    },
    {
      "name": "parents-controller",
      "description": "Operations pertaining to parents"
    }
  ],
  "paths": {
    "/api/children": {
      "get": {
        "tags": [
          "children-controller"
        ],
        "summary": "Find all children of all parents",
        "operationId": "findAllUsingGET",
        "produces": [
          "application/json"
        ],
        "responses": {
          "200": {
            "description": "OK",
            "schema": {
              "type": "array",
              "items": {
                "$ref": "#/definitions/Child"
              }
            }
          },
          "204": {
            "description": "No Content"
          },
          "400": {
            "description": "Bad Request"
          },
          "403": {
            "description": "Forbidden"
          },
          "404": {
            "description": "Invalid request"
          },
          "405": {
            "description": "Method Not Allowed"
          },
          "422": {
            "description": "Unprocessable Entity"
          },
          "500": {
            "description": "Internal Server Error"
          }
        }
      },
      "put": {
        "tags": [
          "children-controller"
        ],
        "summary": "Update child",
        "operationId": "updateUsingPUT",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "child",
            "description": "child",
            "required": true,
            "schema": {
              "$ref": "#/definitions/Child"
            }
          }
        ],
        "responses": {
          "201": {
            "description": "Created"
          }
        }
      }
    },
    "/api/children/{id}": {
      "get": {
        "tags": [
          "children-controller"
        ],
        "summary": "Find child by id",
        "operationId": "findByIdUsingGET",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "description": "id",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "schema": {
              "$ref": "#/definitions/Child"
            }
          },
          "204": {
            "description": "No Content"
          },
          "400": {
            "description": "Bad Request"
          },
          "403": {
            "description": "Forbidden"
          },
          "404": {
            "description": "Invalid request"
          },
          "405": {
            "description": "Method Not Allowed"
          },
          "422": {
            "description": "Unprocessable Entity"
          },
          "500": {
            "description": "Internal Server Error"
          }
        }
      }
    },
    "/api/parents": {
      "get": {
        "tags": [
          "parents-controller"
        ],
        "summary": "Find all parents",
        "operationId": "findAllUsingGET_2",
        "produces": [
          "application/json"
        ],
        "responses": {
          "200": {
            "description": "OK",
            "schema": {
              "type": "array",
              "items": {
                "$ref": "#/definitions/Parent"
              }
            }
          },
          "204": {
            "description": "No Content"
          },
          "400": {
            "description": "Bad Request"
          },
          "403": {
            "description": "Forbidden"
          },
          "404": {
            "description": "Invalid request"
          },
          "405": {
            "description": "Method Not Allowed"
          },
          "422": {
            "description": "Unprocessable Entity"
          },
          "500": {
            "description": "Internal Server Error"
          }
        }
      },
      "post": {
        "tags": [
          "parents-controller"
        ],
        "summary": "Save parent",
        "operationId": "saveUsingPOST",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "parent",
            "description": "parent",
            "required": true,
            "schema": {
              "$ref": "#/definitions/Parent"
            }
          }
        ],
        "responses": {
          "201": {
            "description": "Created"
          }
        }
      },
      "put": {
        "tags": [
          "parents-controller"
        ],
        "summary": "Update parent",
        "operationId": "updateUsingPUT_1",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "parent",
            "description": "parent",
            "required": true,
            "schema": {
              "$ref": "#/definitions/Parent"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      }
    },
    "/api/parents/{id}": {
      "get": {
        "tags": [
          "parents-controller"
        ],
        "summary": "Find parent by id",
        "operationId": "findAllUsingGET_1",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "description": "id",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "schema": {
              "$ref": "#/definitions/Parent"
            }
          },
          "204": {
            "description": "No Content"
          },
          "400": {
            "description": "Bad Request"
          },
          "403": {
            "description": "Forbidden"
          },
          "404": {
            "description": "Invalid request"
          },
          "405": {
            "description": "Method Not Allowed"
          },
          "422": {
            "description": "Unprocessable Entity"
          },
          "500": {
            "description": "Internal Server Error"
          }
        }
      }
    }
  },
  "definitions": {
    "Child": {
      "type": "object",
      "properties": {
        "dateOfBirth": {
          "type": "string",
          "format": "date"
        },
        "emailAddress": {
          "type": "string"
        },
        "firstName": {
          "type": "string"
        },
        "gender": {
          "type": "string"
        },
        "id": {
          "type": "string"
        },
        "lastName": {
          "type": "string"
        },
        "secondName": {
          "type": "string"
        }
      },
      "title": "Child"
    },
    "Parent": {
      "type": "object",
      "properties": {
        "children": {
          "type": "array",
          "items": {
            "$ref": "#/definitions/Child"
          }
        },
        "dateOfBirth": {
          "type": "string",
          "format": "date"
        },
        "emailAddress": {
          "type": "string"
        },
        "firstName": {
          "type": "string"
        },
        "gender": {
          "type": "string"
        },
        "id": {
          "type": "string"
        },
        "lastName": {
          "type": "string"
        },
        "secondName": {
          "type": "string"
        },
        "title": {
          "type": "string"
        }
      },
      "title": "Parent"
    }
  }
}

```





