CREATE TABLE todo (
	id  	    IDENTITY PRIMARY KEY,
	description VARCHAR(2000) NOT NULL,
	state   	VARCHAR(20) NOT NULL, 
	img   		BLOB NOT NULL
);
