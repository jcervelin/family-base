Please, see also this project coded using MongoDB here:
https://github.com/jcervelin/family-base/tree/feature/migrate-from-h2-to-mongo

branch: feature/migrate-from-h2-to-mongo

       * Provide a service to create a parent with zero or more children using an endpoint of http://localhost:8080/api/parents
	* Provide a service to retrieve a parent including their children using an endpoint of http://localhost:8080/api/parents/{id}
	* Use an embedded database such as H2 via JPA as a backing store
	* Provide a service to update a parent using an endpoint of http://localhost:8080/api/parents
	* Provide a service to update a child http://localhost:8080/api/children
  
  
  Using swagger:
  http://localhost:8080
  
  Using h2 console:
  http://localhost:8080/h2

Obs.: Ids will be included automatically.
They only will be used if it already exists. In that case it will be updated instead of saved.
At the first time those ids will be discarted. 
To get the available ids you should POST some payload e use GET to recover it.
It makes ids more standard.

/api/ was included to use swagger properly.
  
Json Sample:

```Javascript
{
  "id": 1,
  "title": "Mrs",
  "firstName": "Jane",
  "lastName": "Doe",
  "emailAddress": "jane.doe@gohenry.co.uk",
  "dateOfBirth": "1990-06-03",
  "gender": "female",
  "secondName": "",
  "children": [
    {
      "id": 2,
      "firstName": "Janet",
      "lastName": "Doe",
      "emailAddress": "janet.doe@gohenry.co.uk",
      "dateOfBirth": "2010-05-22",
      "gender": "female",
      "secondName": ""
    },
    {
      "id": 3,
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
