# Efficio - Project calculation tool

### Simplify your project management with Efficio.

Efficio is a tool that gives you an easy overview of all your projects and helps with your time management.

Access it online here: https://efficioc4.azurewebsites.net/

## Getting Started
If you want to run the application locally here are the steps to do so.

### Dependencies

* Java 21

### Installing

* Clone repository
* Open program folder in Intellij
* To automatically insert some data on startup, rename this file
```
src/main/resources/dataInsert.sql
```
to this
```
src/main/resources/data.sql
```
This will insert three users with some data. Usernames are:
```
santa
elf1
elf2
```
The password to all three users is "123"

### Executing program

* Open program folder in Intellij
* In the file `src/main/resources/application.properties` add this line `spring.profiles.active=dev` to use H2 database
* Go to file `src/main/java/keac4/efficio/EfficioApplication.java` and press "Run"

## Authors

- [Mads Gerstenberg Jacobsen](https://github.com/SkynetHD)
- [Martin Vestergaard Storm](https://github.com/MartinVStorm)
- [Sebastian Welsch Jakobsgaard](https://github.com/sebwelsch)