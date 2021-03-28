#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdint.h>
#include <ctype.h>

char *readInput(char *, char *);
uint8_t createCheckSum8(char *, const int);
uint16_t createCheckSum16(char *, const int);
uint32_t createCheckSum32(char *, const int);
int checkBufferSize(char *, int);
void echoText(char *);

int main(int argc, char *argv[]) {
	if (argc != 3) {
		fprintf(stderr, "ERROR: Improper amount of arguments.\n");
		return 1;
	}
	int checkSize = atoi(argv[2]); //convert 3rd command argument to integer
	if (checkSize != 8 && checkSize != 16 && checkSize != 32) { //check if argument is valid
		fprintf(stderr, "Valid checksum sizes are 8, 16, and 32\n");
		return 1;
	}

	if (argv[1] == NULL) {
		fprintf(stderr, "ERROR: Passed NULL for argument 1.\n");
		return 1;
	}
	
	//echo command arguments
	fprintf(stdout, "----COMMAND ARGUMENTS----\nArg 1: %s\nArg 2: %s\n", argv[1], argv[2]);

	//Get buffer size
	const int BUFFERSIZE = checkBufferSize(argv[1], checkSize);
	
	char *inputBuffer = malloc(sizeof(char) * BUFFERSIZE + 1);
	memset(inputBuffer, 0, BUFFERSIZE + 1);
	if (inputBuffer == NULL) {
		fprintf(stderr, "ERROR: Could not allocate properly.\n");
		return 1;
	}
	
	readInput(argv[1], inputBuffer);

	//Determine which checksum to run
	if (checkSize == 8) {
		uint8_t checkSum8 = createCheckSum8(inputBuffer, BUFFERSIZE);
		echoText(inputBuffer);
		fprintf(stdout, "\n%2d bit checksum is %81x for all %4d chars\n", checkSize, checkSum8, BUFFERSIZE);
	}
	else if (checkSize == 16) {
		uint16_t checkSum16 = createCheckSum16(inputBuffer, BUFFERSIZE);
		echoText(inputBuffer);
		fprintf(stdout, "\n%2d bit checksum is %81x for all %4d chars\n", checkSize, checkSum16, BUFFERSIZE);
	}
	else if (checkSize == 32) {
		uint32_t checkSum32 = createCheckSum32(inputBuffer, BUFFERSIZE);
		echoText(inputBuffer);
		fprintf(stdout, "\n%2d bit checksum is %81x for all %4d chars\n", checkSize, checkSum32, BUFFERSIZE);
	}
	else {
		fprintf(stderr, "Improper argument passed.");
		return 1;
	}

	return 0;
}

//Determine size of input buffer
int checkBufferSize(char *fileName, int checkSize) {
	FILE *file = fopen(fileName, "r");
	if (file == NULL) {
		fprintf(stderr, "ERROR: Could not open file.\n");
		return 1;
	}
	int i = 0;
	char c = 'a';

	while ((c = fgetc(file)) != EOF) {
		i++;
	}

	//check if buffer needs padding
	if (checkSize == 16) {
		if ((i % 2) == 1) {
			i += 1;
		}
	}
	else if (checkSize == 32) {
		if ((i % 4) == 1) {
			i += 1;
		}
		else if ((i % 4) == 2) {
			i += 2;
		}
		else if ((i % 4) == 3) {
			i += 3;
		}
	}

	fclose(file);
	return i;
}

//collect input from file
char *readInput(char *fileName, char *inputBuffer) {
	FILE *file = fopen(fileName, "r");
	if (file == NULL) {
		fprintf(stderr, "ERROR: Could not open file.\n");
		return NULL;
	}
	int i = 0;
	char *c = malloc(sizeof(char) * 2);
	memset(c, 0, 2);

	while ((c[0] = fgetc(file)) != EOF) {
		strcat(inputBuffer, c);
	}

	fclose(file);
	return inputBuffer;
}

//Run 8 bit checksum algorithm
uint8_t createCheckSum8(char *buffer, const int BUFFERSIZE) {
	uint8_t checkSum = 0;
	int i = 0; 
	for (i = 0; i < BUFFERSIZE; i++) {
		checkSum += buffer[i];
		checkSum = checkSum & 0x0FFFF; //cut off overflow bits
	}

	return checkSum;
}

//Run 16 bit checksum algorithm
uint16_t createCheckSum16(char *buffer, const int BUFFERSIZE) {
	uint16_t checkSum = 0;
	int i = 0;
	for (i = 0; i < BUFFERSIZE; i += 2) {
		if (buffer[i + 1] == '\0') { //Pad input buffer if necessary
			strcat(buffer, "X");
			checkSum += ((buffer[i] << 8) + (buffer[i + 1]));
		}
		else {
			checkSum += ((buffer[i] << 8) + (buffer[i + 1]));
		}
		checkSum = checkSum & 0x0FFFF; //cut off overflow
	}
	
	return checkSum;
}

//Run 32 bit checksum algorithm
uint32_t createCheckSum32(char *buffer, const int BUFFERSIZE) {
	uint32_t checkSum = 0;
	int i = 0;
	for (i = 0; i < BUFFERSIZE; i+= 4) {
		if (buffer[i + 1] == '\0') { //Pad input buffer if necessary (with appropriate amount of characters)
			strcat(buffer, "XXX");
			checkSum += ((buffer[i] << 24) + (buffer[i + 1] << 16) + (buffer[i + 2] << 8) + (buffer[i + 3]));
		}
		else if (buffer[i + 2] == '\0') {
			strcat(buffer, "XX");
			checkSum += ((buffer[i] << 24) + (buffer[i + 1] << 16) + (buffer[i + 2] << 8) + (buffer[i + 3]));
		}
		else if (buffer[i + 3] == '\0') {
			strcat(buffer, "X");
			checkSum += ((buffer[i] << 24) + (buffer[i + 1] << 16) + (buffer[i + 2] << 8) + (buffer[i + 3]));
		}
		else {
			checkSum += ((buffer[i] << 24) + (buffer[i + 1] << 16) + (buffer[i + 2] << 8) + (buffer[i + 3]));
		}
		checkSum = checkSum & 0x0FFFFFFFFFF; //cut off overflow
	}

	return checkSum;
}

//echo the inputBuffer
void echoText(char *buffer) {
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