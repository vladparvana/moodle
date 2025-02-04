CREATE TABLE utilizatori (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  rol VARCHAR(255) NOT NULL
);

CREATE TABLE studenti (
	ID INT PRIMARY KEY AUTO_INCREMENT,
	nume VARCHAR(255) NOT NULL,
	prenume VARCHAR(255) NOT NULL,
	email VARCHAR(2500) UNIQUE,
	ciclu_studii ENUM('licenta','master') NOT NULL,
	an_studiu INT NOT NULL,
	grupa INT NOT NULL
);

CREATE TABLE cadre_didactice (
	ID INT PRIMARY KEY AUTO_INCREMENT,
	nume VARCHAR(255) NOT NULL,
	prenume VARCHAR(255) NOT NULL,
	email VARCHAR(255) UNIQUE,
	grad_didactic ENUM('asist','sef_lucr','conf','prof') NULL,
	tip_asociere ENUM('titular','asociat','extern') NOT NULL,
	afiliere VARCHAR(255) NULL
);

CREATE TABLE discipline (
	COD VARCHAR(255) PRIMARY KEY,
	ID_titular INT,
	nume_disciplina VARCHAR(255) NOT NULL,
	an_studiu INT NOT NULL,
	tip_disciplina ENUM('impusa','optionala','liber_aleasa') NOT NULL,
	categorie_disciplina ENUM('domeniu','specialitate','adiacenta') NOT NULL,
	tip_examinare ENUM ('examen','colocviu') NOT NULL,
	FOREIGN KEY (ID_titular) REFERENCES cadre_didactice(ID)
);


CREATE TABLE studenti_discipline (
	ID_student INT,
	COD_disciplina VARCHAR(255),
	PRIMARY KEY (ID_student,COD_disciplina),
	FOREIGN KEY (ID_student) REFERENCES studenti(ID),
	FOREIGN KEY (COD_disciplina) REFERENCES discipline(COD)
);

create user 'academia-manager'@'172.17.0.1' identified by 'password';
grant select, insert, update, delete on academia.* to 'academia-manager'@'172.17.0.1';
flush privileges;


