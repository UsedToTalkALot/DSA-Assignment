create database  recomendation;
use recomendation;


CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(15) NOT NULL UNIQUE,  
    password VARCHAR(20) NOT NULL,
    
);


CREATE TABLE currentuser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(15) NOT NULL,
    password VARCHAR(20) NOT NULL
);



CREATE TABLE connection (
    id INT AUTO_INCREMENT PRIMARY KEY,
    follower_username VARCHAR(255) NOT NULL,
    following_username VARCHAR(255) NOT NULL,
    FOREIGN KEY (follower_username) REFERENCES user(username)
    
);


CREATE TABLE post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    discription TEXT,
    likeCount INT DEFAULT 0
    
);

CREATE TABLE preference(
	user INT,
	sport INT,
	novie INT,
	entertainment INT,
	joke INT,
	FOREIGN KEY (user) REFERENCES user(id)
);
	