# POS - solutii laborator/proiect

| **Deadline&nbsp;general** | **_19.01.2025_** (_duminica, S14_) |
| :--- | :--- |
| **_Branch_ proiect** | Acest _branch_ va fi folosit de studentii care doresc sa dezvolte proiectul propus pentru disciplina. _Commit_-urile trebuie realizate saptamanal. Codurile sursa vor fi organizate pe module, iar in cadrul fiecauri astfel de modul se vor incarca un set de _log_-uri demonstrative. |
| **_Branch_&nbsp;laborator** | Acest _branch_ va fi folosit de studentii care se vor concentra doar pe aplicatiile de baza propuse. Pentru fiecare laborator se va crea cate un director separat in cadrul caruia se vor incarca fisierele sursa, alaturi de un set de _log_-uri demonstrative.<br/><br/>**_Fiecare laborator are un deadline de maxim doua saptamani de la data finalizarii acestuia!! Commit-urile ulterioare NU vor fi considerate in evaluare_** |

## Evaluare

Evaluarea activitatii de laborator consta in evaluarea solutiilor incarcate, urmarind:

- respectarea standardelor si a recomandarilor corespuzatoare tipului de serviciu analizat;
- calitatea codului sursa;
- gradul de indeplinire a cerintelor propuse.

Solutiile **_remarcabile_** pot atrage bonusuri pentru nota de examen/media corespunzatoare disciplinei. **_Revedeti documentul de prezentare a disciplinei, disponibil pe platforma_** <https://edu.tuiasi.ro/>.

## Link-uri utile

