# script 

## pre-reqs
* `init_db.sh`
* open quicktime movie mode

## beats
* :intro:
* :auth(entication|orization):
* `init_db.sh`
* start.spring.io (`auth`): Boot 4! - `security`, `web`, `jdbc`, `postgres`, `auth server`
* hello controller as the mcguffin to secure.
* add InMemoryUserDetailsManager ; inject `PasswordEncoder` to do encoding
* how do we know it works? add `HelloController` with principal
* thats nice but u wouldnt use in-memory users/pws
* lets use JDBC. 
* update the DB table to use passwords =  {sha256} || password
* the passwords are in sha256! yuck! 
* :password history shpiel:
* `JdbcUserDetailsManager` and make sure to set `enableUpdatePassword`
* try authenticating.
* show DB migration in SQL DB
* :magic links:
* "magic links"
* :passkeys:
* add `org.springframework.security` : `spring-security-webauthn`
* we can't scale the authentication; we havent locked down the API. just the client.
* auth server
* lets extract our Dogs into a standalone API. 
* start.spring.io  - (Boot 4) -  `web`, `resource server`, `data jdbc`, `postgresql`, 
`devtools`
* 
