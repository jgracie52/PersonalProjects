#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

char *readKey(char *, char *);
char *readPlain(char *, char *);
char *ciphText(char *, char *);
void echoCrypto(char *);
void echoFile(char *);

int main(int argc, char *argv[]) {
	if (argc < 3 || argc > 3) {
		fprintf(stderr, "You have entered an improper number of arguments.");
		return 1;
	}
	else if (argc == 3) {
		fprintf(stdout, "\n\n----Arguments Passed----\n");
		fprintf(stdout, "Argument 1: %s\nArgument 2: %s\n", argv[1], argv[2]);
	}

	char *keyFile = malloc(sizeof(char)*strlen(argv[1]));
	if (keyFile == NULL) {
		fprintf(stderr, "ERROR: Could not pass file name.");
		return 1;
	}
	char *plainFile = malloc(sizeof(char)*strlen(argv[2]));
	if (plainFile == NULL) {
		fprintf(stderr, "ERROR: Could not pass file name.");
		return 1;
	}
	strcpy(keyFile, argv[1]);
	strcpy(plainFile, argv[2]);

	char *key = malloc(sizeof(char) * 512);
	if (key == NULL) {
		fprintf(stderr, "ERROR: Failed memory allocation");
		return 1;
	}
	char *plain = malloc(sizeof(char) * 512);
	if (plain == NULL) {
		fprintf(stderr, "ERROR: Failed memory allocation");
		return 1;
	}
	char *ciph = malloc(sizeof(char) * 512);
	if (ciph == NULL) {
		fprintf(stderr, "ERROR: Failed memory allocation");
		return 1;
	}

	readKey(key, keyFile);
	//fprintf(stdout, "\n\n---- ECHO KEY ----\n");
	//echoFile(argv[1]); //Prints unchanged file data in original format
	printf("\n");
	echoCrypto(key);

	readPlain(plain, plainFile);
	//fprintf(stdout, "\n\n---- ECHO PLAINTEXT ----\n");
	//echoFile(argv[2]); //Prints unchanged file data in original format
	printf("\n");
	echoCrypto(plain);

	strcpy(ciph, ciphText(plain, key));
	//fprintf(stdout, "\n\n---- ECHO CIPHERTEXT ----\n");
	printf("\n");
	echoCrypto(ciph);
	return 0;
}

//Reads key file and places data in key buffer
char *readKey(char *bufferK, char *keyFile) {

	FILE *file = fopen(keyFile, "r");
	if (file == NULL) {
		fprintf(stderr, "ERROR: Could not open file.");
		return NULL;
	}

	int i = 0;
	char *c = malloc(sizeof(char) * 2);
	while ((c[0] = fgetc(file)) != EOF)
	{
		if (i >= 512) {
			return bufferK;
		}
		if (isalpha(c[0]) != 0) {
			c[0] = tolower(c[0]);
			strcat(bufferK, c);
			i += 1;
		}
	}
	//fclose(file); //Was causing segfaults. Couldn't figure out why
	return bufferK;
}

//Reads plaintext file and places data in file (if data is less than 512 characters then fills with 'x')
char *readPlain(char *bufferP, char *plainFile) {

	FILE *file = fopen(plainFile, "r");
	if (file == NULL) {
		fprintf(stderr, "ERROR: Could not open file.");
		return NULL;
	}

	int i = 0;
	char *c = malloc(sizeof(char) * 2);
	while ((c[0] = fgetc(file)) != EOF) {
		if (i >= 512) {
			return bufferP;
		}
		if (isalpha(c[0]) != 0) {
			c[0] = tolower(c[0]);
			strcat(bufferP, c);
			i += 1;
		}
	}

	if (strlen(bufferP) < 512) {
		for (; i < 512; i++) {
			strcat(bufferP, "x");
		}
	}
	//fclose(file);
	return bufferP;
}

//Encrypts text using key and plaintext buffers with the Vigenére Cipher
char *ciphText(char *bufferP, char *bufferK) {
	char *bufferC = malloc(sizeof(char) * 512);
	if (bufferC == NULL) {
		fprintf(stderr, "ERROR: Could not allocate memory.");
	}
	int plainLen = strlen(bufferP);
	int keyLen = strlen(bufferK);
	int i;
	for (i = 0; i < plainLen; i++) {
		bufferC[i] = (char)((((int)bufferP[i] - 97 + (int)bufferK[i % keyLen] - 97) % 26) + 97);
	}
	bufferC[i] = '\0';
	return bufferC;
}

//Echos the encrypted text (or key/plaintext buffers)
void echoCrypto(char *buffer) {
	int i = 0;
	int j = 0;
	while (buffer[i] != '\0') {
		for (j = 0; j < 80; j++) {
			if (buffer[i] == '\0') {
				return;
			}
			fprintf(stdout, "%c", buffer[i]);
			i++;
		}
		fprintf(stdout, "\n");
	}
	return;
}

//Echos the file data without changing data or formatting
void echoFile(char *buffer) {
	FILE *file = fopen(buffer, "r");
	char c = 'A';
	int i = 0;

	while ((c = fgetc(file)) != EOF) {
		fprintf(stdout, "%c", c);
		i += 1;
		/*if (i = 80) {
		fprintf(stdout, "\n");
		i = 0;
		}*/
	}
	//fclose(file);
}