1. **Markdown** - limbaj de prezentare "pretty" pentru o documentatie `git` (in cazul in care considerati utila o scurta descriere a implementarii <https://www.markdownguide.org>)
	- sintaxa de baza: <https://www.markdownguide.org/basic-syntax/>
	- _cheat-sheet_: <https://www.markdownguide.org/cheat-sheet/>
2. **Github** - comenzi uzuale: <https://education.github.com/git-cheat-sheet-education.pdf>
  
  

# Documentatie 

Aplicatie dezvoltata in cadrul disciplinei **Programare orientata pe servicii**

## Arhitectura
<img src="https://i.imgur.com/u8zYMXt.png">

### Componentele principale ale arhitecturii
* RESTful Service integrat cu o baza de date MariaDB
* Server gRPC integrat la o baza de date MariaDB
* Server MariaDB
* RESTful Service integrat cu o baza de date MongoDB
* Server MongoDB
* Frontend folosind ReactJS ( se afla in varianta alpha)

### Tehnologii folosite
* Java Spring Boot
* gRPC
* Docker
* MariaDB
* MongoDB
* ReactJS (nu prea)

# Docker
Fiecare serviciu are propriul **Dockerfile**, fiind integrate intr-un fisier **docker-compose.yaml**

* [Fisiere docker](https://github.com/TUIasi-AC-licenta-POS/activitatea-de-laborator-2024-2025-vladparvana/tree/proiect/Docker)

# Serviciu gRPC
Este construit pentru a oferi o metodă rapidă, scalabilă si sigura pentru gestionarea de utilizatori oferind functionalitati precum autentificarea si inregistrarea.
Este mai rapid decat un serviciu REST , utilizand un protocol binar.
Ofera suport pentru
*  Creare si validare token JWT folosind HMAC-SHA256
* Adaugare , stergere, actualizare si vizualizare utilizatori din baza de date

### Interfata serviciului
```.proto
syntax = "proto3";
option java_multiple_files = true;
option java_outer_classname = "AuthProto";
package pos.proiect.auth;

service AuthService {
  rpc Login (LoginRequest) returns (LoginResponse);
  rpc ValidateToken (TokenRequest) returns (TokenResponse);
  rpc InvalidateToken (TokenRequest) returns (TokenResponse);
  rpc RegisterUser (RegisterUserRequest) returns (RegisterUserResponse);
  rpc DeleteUser (DeleteUserRequest) returns (DeleteUserResponse);
  rpc GetUser (GetUserRequest) returns (GetUserResponse);
  rpc UpdateUser (UpdateUserRequest) returns (UpdateUserResponse);
  rpc GetAllUsers (EmptyRequest) returns (GetAllUsersResponse);
}
````




### Integrare cu ReactJS

* [Cod sursa](https://github.com/TUIasi-AC-licenta-POS/activitatea-de-laborator-2024-2025-vladparvana/tree/proiect/GrpcAuth)

Folosirea unui [proxy](https://github.com/improbable-eng/grpc-web/releases)

Comanda rulare  

```console
 .\grpcwebproxy-v0.15.0-win64.exe --backend_addr=127.0.0.1:9090 --run_tls_server=false --allow_all_origins --server_http_debug_port=8082
````



# RESTful service - AcademiaAPI
* [Cod sursa](https://github.com/TUIasi-AC-licenta-POS/activitatea-de-laborator-2024-2025-vladparvana/tree/proiect/AcademiaAPI)

Dezvoltat dupa arhitectura MVC.

### Implementari

* Principii HATEOS
* Autorizare CORS
* Autorizare JWT
* Permisiuni in functie de rol
* Mappere
* Tratarea exceptiilor
* Cautare dupa anumite campuri
* Integrare cu baza de date MariaDB
* Documentatie OpenAPI

# RESTful service - AcademiaMongoAPI
* [Cod sursa](https://github.com/TUIasi-AC-licenta-POS/activitatea-de-laborator-2024-2025-vladparvana/tree/proiect/AcademiaMongoAPI)

* Tratare Exceptii
* Integrare cu baza de date MongoDB
* Posibilitatea stocarii documentelor
* Autorizare CORS
* Autorizare JWT
* Permisiuni in functie de rol
* Mappere

# Server MariaDB

* [Script creare tabele](https://github.com/TUIasi-AC-licenta-POS/activitatea-de-laborator-2024-2025-vladparvana/blob/proiect/script_creare_tabele.sql)

# Server MongoDB

# Docker
  
# Aplicatie ReactJS
* Functioneaza doar inregistrarea ca admin si crearea de noi utilizatori (student sau profesor) din contul de admin

# Testare
* Folosim Postman

### Testare gRPC

* Inregistrare

<img src="https://i.imgur.com/quzBdoo.png">
  
* Autentificare

<img src="https://i.imgur.com/X415Yjo.png">

* Validare token
<img src="https://i.imgur.com/oRCrNL1.png">

* Invalidare token
<img src="https://i.imgur.com/hKngnqM.png">
<img src="https://i.imgur.com/O6zvxU9.png">

### Testare Academia API

* Testare endpoint-uri fara token

<img src="https://i.imgur.com/RraCj0H.png">

* Testare endpoint-uri 

<img src="https://i.imgur.com/AABV6qy.png">
<img src="https://i.imgur.com/rSZ0lLo.png">
<img src="https://i.imgur.com/ABQF45c.png">
<img src="https://i.imgur.com/miMePfg.png">
<img src="https://i.imgur.com/7hx5KbR.png">
<img src="https://i.imgur.com/CtLgTzO.png">  
<img src="https://i.imgur.com/T5O4rK5.png">  
<img src="https://i.imgur.com/GbHjJ5j.png">

* Este implementata validare pe fiecare camp Not Null si Unique,
precum si alte constrangeri (email in format corespunzator, numele nu poate contine cifre etc.)

## Documentatie OpenAPI

<img src="https://i.imgur.com/aUF72sd.png">
<img src="https://i.imgur.com/XsDwt1Y.png">
<img src="https://i.imgur.com/X1h0vxS.png">
<img src="https://i.imgur.com/A81YycG.png">
<img src="https://i.imgur.com/k5onfPO.png">
<img src="https://i.imgur.com/uIg5pyX.png">
<img src="https://i.imgur.com/XboXBwR.png">
<img src="https://i.imgur.com/EAfMumT.png">


