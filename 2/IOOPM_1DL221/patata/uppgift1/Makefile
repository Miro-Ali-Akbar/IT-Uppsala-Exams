compile:
	gcc -Wall -g driver.c yourcode.c

test: 
	@gcc -Wall -g driver.c yourcode.c
	@./a.out

memtest: compile
	valgrind --leak-check=full ./a.out
